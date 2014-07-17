package ml.core.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

/**
 * A powerful way of reading NBT data.<br/>
 * NB: Things are not type safe. If you request a string from an IntegerTag, you will get a ClassCastException (Call the toString() function Java!) >:(
 * @author Matchlighter
 */
public class NBTUtils {

	@SuppressWarnings("unchecked")
	public static <T> T getTagValue(NBTBase tag) {
		if
		(tag instanceof NBTTagFloat)		{return (T)(Object)((NBTTagFloat)tag).data;}	else if
		(tag instanceof NBTTagInt)			{return (T)(Object)((NBTTagInt)tag).data;}		else if
		(tag instanceof NBTTagShort)		{return (T)(Object)((NBTTagShort)tag).data;}	else if
		(tag instanceof NBTTagLong)			{return (T)(Object)((NBTTagLong)tag).data;}		else if
		(tag instanceof NBTTagDouble)		{return (T)(Object)((NBTTagDouble)tag).data;}	else if
		(tag instanceof NBTTagByte)			{return (T)(Object)((NBTTagByte)tag).data;}		else if
		
		(tag instanceof NBTTagString)		{return (T)((NBTTagString)tag).data;}			else if
		(tag instanceof NBTTagIntArray)		{return (T)((NBTTagIntArray)tag).intArray;}		else if
		(tag instanceof NBTTagByteArray)	{return (T)((NBTTagByteArray)tag).byteArray;}	else if
		(tag instanceof NBTTagCompound)		{return (T)tag;}
		return null;
	}
	
	public static NBTBase createTag(String name, Object value) {
		if
		(value instanceof Float)	{return new NBTTagFloat(	(Float)value);}		else if
		(value instanceof Integer)	{return new NBTTagInt(		(Integer)value);}	else if
		(value instanceof Short)	{return new NBTTagShort(	(Short)value);}		else if
		(value instanceof Long)		{return new NBTTagLong(		(Long)value);}		else if
		(value instanceof Double)	{return new NBTTagDouble(	(Double)value);}	else if
		(value instanceof Byte)		{return new NBTTagByte(		(Byte)value);}		else if
		(value instanceof String)	{return new NBTTagString(	(String)value);}	else if
		(value instanceof int[])	{return new NBTTagIntArray(	(int[])value);}		else if
		(value instanceof byte[])	{return new NBTTagByteArray((byte[])value);}	else if
		(value instanceof NBTBase)	{return (NBTBase)value;}
		return null;
	}
	
	private static String[] splitPath(String... path) {
		List<String> spath = new ArrayList<String>();
		for (String elm : path) {
			spath.addAll(Arrays.asList(elm.split("\\.")));
		}
		return (String[])spath.toArray(new String[]{});
	}
	
	/**
	 * You CAN get a string from any NBTTag type with this method - if the defaultVal is a String, toString() will be called on the returning Object.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getTagValue(NBTTagCompound parent, T defaultVal, String...tagPath) {
		try {
			tagPath = splitPath(tagPath);
			String tagName = null;
			for (int i=0; i<tagPath.length; i++) {
				tagName = tagPath[i];
				if (!parent.hasKey(tagName)) return defaultVal;
				if (i==tagPath.length-1) break;
				parent = parent.getCompoundTag(tagName);
			}
			Object value = NBTUtils.getTagValue(parent.getTag(tagName));
			if (defaultVal instanceof String)
				return (T)value.toString();
			if (defaultVal instanceof Boolean)
				return (T)(Boolean)((Byte)value!=0);
			
			return (T)value;
		} catch (Exception ex) {
			return defaultVal;
		}
	}
	
	public static void setTag(NBTTagCompound parent, Object value, String...tagPath) {
		tagPath = splitPath(tagPath);
		String tagName = null;
		for (int i=0; i<tagPath.length; i++){
			tagName = tagPath[i];
			if (i == tagPath.length-1) break;
			if (!parent.hasKey(tagName))
				parent.setTag(tagName, new NBTTagCompound());
			parent = parent.getCompoundTag(tagName);
		}
		NBTBase ntag = createTag(tagName, value);
		if (ntag != null)
			parent.setTag(tagName, ntag);
	}
	
	public static boolean hasTagAt(NBTTagCompound parent, String...tagPath) {
		tagPath = splitPath(tagPath);
		String tagName = null;
		for (int i=0; i<tagPath.length; i++) {
			tagName = tagPath[i];
			if (!parent.hasKey(tagName)) return false;
			if (i == tagPath.length-1) return true;
			if (!(parent.getTag(tagName) instanceof NBTTagCompound)) return false;
			parent = parent.getCompoundTag(tagName);
		}
		return true;
	}
}
