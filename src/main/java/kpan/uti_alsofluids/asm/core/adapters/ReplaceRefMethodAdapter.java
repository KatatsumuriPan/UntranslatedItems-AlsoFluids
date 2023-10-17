package kpan.uti_alsofluids.asm.core.adapters;

import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.AsmTypes.MethodDesc;
import kpan.uti_alsofluids.asm.core.AsmUtil;
import kpan.uti_alsofluids.asm.core.MyAsmNameRemapper;
import kpan.uti_alsofluids.asm.core.MyAsmNameRemapper.MethodRemap;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ReplaceRefMethodAdapter extends ReplaceMethodAdapter {

	private final String runtimeClassForRefMethodParam;
	private final String originalName;
	private final String refClass;
	private final String returnType;
	private final String[] params;

	public ReplaceRefMethodAdapter(ClassVisitor cv, String refClass, MethodRemap method) {
		super(cv, method);
		originalName = runtimeName;
		runtimeClassForRefMethodParam = MyAsmNameRemapper.runtimeClass(method.deobfOwner);
		this.refClass = refClass;
		MethodDesc md = MethodDesc.fromMethodDesc(AsmUtil.runtimeDesc(method.deobfMethodDesc));
		returnType = md.returnDesc;
		params = md.paramsDesc;
	}
	public ReplaceRefMethodAdapter(ClassVisitor cv, String refClass, String runtimeClassForRefMethodParam, String runtimeMethodName, String runtimeDesc) {
		super(cv, runtimeMethodName, runtimeDesc);
		originalName = runtimeName;
		this.runtimeClassForRefMethodParam = runtimeClassForRefMethodParam;
		this.refClass = refClass;
		MethodDesc md = MethodDesc.fromMethodDesc(runtimeDesc);
		returnType = md.returnDesc;
		params = md.paramsDesc;
	}

	@Override
	protected void methodBody(MethodVisitor mv) {

		//java7との互換性のため、文字列switchを使用していない
		boolean is_static = (access & Opcodes.ACC_STATIC) != 0;
		int offset = 0;
		//this
		if (!is_static) {
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			offset = 1;
		}
		//params
		for (int i = 0; i < params.length; i++) {
			if (params[i].equals(AsmTypes.BOOL) || params[i].equals(AsmTypes.CHAR) || params[i].equals(AsmTypes.BYTE) || params[i].equals(AsmTypes.SHORT) || params[i].equals(AsmTypes.INT))
				mv.visitVarInsn(Opcodes.ILOAD, i + offset);
			else if (params[i].equals(AsmTypes.LONG))
				mv.visitVarInsn(Opcodes.LLOAD, i + offset);
			else if (params[i].equals(AsmTypes.FLOAT))
				mv.visitVarInsn(Opcodes.FLOAD, i + offset);
			else if (params[i].equals(AsmTypes.DOUBLE))
				mv.visitVarInsn(Opcodes.DLOAD, i + offset);
			else
				mv.visitVarInsn(Opcodes.ALOAD, i + offset);
		}

		//invoke
		if (is_static)
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, refClass, originalName, runtimeDesc, false);
		else
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, refClass, originalName, AsmUtil.runtimeDesc(AsmUtil.toMethodDesc(returnType, runtimeClassForRefMethodParam, params)), false);

		//return
		if (returnType.equals(AsmTypes.VOID))
			mv.visitInsn(Opcodes.RETURN);
		else if (returnType.equals(AsmTypes.BOOL) || returnType.equals(AsmTypes.CHAR) || returnType.equals(AsmTypes.BYTE) || returnType.equals(AsmTypes.SHORT) || returnType.equals(AsmTypes.INT))
			mv.visitInsn(Opcodes.IRETURN);
		else if (returnType.equals(AsmTypes.LONG))
			mv.visitInsn(Opcodes.LRETURN);
		else if (returnType.equals(AsmTypes.FLOAT))
			mv.visitInsn(Opcodes.FRETURN);
		else if (returnType.equals(AsmTypes.DOUBLE))
			mv.visitInsn(Opcodes.DRETURN);
		else
			mv.visitInsn(Opcodes.ARETURN);
	}

}
