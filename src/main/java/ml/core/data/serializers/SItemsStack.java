package ml.core.data.serializers;

import java.io.DataOutputStream;
import java.io.IOException;

import ml.core.data.IDataSerializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;

public class SItemsStack implements IDataSerializer {

	@Override
	public boolean handles(Class<?> clazz) {
		return clazz==ItemStack.class || ItemStack.class.isAssignableFrom(clazz);
	}

	@Override
	public Object deserialize(Class<?> clazz, ByteArrayDataInput dIn) throws IOException {
		ItemStack var1 = null;
		short var2 = dIn.readShort();

		if (var2 >= 0)
		{
			byte var3 = dIn.readByte();
			short var4 = dIn.readShort();
			var1 = new ItemStack(Item.getItemById(var2), var3, var4);
			var1.stackTagCompound = SNBTTagCompound.readNBTTagCompound(dIn);
		}

		return var1;
	}

	@Override
	public void serialize(Object obj, DataOutputStream dOut) throws IOException {
		ItemStack is = (ItemStack)obj;
		if (is == null) {
			dOut.writeShort(-1);
		} else {
			dOut.writeShort(Item.getIdFromItem(is.getItem()));
			dOut.writeByte(is.stackSize);
			dOut.writeShort(is.getItemDamage());
			NBTTagCompound var2 = null;

			if (is.getItem().isDamageable() || is.getItem().getShareTag()) {
				var2 = is.stackTagCompound;
			}

			SNBTTagCompound.writeNBTTagCompound(dOut, var2);
		}
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
