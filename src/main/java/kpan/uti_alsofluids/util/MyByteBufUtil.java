package kpan.uti_alsofluids.util;

import java.nio.charset.StandardCharsets;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MyByteBufUtil {

	public static void writeVarInt(ByteBuf buf, int value) {
		ByteBufUtils.writeVarInt(buf, value, 5);
	}

	public static void writeVarIntAsShort(ByteBuf buf, int value) {
		//15bit目が延長フラグ
		while (true) {
			if ((value & ~0x7fff) == 0) {
				buf.writeShort(value);
				break;
			} else {
				buf.writeShort(value & 0x7fff | 0x8000);
				value >>>= 15;
			}
		}
	}

	public static void writeBlockPos(ByteBuf buf, BlockPos pos) {
		buf.writeLong(pos.toLong());
	}

	public static void writeVec3d(ByteBuf buf, Vec3d vec) {
		buf.writeDouble(vec.x);
		buf.writeDouble(vec.y);
		buf.writeDouble(vec.z);
	}

	public static void writeTextComponent(ByteBuf buf, ITextComponent component) {
		writeString(buf, ITextComponent.Serializer.componentToJson(component));
	}

	/**
	 * Unlimited version of ByteBufUtils.writeUTF8String
	 */
	public static void writeString(ByteBuf buf, String string) {
		byte[] utf8Bytes = string.getBytes(StandardCharsets.UTF_8);
		writeVarInt(buf, utf8Bytes.length);
		buf.writeBytes(utf8Bytes);
	}

	public static <E extends Enum<E>> void writeEnum(ByteBuf buf, Enum<E> enum1) {
		writeVarInt(buf, enum1.ordinal());
	}

	public static void writeTagCompound(ByteBuf buf, NBTTagCompound compound) {
		ByteBufUtils.writeTag(buf, compound);
	}

	public static void writeFluidStack(ByteBuf buf, FluidStack fluidStack) {
		writeString(buf, FluidRegistry.getFluidName(fluidStack.getFluid()));
		buf.writeInt(fluidStack.amount);
		if (fluidStack.tag != null) {
			buf.writeBoolean(true);
			writeTagCompound(buf, fluidStack.tag);
		} else {
			buf.writeBoolean(false);
		}
	}

	public static int readVarInt(ByteBuf buf) {
		return ByteBufUtils.readVarInt(buf, 5);
	}

	public static int readVarIntAsShort(ByteBuf buf) {
		int value = 0;
		for (int exp = 0;; exp++) {
			short v = buf.readShort();
			value |= (v & 0x7fff) << exp * 15;
			if ((v & 0x8000) == 0)
				break;
		}
		return value;
	}

	public static BlockPos readBlockPos(ByteBuf buf) {
		return BlockPos.fromLong(buf.readLong());
	}

	public static Vec3d readVec3d(ByteBuf buf) {
		return new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	public static ITextComponent readTextComponent(ByteBuf buf) {
		return ITextComponent.Serializer.jsonToComponent(readString(buf));
	}

	/**
	 * Unlimited version of ByteBufUtils.readUTF8String
	 */
	public static String readString(ByteBuf buf) {
		int len = readVarInt(buf);
		String str = buf.toString(buf.readerIndex(), len, StandardCharsets.UTF_8);
		buf.readerIndex(buf.readerIndex() + len);
		return str;
	}

	public static <E extends Enum<E>> E readEnum(ByteBuf buf, Class<E> clazz) {
		return clazz.getEnumConstants()[readVarInt(buf)];
	}

	@Nullable
	public static NBTTagCompound readTagCompound(ByteBuf buf) {
		return ByteBufUtils.readTag(buf);
	}

	@Nullable
	public static FluidStack readFluidStack(ByteBuf buf) {
		String fluidName = readString(buf);
		Fluid fluid = FluidRegistry.getFluid(fluidName);
		if (fluid == null)
			return null;
		FluidStack stack = new FluidStack(FluidRegistry.getFluid(fluidName), buf.readInt());
		if (buf.readBoolean()) {
			stack.tag = readTagCompound(buf);
		}
		return stack;
	}

}
