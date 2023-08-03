package kpan.uti_alsofluids.asm.tf.integration.ae2;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.AsmUtil;
import kpan.uti_alsofluids.asm.core.MyAsmNameRemapper.MethodRemap;
import kpan.uti_alsofluids.asm.core.adapters.Instructions;
import kpan.uti_alsofluids.asm.core.adapters.MyClassVisitor;
import kpan.uti_alsofluids.asm.core.adapters.ReplaceInstructionsAdapter;

public class TF_GuiFluidSlot {

	private static final String TARGET = "appeng.fluids.client.gui.widgets.GuiFluidSlot";
	private static final String HOOK = AsmTypes.HOOK + "integration/ae2/" + "HK_" + "GuiFluidSlot";
	private static final MethodRemap getLocalizedName = new MethodRemap(AsmTypes.FLUIDSTACK, "getLocalizedName", AsmUtil.toMethodDesc(AsmTypes.STRING));

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (TARGET.equals(className)) {
			ClassVisitor newcv = new MyClassVisitor(cv, className) {
				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
					if ("getMessage".equals(name)) {
						mv = new ReplaceInstructionsAdapter(mv, name,
								Instructions.create()
										.invokeVirtual(getLocalizedName),
								Instructions.create()
										.invokeStatic(HOOK, "getBothNames", AsmUtil.toMethodDesc(AsmTypes.STRING, AsmTypes.FLUIDSTACK)));
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
