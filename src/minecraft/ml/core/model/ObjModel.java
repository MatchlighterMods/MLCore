package ml.core.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjModel {
	
	public static class ObjModelImporter{
		
		public List<Vector3> vertices;
		public Map<Integer, String> groups;
		public List<double[]> uvs;
		public List<faceVert[]> faces;
		
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
		
		private void parseFace(StreamTokenizer st) throws IOException {
			List<faceVert> fverts = new ArrayList<ObjModel.ObjModelImporter.faceVert>();
			while (st.nextToken() != st.TT_EOL && st.ttype != st.TT_EOF){
				if (st.ttype != st.TT_NUMBER)
					throw new IOException("Failed to parse int");
				
				int vref = (int)st.nval;
				if (st.nextToken() != '/')
					throw new IOException("Failed to parse face");
				
				fverts.add(new faceVert(vref, readInt(st), 0));
			}
			faces.add((faceVert[])fverts.toArray());
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
				if (st.sval == "v"){
					Vector3 nv = new Vector3(readDouble(st), readDouble(st), readDouble(st));
					vertices.add(nv);
					readLineEnd(st);
					continue;
				} else if (st.sval == "vt") {
					// TODO vt
					readLineEnd(st);
					continue;
				} else if (st.sval == "f") {
					parseFace(st);
					readLineEnd(st);
					continue;
				} else if (st.sval == "g") {
					groups.put(faces.size(), readString(st));
					readLineEnd(st);
					continue;
				}
			}
		}
		
		private static class faceVert {
			public int vertRef;
			public int uvRef;
			public double normal;
			
			public faceVert(int vert, int uv, int norm) {
				vertRef = vert;
				uvRef = uv;
				normal = norm;
			}
		}
	}
}
