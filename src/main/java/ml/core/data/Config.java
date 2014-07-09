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
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public abstract class Config {

	/**
	 * Property names will be determined by the field name if {@link Prop#inFileName()} is not set.<br/><br/>
	 * 
	 * If you prefix a field name with the camelCased category name and an '_', the prefix will be removed when saved to file.<br/>
	 * <b>Ex:</b> "generalChild_property" will be saved as simply "property" if it is in the category "general.child"<br/>
	 * In this scenario, "child_property" will also become just "property" in the config file; you can go as deep or shallow into the category ancestory as you want.
	 * 
	 * @author Matchlighter
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface Prop {
		public String category() default ""; //Configuration.CATEGORY_GENERAL;
		public String comment() default "";
		public String inFileName() default "";
		public int version() default 0;
		
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
	 * Thanks to monoxide0184 for the idea of adding category comments
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface Category {
		public String value() default ""; //Configuration.CATEGORY_GENERAL;
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
	
	public static Property.Type getForgeType(Class<?> cls) {
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
	
	private String getPropertyName(Field fld, Prop prop_ann, String category) {
		String fldName = fld.getName();
		
		// Strip Category Prefix
		String[] cats = category.split("\\.");
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
		
		return prop_ann.inFileName().isEmpty() ? fldName : prop_ann.inFileName();
	}
	
	private String testGetCategory(Field fld, String last) {
		Category cat_ann = fld.getAnnotation(Category.class);
		if (cat_ann != null) {
			String catn = cat_ann.value();
			String com = cat_ann.comment().isEmpty() ? null : cat_ann.comment();
			if (catn.isEmpty()) {
				catn = fld.getName();
			}
			
			fcfg.addCustomCategoryComment(catn, com);
			return catn;
		}
		return last;
	}
	
	protected void loadModule(Object modInst, Class<?> modCls) throws IllegalAccessException {
		String lastCategory = Configuration.CATEGORY_GENERAL;
		
		for (Field fld : modCls.getFields()){
			if (modInst != null || Modifier.isStatic(fld.getModifiers())) {
				lastCategory = testGetCategory(fld, lastCategory);
				
				Prop prop_ann = fld.getAnnotation(Prop.class);
				if (prop_ann != null){
					
					String propCategory = prop_ann.category().isEmpty() ? lastCategory : prop_ann.category();
					String propName = getPropertyName(fld, prop_ann, propCategory);
					
					Property cProp = null;
					
					// Find Old Names. Names at the end of the list take priority
					Renamed oldNames = fld.getAnnotation(Renamed.class);
					if (oldNames != null) {
						for (int i=oldNames.value().length-1; i>=0; i--) {
							String oldName = oldNames.value()[i];
							
							if (oldName.endsWith(Configuration.CATEGORY_SPLITTER)) oldName += propName; // Category changed, but same name
							String[] oldCatPath = oldName.split("\\"+Configuration.CATEGORY_SPLITTER);
							oldName = oldCatPath[oldCatPath.length-1];
							ConfigCategory oldCategory = fcfg.getCategory(oldCatPath.length>1 ? StringUtils.join(Arrays.copyOf(oldCatPath, oldCatPath.length-1), Configuration.CATEGORY_SPLITTER) : propCategory);
							if (oldCategory != null && oldCategory.containsKey(oldName)) {
								cProp = oldCategory.get(oldName);
								oldCategory.remove(oldName);
								break;
							}
						}
					}
					
					Class<?> type = fld.getType();
					Property.Type forgeTyp = getForgeType(type);
					Object fldValue = null;
					if (type.isArray()) {
						switch (forgeTyp) {
						case INTEGER:
							cProp = fcfg.get(propCategory, propName, cProp!=null ? cProp.getIntList() : (int[])fld.get(modInst), null);
							fldValue = cProp.getIntList();
							break;
						case BOOLEAN:
							cProp = fcfg.get(propCategory, propName, cProp!=null ? cProp.getBooleanList() : (boolean[])fld.get(modInst), null);
							fldValue = cProp.getBooleanList();
							break;
						case DOUBLE:
							cProp = fcfg.get(propCategory, propName, cProp!=null ? cProp.getDoubleList() : (double[])fld.get(modInst), null);
							fldValue = cProp.getDoubleList();
							break;
						case STRING:
							cProp = fcfg.get(propCategory, propName, cProp!=null ? cProp.getStringList() : (String[])fld.get(modInst), null);
							fldValue = cProp.getStringList();
							break;
						}
					} else {
						cProp = fcfg.get(propCategory, propName, cProp!=null ? cProp.getString() : fld.get(modInst).toString(), null, forgeTyp);
						switch (forgeTyp) {
						case INTEGER:
							fldValue = cProp.getInt(fld.getInt(this));
							break;
						case BOOLEAN:
							fldValue = cProp.getBoolean(fld.getBoolean(this));
							break;
						case DOUBLE:
							fldValue = cProp.getDouble(fld.getDouble(this));
							break;
						case STRING:
							fldValue = cProp.getString();
							break;
						}
					}
					
					if (prop_ann.version() > 0) {
						String propVName = propName+"_version";
						Property oldV = fcfg.get(propCategory, propVName, 0); 
						if (oldV.getInt() < prop_ann.version()) {
							fldValue = updateProperty(fld, fldValue, oldV.getInt(), prop_ann.version());
							oldV.set(prop_ann.version());
						}
					}
					
					fld.set(modInst, fldValue);
					
					if (cProp != null && !prop_ann.comment().isEmpty())
						cProp.comment = prop_ann.comment();
				}
			}
		}
	}
	
	protected Object updateProperty(Field fld, Object readVal, int oldV, int newV) {
		System.out.println("Update: "+fld.getName());
		return readVal;
	}
	
	/**
	 * Populates the Config's Fields with data from the config file, using each Field's value as the default
	 * @return
	 */
	public Config load() {
		try {
			loadModule(this, this.getClass());
			for (Object mod : mods)
				if (mod instanceof Class)
					loadModule(null, (Class<?>)mod);
				else loadModule(mod, mod.getClass());
		} catch(Exception e) {
			CoreLogger.log(Level.SEVERE, "Failed to load the configuration properly", e);
		} finally {
			fcfg.save();
		}
		return this;
	}
	
	protected void saveModule(Object modInst, Class<?> modCls) throws IllegalAccessException {
		String lastCategory = Configuration.CATEGORY_GENERAL;
		
		for (Field fld : modCls.getFields()){
			if (modInst != null || Modifier.isStatic(fld.getModifiers())) {
				lastCategory = testGetCategory(fld, lastCategory);
				
				Prop prop_ann = fld.getAnnotation(Prop.class);
				if (prop_ann != null){
					
					String propCategory = prop_ann.category().isEmpty() ? lastCategory : prop_ann.category();
					String propName = getPropertyName(fld, prop_ann, propCategory);
					
					Property cProp = null;
					
					Class<?> type = fld.getType();
					Property.Type forgeTyp = getForgeType(type);
					if (type.isArray()) {
						cProp = fcfg.get(propCategory, propName, (String[])fld.get(modInst), null, forgeTyp);
						cProp.set((String[])fld.get(modInst));
					} else {
						cProp = fcfg.get(propCategory, propName, fld.get(modInst).toString(), null, forgeTyp);
						cProp.set((String)fld.get(modInst));
					}
										
					if (cProp != null && !prop_ann.comment().isEmpty())
						cProp.comment = prop_ann.comment();
				}
			}
		}
	}
	
	/**
	 * Writes the content of the Config's Fields to the config file.
	 */
	public void save() {
		try {
			saveModule(this, this.getClass());
			for (Object mod : mods)
				if (mod instanceof Class)
					saveModule(null, (Class<?>)mod);
				else saveModule(mod, mod.getClass());
			
		} catch(Exception e) {
			CoreLogger.log(Level.SEVERE, "Failed to save all of the configuration", e);
		} finally {
			fcfg.save();
		}
	}
}
