package ml.core.data;

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

public class NBTUtils {

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
		(value instanceof Float)	{return new NBTTagFloat(name,		(Float)value);}		else if
		(value instanceof Integer)	{return new NBTTagInt(name,			(Integer)value);}	else if
		(value instanceof Short)	{return new NBTTagShort(name,		(Short)value);}		else if
		(value instanceof Long)		{return new NBTTagLong(name,		(Long)value);}		else if
		(value instanceof Double)	{return new NBTTagDouble(name,		(Double)value);}	else if
		(value instanceof Byte)		{return new NBTTagByte(name,		(Byte)value);}		else if
		(value instanceof String)	{return new NBTTagString(name,		(String)value);}	else if
		(value instanceof int[])	{return new NBTTagIntArray(name,	(int[])value);}		else if
		(value instanceof byte[])	{return new NBTTagByteArray(name,	(byte[])value);}
		return null;
	}
	
	public static <T> T getTagValue(NBTTagCompound parent, T defaultVal, String...tagPath) {
		try {
			String tagName = null;
			for (int i=0; i<tagPath.length; i++) {
				tagName = tagPath[i];
				if (!parent.hasKey(tagName)) return defaultVal;
				if (i==tagPath.length-1) break;
				parent = parent.getCompoundTag(tagName);
			}
			return NBTUtils.getTagValue(parent.getTag(tagName));
		} catch (Exception ex) {
			return defaultVal;
		}
	}
	
	public static void setTag(NBTTagCompound parent, Object value, String...tagPath) {
		String tagName = null;
		for (int i=0; i<tagPath.length; i++){
			tagName = tagPath[i];
			if (i == tagPath.length-1) break;
			if (!parent.hasKey(tagName))
				parent.setCompoundTag(tagName, new NBTTagCompound(tagName));
			parent = parent.getCompoundTag(tagName);
		}
		parent.setTag(tagName, createTag(tagName, value));
	}
	
	public static boolean hasTagAt(NBTTagCompound parent, String...tagPath) {
		String tagName = null;
		for (int i=0; i<tagPath.length; i++) {
			tagName = tagPath[i];
			if (!parent.hasKey(tagName)) return false;
			parent = parent.getCompoundTag(tagName);
		}
		return true;
	}
}
