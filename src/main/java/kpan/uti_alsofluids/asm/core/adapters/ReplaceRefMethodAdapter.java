package kpan.uti_alsofluids.asm.core.adapters;

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
			mv.visitVarInsn(AsmUtil.loadOpcode(params[i]), i + offset);
		}

		//invoke
		if (is_static)
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, refClass, originalName, runtimeDesc, false);
		else
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, refClass, originalName, AsmUtil.runtimeDesc(AsmUtil.toMethodDesc(returnType, runtimeClassForRefMethodParam, params)), false);

		//return
		mv.visitInsn(AsmUtil.returnOpcode(returnType));
	}

}
