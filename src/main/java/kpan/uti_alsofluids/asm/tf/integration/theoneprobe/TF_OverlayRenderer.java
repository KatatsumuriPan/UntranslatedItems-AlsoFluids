package kpan.uti_alsofluids.asm.tf.integration.theoneprobe;

import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.AsmUtil;
import kpan.uti_alsofluids.asm.core.adapters.InjectInstructionsAdapter;
import kpan.uti_alsofluids.asm.core.adapters.Instructions;
import kpan.uti_alsofluids.asm.core.adapters.Instructions.OpcodeVar;
import kpan.uti_alsofluids.asm.core.adapters.MyClassVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class TF_OverlayRenderer {

	private static final String TARGET = "mcjty.theoneprobe.rendering.OverlayRenderer";
	private static final String HOOK = AsmTypes.HOOK + "integration/theoneprobe/" + "HK_" + "OverlayRenderer";
	private static final String PROBE_INFO = "mcjty/theoneprobe/apiimpl/ProbeInfo";

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (TARGET.equals(className)) {
			ClassVisitor newcv = new MyClassVisitor(cv, className) {
				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
					if ("registerProbeInfo".equals(name) && desc.equals(AsmUtil.toMethodDesc(AsmTypes.VOID, AsmTypes.INT, AsmTypes.BLOCKPOS, PROBE_INFO))) {
						mv = InjectInstructionsAdapter.injectFirst(mv, name,
								Instructions.create()
										.varInsn(OpcodeVar.ILOAD, 0)
										.aload(1)
										.aload(2)
										.invokeStatic(HOOK, "onRegisterProbeInfo", AsmUtil.toMethodDesc(PROBE_INFO, AsmTypes.INT, AsmTypes.BLOCKPOS, PROBE_INFO))
										.varInsn(OpcodeVar.ASTORE, 2));
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
