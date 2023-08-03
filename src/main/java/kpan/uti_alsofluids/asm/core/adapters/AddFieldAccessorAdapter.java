package kpan.uti_alsofluids.asm.core.adapters;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.AsmUtil;
import kpan.uti_alsofluids.asm.core.MyAsmNameRemapper;
import kpan.uti_alsofluids.asm.core.MyAsmNameRemapper.FieldRemap;

public class AddFieldAccessorAdapter extends MyClassVisitor {

	protected final String runtimeInstanceClassName;
	protected final String fieldNameForAccessorName;
	protected final String runtimeFieldName;
	protected final String runtimeDesc;
	private String runtimeGenerics;
	private boolean found = false;

	public AddFieldAccessorAdapter(ClassVisitor cv, String accessorClassName, FieldRemap field) {
		super(cv, accessorClassName);
		runtimeInstanceClassName = MyAsmNameRemapper.runtimeClass(field.deobfOwner);
		fieldNameForAccessorName = field.mcpFieldName;
		runtimeFieldName = MyAsmNameRemapper.runtimeField(field);
		runtimeDesc = AsmUtil.runtimeDesc(field.deobfDesc);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if (name.equals("get_" + fieldNameForAccessorName) || name.equals("set_" + fieldNameForAccessorName)) {
			if (access != (Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC))
				throw new RuntimeException("Invalid access value:" + name);
			runtimeGenerics = signature;
			found = true;
			return null;//既存のを削除、visitEndで追加
		} else
			return super.visitMethod(access, name, desc, signature, exceptions);
	}

	@Override
	public void visitEnd() {
		if (found) {
			String signature = runtimeGenerics;
			MethodVisitor get_mv = super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "get_" + fieldNameForAccessorName, AsmUtil.toMethodDesc(runtimeDesc, runtimeInstanceClassName), signature,
					null);
			if (get_mv != null) {
				get_mv.visitCode();
				get_mv.visitVarInsn(Opcodes.ALOAD, 0);
				get_mv.visitFieldInsn(Opcodes.GETFIELD, runtimeInstanceClassName, runtimeFieldName, runtimeDesc);
				get_mv.visitInsn(returnOpcode(runtimeDesc));
				get_mv.visitMaxs(0, 0);//引数は無視され、再計算される(Write時に再計算されるのでtrace時点では0,0のまま)
				get_mv.visitEnd();
			}
			MethodVisitor set_mv = super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "set_" + fieldNameForAccessorName,
					AsmUtil.toMethodDesc(AsmTypes.VOID, runtimeInstanceClassName, runtimeDesc),
					signature,
					null);
			if (set_mv != null) {
				set_mv.visitCode();
				set_mv.visitVarInsn(Opcodes.ALOAD, 0);
				set_mv.visitVarInsn(loadOpcode(runtimeDesc), 1);
				set_mv.visitFieldInsn(Opcodes.PUTFIELD, runtimeInstanceClassName, runtimeFieldName, runtimeDesc);
				set_mv.visitInsn(Opcodes.RETURN);
				set_mv.visitMaxs(0, 0);//引数は無視され、再計算される(Write時に再計算されるのでtrace時点では0,0のまま)
				set_mv.visitEnd();
			}
			success();
		}
		super.visitEnd();
	}

	protected int loadOpcode(String type) {
		if (type.equals(AsmTypes.BOOL) || type.equals(AsmTypes.CHAR) || type.equals(AsmTypes.BYTE) || type.equals(AsmTypes.SHORT) || type.equals(AsmTypes.INT))
			return Opcodes.ILOAD;
		else if (type.equals(AsmTypes.LONG))
			return Opcodes.LLOAD;
		else if (type.equals(AsmTypes.FLOAT))
			return Opcodes.FLOAD;
		else if (type.equals(AsmTypes.DOUBLE))
			return Opcodes.DLOAD;
		else
			return Opcodes.ALOAD;
	}

	protected static int returnOpcode(String type) {
		if (type.equals(AsmTypes.VOID))
			return Opcodes.RETURN;
		else if (type.equals(AsmTypes.BOOL) || type.equals(AsmTypes.CHAR) || type.equals(AsmTypes.BYTE) || type.equals(AsmTypes.SHORT) || type.equals(AsmTypes.INT))
			return Opcodes.IRETURN;
		else if (type.equals(AsmTypes.LONG))
			return Opcodes.LRETURN;
		else if (type.equals(AsmTypes.FLOAT))
			return Opcodes.FRETURN;
		else if (type.equals(AsmTypes.DOUBLE))
			return Opcodes.DRETURN;
		else
			return Opcodes.ARETURN;
	}
}
