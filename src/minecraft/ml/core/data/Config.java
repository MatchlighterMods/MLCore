package ml.core.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import ml.core.data.Config.Prop.Renamed;
import ml.core.internal.CoreLogger;
import ml.core.util.StringUtils;
import net.minecraftforge.common.ConfigCategory;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public abstract class Config {

	/**
	 * Property names will be determined by the field name if {@link Prop#inFileName()} is not set.<br/><br/>
	 * 
	 * If you prefix a field name with the camelCased category name and an '_', the prefix will be removed when saved to file.<br/>
	 * <b>Ex:</b> "generalChild_property" will be saved as simply "property" if it is in the category "general.child"<br/>
	 * In this scenario, "child_property" will also become just "property" in the config file; you can go as deep or challow into the category ancestory as you want.
	 * 
	 * @author Matchlighter
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface Prop {
		public String category() default Configuration.CATEGORY_GENERAL;
		public String comment() default "";
		public String inFileName() default "";
		
		/**
		 * If you rename a property, add this annotation with any old names.<br/>
		 * The config will load the most recent name that it can find.<br/>
		 * Will resave as the latest name and delete old names.<br/><br/>
		 * 
		 * End the pattern with a . to take the current name (For easily changing categories)<br/><br/>
		 * 
		 * *Do NOT create a new property with the name of a renamed property. It will be deleted.
		 */
		@Retention(RetentionPolicy.RUNTIME)
		public static @interface Renamed {
			public String[] value();
		}
	}
	
	/**
	 * Thanks to monoxide0184 for the idea of add category comments
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface Category {
		public String category() default Configuration.CATEGORY_GENERAL;
		public String comment() default "";

	}
	
	protected Configuration fcfg;
	private List<Object> mods = new ArrayList<Object>();
	
	public void addModule(Object mod) {
		mods.add(mod);
	}
	
	public Config(Configuration cfg) {
		fcfg = cfg;
	}
	
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
	
	protected void loadModule(Object modInst, Class modCls) throws IllegalAccessException {
		for (Field fld : modCls.getFields()){
			if (modInst != null || Modifier.isStatic(fld.getModifiers())) {
				Category cat_ann = fld.getAnnotation(Category.class);
				if (cat_ann != null) {
					String catn = cat_ann.category();
					String com = cat_ann.comment().isEmpty() ? null : cat_ann.comment();
					if (catn.isEmpty()) {
						if (fld.getType() == String.class && (String)fld.get(modInst)!=null) {
							catn = (String)fld.get(modInst);
						} else {
							catn = fld.getName();
						}
					}
					fcfg.addCustomCategoryComment(catn, com);
					if (fld.getType() == ConfigCategory.class) {
						fld.set(modInst, fcfg.getCategory(catn));
					}
				}
				
				Prop prop_ann = fld.getAnnotation(Prop.class);
				if (prop_ann != null){
					Class type = fld.getType();
					String fldName = fld.getName();
					
					String[] cats = prop_ann.category().split("\\.");
					for (int i=cats.length-1; i>=0; i--) {
						String cccat = cats[i].toLowerCase();
						for (int j=i+1; j<cats.length; j++) {
							cccat += cats[j].substring(0, 1).toUpperCase() + cats[j].substring(1);
						}
						cccat+="_";
						if (fldName.startsWith(cccat))
							fldName = fldName.substring(cccat.length());
					}
					fldName = fldName.substring(0, 1).toLowerCase() + fldName.substring(1);
					
					String propName = prop_ann.inFileName().isEmpty() ? fldName : prop_ann.inFileName();
					Property cProp = null;
					
					Property.Type forgeTyp = getForgeType(type);
					
					Renamed oldNames = fld.getAnnotation(Renamed.class);
					if (oldNames != null)
						for (int i=0; i<oldNames.value().length; i++) {
							String onm = oldNames.value()[i];
							
							if (onm.endsWith(".")) onm += propName;
							String[] path = onm.split("\\"+Configuration.CATEGORY_SPLITTER);
							onm = path[path.length-1];
							ConfigCategory cat = fcfg.getCategory(path.length>1 ? StringUtils.join(Arrays.copyOf(path, path.length-1), ".") : prop_ann.category());
							if (cat != null && cat.containsKey(onm)) {
								cProp = cat.get(onm);
								cat.remove(onm);
							}
						}
					
					if (type.isArray()) {
						cProp = fcfg.get(prop_ann.category(), propName, cProp!=null ? cProp.getStringList() : (String[])fld.get(modInst), null, forgeTyp);
						switch (forgeTyp) {
						case INTEGER:
							fld.set(modInst, cProp.getIntList());
							break;
						case BOOLEAN:
							fld.set(modInst, cProp.getBooleanList());
							break;
						case DOUBLE:
							fld.set(modInst, cProp.getDoubleList());
							break;
						case STRING:
							fld.set(modInst, cProp.getStringList());
							break;
						}
					} else {
						cProp = fcfg.get(prop_ann.category(), propName, cProp!=null ? cProp.getString() : fld.get(modInst).toString(), null, forgeTyp);
						switch (forgeTyp) {
						case INTEGER:
							fld.set(modInst, cProp.getInt(fld.getInt(this)));
							break;
						case BOOLEAN:
							fld.set(modInst, cProp.getBoolean(fld.getBoolean(this)));
							break;
						case DOUBLE:
							fld.set(modInst, cProp.getDouble(fld.getDouble(this)));
							break;
						case STRING:
							fld.set(modInst, cProp.getString());
							break;
						}
					}
										
					if (cProp != null && !prop_ann.comment().isEmpty())
						cProp.comment = prop_ann.comment();
				}
			}
		}
	}
	
	public Config load() {
		try {
			loadModule(this, this.getClass());
			for (Object mod : mods)
				if (mod instanceof Class)
					loadModule(null, (Class)mod);
				else loadModule(mod, mod.getClass());
		} catch(Exception e) {
			CoreLogger.log(Level.SEVERE, "Failed to load the configuration properly", e);
		} finally {
			fcfg.save();
		}
		return this;
	}
	
	protected void saveModule(Object modInst, Class modCls) throws IllegalAccessException {
		for (Field fld : modInst.getClass().getFields()){
			Prop ann = fld.getAnnotation(Prop.class);
			if ((modInst != null || Modifier.isStatic(fld.getModifiers())) && ann != null){
				Class type = fld.getType();
				String fldName = fld.getName();
				
				String propName = ann.inFileName().isEmpty() ? fldName : ann.inFileName();
				Property cProp = null;
				
				Property.Type forgeTyp = getForgeType(type);
				
				if (type.isArray()) {
					cProp = fcfg.get(ann.category(), propName, (String[])fld.get(modInst), null, forgeTyp);
					cProp.set((String[])fld.get(modInst));
				} else {
					cProp = fcfg.get(ann.category(), propName, fld.get(modInst).toString(), null, forgeTyp);
					cProp.set((String)fld.get(modInst));
				}
									
				if (cProp != null && !ann.comment().isEmpty())
					cProp.comment = ann.comment();
			}
		}
	}
	
	public void save() {
		try {
			saveModule(this, this.getClass());
			for (Object mod : mods)
				if (mod instanceof Class)
					saveModule(null, (Class)mod);
				else saveModule(mod, mod.getClass());
			
		} catch(Exception e) {
			CoreLogger.log(Level.SEVERE, "Failed to save all of the configuration", e);
		} finally {
			fcfg.save();
		}
	}
}
