package ml.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.logging.Level;

import ml.core.Config.Prop.Renamed;
import net.minecraftforge.common.ConfigCategory;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.FMLLog;

public abstract class Config {

	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Prop {
		public String category() default Configuration.CATEGORY_GENERAL;
		public String comment() default "";
		public String inFileName() default "";
		
		@Retention(RetentionPolicy.RUNTIME)
		public static @interface Renamed {
			public String[] value();
		}
	}
	
	public abstract String getFailMsg();
	
	public static Property.Type getForgeType(Class cls) {
		if (cls.isArray()) cls = cls.getComponentType();
		if (cls == int.class || cls == Integer.class)
			return Property.Type.INTEGER;
		else if (cls == boolean.class || cls == Boolean.class)
			return Property.Type.BOOLEAN;
		else if (cls == double.class || cls == Double.class)
			return Property.Type.DOUBLE;
		else if (cls == String.class)
			return Property.Type.STRING;
		return null;
	}
		
	public void load(Configuration cfg) {
		try {
			for (Field fld : this.getClass().getFields()){
				Prop ann = fld.getAnnotation(Prop.class);
				if (ann != null){
					Class type = fld.getType();
					String propName = ann.inFileName().isEmpty() ? fld.getName() : ann.inFileName();
					Property cProp = null;
					
					Property.Type forgeTyp = getForgeType(type);
					
					Renamed oldNames = fld.getAnnotation(Renamed.class);
					if (oldNames != null)
						for (String onm : oldNames.value()) {
							String[] path = onm.split("\\"+Configuration.CATEGORY_SPLITTER);
							onm = path[path.length-1];
							ConfigCategory cat = cfg.getCategory(path.length>1 ? StringUtils.join(Arrays.copyOf(path, path.length-1), ".") : ann.category());
							if (cat != null && cat.containsKey(onm)) {
								cProp = cat.get(onm);
								cat.remove(onm);
							}
						}
					
					if (type.isArray()) {
						cProp = cfg.get(ann.category(), propName, cProp!=null ? cProp.getStringList() : (String[])fld.get(this), null, forgeTyp);
						switch (forgeTyp) {
						case INTEGER:
							fld.set(this, cProp.getIntList());
							break;
						case BOOLEAN:
							fld.set(this, cProp.getBooleanList());
							break;
						case DOUBLE:
							fld.set(this, cProp.getDoubleList());
							break;
						case STRING:
							fld.set(this, cProp.getStringList());
							break;
						}
					} else {
						cProp = cfg.get(ann.category(), propName, cProp!=null ? cProp.getString() : fld.get(this).toString(), null, forgeTyp);
						switch (forgeTyp) {
						case INTEGER:
							fld.set(this, cProp.getInt(fld.getInt(this)));
							break;
						case BOOLEAN:
							fld.set(this, cProp.getBoolean(fld.getBoolean(this)));
							break;
						case DOUBLE:
							fld.set(this, cProp.getDouble(fld.getDouble(this)));
							break;
						case STRING:
							fld.set(this, cProp.getString());
							break;
						}
					}
										
					if (cProp != null && !ann.comment().isEmpty())
						cProp.comment = ann.comment();
				}
			}
		} catch(Exception e) {
			FMLLog.log(Level.SEVERE, e, getFailMsg());
		} finally {
			cfg.save();
		}
	}
}
