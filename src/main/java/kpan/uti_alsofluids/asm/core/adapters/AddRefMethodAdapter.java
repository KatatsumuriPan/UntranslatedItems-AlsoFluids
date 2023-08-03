package kpan.uti_alsofluids.asm.core.adapters;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.AsmTypes.MethodDesc;
import kpan.uti_alsofluids.asm.core.AsmUtil;
import kpan.uti_alsofluids.asm.core.MyAsmNameRemapper;
import kpan.uti_alsofluids.asm.core.MyAsmNameRemapper.MethodRemap;

public class AddRefMethodAdapter extends AddMethodAdapter {

	private final String runtimeTarget;
	private final String refMethodName;
	private final String runtimeRefMethodOwner;
	private final String runtimeReturnType;
	private final String[] runtimeParams;

	public AddRefMethodAdapter(ClassVisitor cv, String runtimeRefMethodOwner, int access, MethodRemap method) {
		super(cv, access, MyAsmNameRemapper.runtimeMethod(method), AsmUtil.runtimeDesc(method.deobfMethodDesc));
		refMethodName = method.mcpMethodName;
		runtimeTarget = MyAsmNameRemapper.runtimeClass(method.deobfOwner);
		this.runtimeRefMethodOwner = runtimeRefMethodOwner;
		MethodDesc md = MethodDesc.fromMethodDesc(AsmUtil.runtimeDesc(method.deobfMethodDesc));
		runtimeReturnType = md.returnDesc;
		runtimeParams = md.paramsDesc;
	}

	@Override
	protected void methodBody(MethodVisitor mv) {

		//java7との互換性のため、文字列switchを使用していない
		//this
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		//params
		for (int i = 0; i < runtimeParams.length; i++) {
			if (runtimeParams[i].equals(AsmTypes.BOOL) || runtimeParams[i].equals(AsmTypes.CHAR) || runtimeParams[i].equals(AsmTypes.BYTE) || runtimeParams[i].equals(AsmTypes.SHORT)
					|| runtimeParams[i].equals(AsmTypes.INT))
				mv.visitVarInsn(Opcodes.ILOAD, i + 1);
			else if (runtimeParams[i].equals(AsmTypes.LONG))
				mv.visitVarInsn(Opcodes.LLOAD, i + 1);
			else if (runtimeParams[i].equals(AsmTypes.FLOAT))
				mv.visitVarInsn(Opcodes.FLOAD, i + 1);
			else if (runtimeParams[i].equals(AsmTypes.DOUBLE))
				mv.visitVarInsn(Opcodes.DLOAD, i + 1);
			else
				mv.visitVarInsn(Opcodes.ALOAD, i + 1);
		}

		//invoke
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, runtimeRefMethodOwner, refMethodName, AsmUtil.toMethodDesc(runtimeReturnType, runtimeTarget, runtimeParams), false);

		//return
		if (runtimeReturnType.equals(AsmTypes.VOID))
			mv.visitInsn(Opcodes.RETURN);
		else if (runtimeReturnType.equals(AsmTypes.BOOL) || runtimeReturnType.equals(AsmTypes.CHAR) || runtimeReturnType.equals(AsmTypes.BYTE) || runtimeReturnType.equals(AsmTypes.SHORT)
				|| runtimeReturnType.equals(AsmTypes.INT))
			mv.visitInsn(Opcodes.IRETURN);
		else if (runtimeReturnType.equals(AsmTypes.LONG))
			mv.visitInsn(Opcodes.LRETURN);
		else if (runtimeReturnType.equals(AsmTypes.FLOAT))
			mv.visitInsn(Opcodes.FRETURN);
		else if (runtimeReturnType.equals(AsmTypes.DOUBLE))
			mv.visitInsn(Opcodes.DRETURN);
		else
			mv.visitInsn(Opcodes.ARETURN);
	}

}
