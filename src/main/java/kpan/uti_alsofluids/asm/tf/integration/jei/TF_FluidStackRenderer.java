package kpan.uti_alsofluids.asm.tf.integration.jei;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.AsmUtil;
import kpan.uti_alsofluids.asm.core.adapters.Instructions;
import kpan.uti_alsofluids.asm.core.adapters.Instructions.OpcodeVar;
import kpan.uti_alsofluids.asm.core.adapters.MyClassVisitor;
import kpan.uti_alsofluids.asm.core.adapters.ReplaceInstructionsAdapter;

public class TF_FluidStackRenderer {

	private static final String TARGET = "mezz.jei.plugins.vanilla.ingredients.fluid.FluidStackRenderer";
	private static final String HOOK = AsmTypes.HOOK + "integration/jei/" + "HK_" + "FluidStackRenderer";

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (TARGET.equals(className)) {
			ClassVisitor newcv = new MyClassVisitor(cv, className) {
				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
					if ("getTooltip".equals(name)) {
						if (desc.equals(AsmUtil.toMethodDesc(AsmTypes.LIST, AsmTypes.MINECRAFT, AsmTypes.FLUIDSTACK, "Lnet/minecraft/client/util/ITooltipFlag;"))) {
							mv = new ReplaceInstructionsAdapter(mv, name,
									Instructions.create()
											.varInsn(OpcodeVar.ALOAD, 4)
											.varInsn(OpcodeVar.ALOAD, 6)
											.invokeInterface("java/util/List", "add", "(Ljava/lang/Object;)Z")
											.insn(Opcodes.POP),
									Instructions.create()
											.varInsn(OpcodeVar.ALOAD, 4)
											.varInsn(OpcodeVar.ALOAD, 2)
											.invokeStatic(HOOK, "addBothNames", AsmUtil.toMethodDesc(AsmTypes.VOID, AsmTypes.LIST, AsmTypes.FLUIDSTACK)));
							success();
						}
					}
					return mv;
				}
			};
			return newcv;
		}
		return cv;
	}
}
