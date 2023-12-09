package kpan.uti_alsofluids.asm.tf.integration.gregtech;

import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.AsmUtil;
import kpan.uti_alsofluids.asm.core.adapters.Instructions;
import kpan.uti_alsofluids.asm.core.adapters.MyClassVisitor;
import kpan.uti_alsofluids.asm.core.adapters.ReplaceInstructionsAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TF_PhantomFluidWidget {

	private static final String TARGET = "gregtech.api.gui.widgets.PhantomFluidWidget";
	private static final String HOOK = AsmTypes.HOOK + "integration/gregtech/" + "HK_" + "PhantomFluidWidget";

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (TARGET.equals(className)) {
			ClassVisitor newcv = new MyClassVisitor(cv, className) {
				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
					if ("drawInForeground".equals(name)) {
						mv = new ReplaceInstructionsAdapter(mv, name,
								Instructions.create()
										.aload(4)
										.aload(3)
										.invokeInterface("java/util/List", "add", "(Ljava/lang/Object;)Z")
										.insn(Opcodes.POP),
								Instructions.create()
										.aload(4)
										.aload(0)
										.getField(TARGET, "lastFluidStack", AsmTypes.FLUIDSTACK)
										.invokeStatic(HOOK, "addBothNames", AsmUtil.toMethodDesc(AsmTypes.VOID, AsmTypes.LIST, AsmTypes.FLUIDSTACK)));
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
