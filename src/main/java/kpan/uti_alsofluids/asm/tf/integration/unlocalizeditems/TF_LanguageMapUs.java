package kpan.uti_alsofluids.asm.tf.integration.unlocalizeditems;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import kpan.uti_alsofluids.asm.core.adapters.InjectInstructionsAdapter;
import kpan.uti_alsofluids.asm.core.adapters.Instructions;
import kpan.uti_alsofluids.asm.core.adapters.Instructions.OpcodeJump;
import kpan.uti_alsofluids.asm.core.adapters.Instructions.OpcodeVar;
import kpan.uti_alsofluids.asm.core.adapters.MyClassVisitor;

public class TF_LanguageMapUs {

	private static final String TARGET = "bre.nti.LanguageMapUs";

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (TARGET.equals(className)) {
			ClassVisitor newcv = new MyClassVisitor(cv, className) {
				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
					if ("shouldDelocalize".equals(name)) {
						Label label_true = new Label();
						Label label_false = new Label();
						mv = InjectInstructionsAdapter.after(mv, name,
								Instructions.create()
										.insn(Opcodes.ICONST_1)
										.insn(Opcodes.IRETURN)
										.labelRep(),
								Instructions.create()
										.varInsn(OpcodeVar.ALOAD, 1)
										.ldcInsn("tile.water.name")
										.invokeVirtual("java/lang/Object", "equals", "(Ljava/lang/Object;)Z")
										.jumpInsn(OpcodeJump.IFNE, label_true)
										.varInsn(OpcodeVar.ALOAD, 1)
										.ldcInsn("tile.lava.name")
										.invokeVirtual("java/lang/Object", "equals", "(Ljava/lang/Object;)Z")
										.jumpInsn(OpcodeJump.IFEQ, label_false)
										.label(label_true)
										.insn(Opcodes.ICONST_1)
										.insn(Opcodes.IRETURN)
										.label(label_false));
						success();
					}
					return mv;
				}
			};
			return newcv;
		}
		return cv;
	}
}
