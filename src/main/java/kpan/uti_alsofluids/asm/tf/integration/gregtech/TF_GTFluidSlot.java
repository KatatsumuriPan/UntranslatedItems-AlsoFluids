package kpan.uti_alsofluids.asm.tf.integration.gregtech;

import java.util.function.Consumer;
import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.AsmUtil;
import kpan.uti_alsofluids.asm.core.adapters.Instructions;
import kpan.uti_alsofluids.asm.core.adapters.MyClassVisitor;
import kpan.uti_alsofluids.asm.core.adapters.ReplaceInstructionsAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class TF_GTFluidSlot {

	private static final String TARGET = "gregtech.common.mui.widget.GTFluidSlot";
	private static final String HOOK = AsmTypes.HOOK + "integration/gregtech/" + "HK_" + "GTFluidSlot";

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (TARGET.equals(className)) {
			ClassVisitor newcv = new MyClassVisitor(cv, className) {
				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
					if ("<init>".equals(name)) {
						mv = AsmUtil.traceMethod(mv, name);
						mv = new ReplaceInstructionsAdapter(mv, name,
								Instructions.create()
										.dynamicRep()
								,
								Instructions.create()
										.invokeStatic(HOOK, "getTooltipBuilder", AsmUtil.toMethodDesc(Consumer.class, TARGET))
						);
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
