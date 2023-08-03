package kpan.uti_alsofluids.asm.core;

import javax.annotation.Nullable;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

public class AsmUtil {
	public static final int ASM_VER = Opcodes.ASM5;

	public static boolean isDeobfEnvironment() { return FMLLaunchHandler.isDeobfuscatedEnvironment(); }

	//MethodDescにも使用可能
	public static String obfDesc(String deobfDesc) {
		StringBuilder sb = new StringBuilder(deobfDesc.length());
		boolean object = false;
		StringBuilder sb_object = new StringBuilder();
		for (int i = 0; i < deobfDesc.length(); i++) {
			char c = deobfDesc.charAt(i);
			if (object) {
				if (c == ';') {
					String name = MyAsmNameRemapper.getClassObfName(sb_object.toString());
					sb.append('L');
					sb.append(name);
					sb.append(';');
					object = false;
					sb_object.setLength(0);
				} else {
					sb_object.append(c);
				}
			} else {
				if (c == 'L') {
					object = true;
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	//MethodDescにも使用可能
	public static String deobfDesc(String obfDesc) {
		StringBuilder sb = new StringBuilder(obfDesc.length());
		boolean object = false;
		StringBuilder sb_object = new StringBuilder();
		for (int i = 0; i < obfDesc.length(); i++) {
			char c = obfDesc.charAt(i);
			if (object) {
				if (c == ';') {
					String name = MyAsmNameRemapper.getClassDeobfName(sb_object.toString());
					sb.append('L');
					sb.append(name);
					sb.append(';');
					object = false;
					sb_object.setLength(0);
				} else {
					sb_object.append(c);
				}
			} else {
				if (c == 'L') {
					object = true;
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	public static String toMethodDesc(Object returnType, Object... rawDesc) {
		StringBuilder sb = new StringBuilder("(");
		for (Object o : rawDesc) {
			sb.append(toDesc(o));
		}
		sb.append(')');
		sb.append(toDesc(returnType));
		return sb.toString();
	}

	public static String toDesc(Object raw) {
		if (raw instanceof Class) {
			Class<?> clazz = (Class<?>) raw;
			return Type.getDescriptor(clazz);
		} else if (raw instanceof String) {
			String desc = (String) raw;
			int arr_dim = 0;
			while (arr_dim < desc.length() - 1) {
				if (desc.charAt(arr_dim) != '[')
					break;
				arr_dim++;
			}
			String arr_str = arr_dim > 0 ? StringUtils.repeat('[', arr_dim) : "";
			desc = desc.substring(arr_dim);
			if (desc.equals(AsmTypes.VOID) || desc.equals(AsmTypes.BOOL) || desc.equals(AsmTypes.CHAR) || desc.equals(AsmTypes.BYTE) || desc.equals(AsmTypes.SHORT) || desc.equals(AsmTypes.INT)
					|| desc.equals(AsmTypes.LONG) || desc.equals(AsmTypes.FLOAT) || desc.equals(AsmTypes.DOUBLE))
				return arr_str + desc;
			desc = desc.replace('.', '/');
			desc = desc.matches("L.+;") ? desc : "L" + desc + ";";//全体とマッチ
			return arr_str + desc;
		} else if (raw instanceof Object[]) {
			StringBuilder sb = new StringBuilder();
			for (Object o : (Object[]) raw) {
				sb.append(toDesc(o));
			}
			return sb.toString();
		} else {
			throw new IllegalArgumentException();
		}
	}

	public static MethodVisitor traceMethod(MethodVisitor mv, @Nullable String methodName) {
		Textifier p = new MyTextifier(methodName);
		TraceMethodVisitor tracemv = new TraceMethodVisitor(mv, p);
		return tracemv;
	}

	//MethodDescにも使用可能
	public static String runtimeDesc(String deobfDesc) {
		if (isDeobfEnvironment())
			return deobfDesc;
		else
			return obfDesc(deobfDesc);
	}

	public static String composeRuntimeMethodDesc(Object deobfReturnType, Object... deobfParams) {
		return runtimeDesc(toMethodDesc(deobfReturnType, deobfParams));
	}

	public static String runtimeMethodGenerics(String deobfGenerics) {
		throw new NotImplementedException("TODO");//TODO
	}

	public static String[] runtimeExceptions(String[] deobfExceptions) {
		throw new NotImplementedException("TODO");//TODO
	}

}
