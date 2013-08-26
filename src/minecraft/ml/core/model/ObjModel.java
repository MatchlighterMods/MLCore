package ml.core.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ml.core.vec.Vector3;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Parses Wavefront OBJs so they can be rendered.
 * <ul>
 * <li>In Blender, Y+ is North, X+ East, Z+ Up. Models should be modeled in the X+,Y-,Z+ Octant</li>
 * </ul>
 * 
 * @author Matchlighter
 *
 */
@Deprecated() //Forge added an equivalent. Only keeping for posterity ;)
public class ObjModel {

	private Map<String, List<uvVertex[]>> groups = new HashMap<String, List<uvVertex[]>>();
	private Map<String, Integer> cpldGroups = new HashMap<String, Integer>();

	public ObjModel(ObjModelImporter objMI) {
		List<uvVertex[]> cgroup = new ArrayList<ObjModel.uvVertex[]>();
		String gName = "Default";
		for (int i=0; i<objMI.faces.size(); i++){
			if (objMI.groups.containsKey(i)){
				if (cgroup.size()>0){
					groups.put(gName, cgroup);
				}
				cgroup = new ArrayList<ObjModel.uvVertex[]>();
				gName = objMI.groups.get(i);
			}
			cgroup.add(objMI.faces.get(i));
		}
		groups.put(gName, cgroup);
	}

	public static ObjModel loadFromResource(String resName){
		InputStream ris = ObjModel.class.getResourceAsStream(resName);
		ObjModel objMI;
		try {
			objMI = new ObjModel(new ObjModelImporter(ris));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return objMI;
	}

	public void renderAll(){
		for (String gn : groups.keySet()){
			renderGroup(gn);
		}
	}

	public void renderGroup(String gName){
		if (groups.containsKey(gName)){
			if (!cpldGroups.containsKey(gName)){
				compileDisplayList(gName, 0);
			}
			GL11.glCallList(cpldGroups.get(gName));
		}
	}

	@SideOnly(Side.CLIENT)
	private void compileDisplayList(String grp, float par1)
	{
		int dl = GLAllocation.generateDisplayLists(1);
		GL11.glNewList(dl, GL11.GL_COMPILE);
		Tessellator tes = Tessellator.instance;
		
		for (uvVertex[] face : groups.get(grp)){
			tes.startDrawingQuads();
			for (uvVertex vert : face){
				if (vert.normal != null){
					tes.setNormal((float)vert.normal.x, (float)vert.normal.y, (float)vert.normal.z);
				}
				tes.addVertexWithUV(vert.vertex.x, vert.vertex.y, vert.vertex.z, vert.uv.u, vert.uv.v);
			}
			tes.draw();
		}

		GL11.glEndList();
		cpldGroups.put(grp, dl);
	}

	public static class ObjModelImporter {

		public List<Vector3> vertices = new ArrayList<Vector3>();
		public List<uvPair> uvs = new ArrayList<uvPair>();
		public List<Vector3> normals = new ArrayList<Vector3>();
		public List<uvVertex[]> faces = new ArrayList<uvVertex[]>();
		public Map<Integer, String> groups = new HashMap<Integer, String>();

		private int readInt(StreamTokenizer st) throws IOException {
			if (st.nextToken() != st.TT_NUMBER)
				throw new IOException("Failed to parse int");
			return (int)st.nval;
		}

		private double readDouble(StreamTokenizer st) throws IOException {
			if (st.nextToken() != st.TT_NUMBER)
				throw new IOException("Failed to parse double");
			return st.nval;
		}

		private String readString(StreamTokenizer st) throws IOException {
			if (st.nextToken() != st.TT_WORD)
				throw new IOException("Failed to parse String");
			return st.sval;
		}

		private void readLineEnd(StreamTokenizer st) throws IOException {
			if (st.nextToken() != st.TT_EOL)
				throw new IOException("EOL expected");
		}

		private void readToEOL(StreamTokenizer st) throws IOException {
			while (st.nextToken() != st.TT_EOL);
		}

		private void parseFace(StreamTokenizer st) throws IOException {
			List<uvVertex> fverts = new ArrayList<uvVertex>();
			st.nextToken();
			while (st.ttype != st.TT_EOL && st.ttype != st.TT_EOF){
				if (st.ttype != st.TT_NUMBER)
					throw new IOException("Failed to parse int");
				
				int vref = (int)st.nval-1;
				if (st.nextToken() != '/')
					throw new IOException("Failed to parse face");
				
				int uvref = readInt(st)-1;
				
				int nref = -1;
				if (st.nextToken() == '/'){
					nref = readInt(st)-1;
					st.nextToken();
				}
				fverts.add(new uvVertex(this, vref, uvref, nref));
			}
			if (fverts.size()<3 || fverts.size()>4){
				throw new IllegalArgumentException("Face must have no less than 3 or no more than 4 vertices");
			}
			faces.add(fverts.toArray(new uvVertex[fverts.size()]));
		}

		public ObjModelImporter(InputStream is) throws IOException {
			Reader r = new BufferedReader(new InputStreamReader(is));
			StreamTokenizer st = new StreamTokenizer(r);
			st.ordinaryChar('/');
			st.commentChar('#');
			st.quoteChar('"');
			st.eolIsSignificant(true);
			st.lowerCaseMode(false);
			st.parseNumbers();

			while (st.nextToken() != st.TT_EOF){
				if (st.ttype == st.TT_EOL){
					continue;
				} else if (st.ttype != st.TT_WORD) {
					throw new IOException("Expected an OBJ type to start the line");
				}

				// If only it could be assumed that everyone had Java 7...
				if (st.sval.equals("v")){
					Vector3 nv = new Vector3(readDouble(st), readDouble(st), readDouble(st));
					vertices.add(nv);
					readLineEnd(st);
					continue;
				} else if (st.sval.equals("vt")) {
					uvs.add(new uvPair(readDouble(st), 1-readDouble(st)));
					readLineEnd(st);
					continue;
				} else if (st.sval.equals("vn")){
					Vector3 nv = new Vector3(readDouble(st), readDouble(st), readDouble(st));
					normals.add(nv);
					readLineEnd(st);
					continue;
				} else if (st.sval.equals("f")) {
					parseFace(st);
					//readLineEnd(st);
					continue;
				} else if (st.sval.equals("g")) {
					groups.put(faces.size(), readString(st));
					readLineEnd(st);
					continue;
				} else {
					readToEOL(st);
				}
			}
		}
	}
	
	public static class uvPair {
		public double u;
		public double v;

		public uvPair(double iu, double iv) {
			u = iu;
			v = iv;
		}
	}

	public static class uvVertex {
		public Vector3 vertex;
		public uvPair uv;
		public Vector3 normal;

		public uvVertex(Vector3 ivert, uvPair iuv, Vector3 norm) {
			vertex = ivert;
			uv = iuv;
			normal = norm;
		}
		
		public uvVertex(ObjModelImporter imp, int vref, int uvref, int nref) {
			vertex = imp.vertices.get(vref);
			if (uvref>-1)
				uv = imp.uvs.get(uvref);
			if (nref>-1)
				normal = imp.normals.get(nref);
		}
	}
}
