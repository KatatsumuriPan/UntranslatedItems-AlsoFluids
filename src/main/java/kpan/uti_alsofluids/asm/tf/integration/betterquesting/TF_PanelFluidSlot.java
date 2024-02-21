package kpan.uti_alsofluids.asm.tf.integration.betterquesting;

import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.AsmUtil;
import kpan.uti_alsofluids.asm.core.adapters.InjectInstructionsAdapter;
import kpan.uti_alsofluids.asm.core.adapters.Instructions;
import kpan.uti_alsofluids.asm.core.adapters.MyClassVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TF_PanelFluidSlot {

	private static final String TARGET = "betterquesting.api2.client.gui.panels.content.PanelFluidSlot";
	private static final String HOOK = AsmTypes.HOOK + "integration/betterquesting/" + "HK_" + "PanelFluidSlot";

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (!TARGET.equals(className))
			return cv;
		ClassVisitor newcv = new MyClassVisitor(cv, className) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				if ("setStoredValue".equals(name) && desc.equals(AsmUtil.toMethodDesc(TARGET, AsmTypes.FLUIDSTACK))) {
					mv = InjectInstructionsAdapter.after(mv, name
							, Instructions.create()
									.invokeVirtual(AsmTypes.FLUIDSTACK, "getLocalizedName", AsmUtil.composeRuntimeMethodDesc(AsmTypes.STRING))
									.invokeInterface(AsmTypes.LIST, "add", AsmUtil.composeRuntimeMethodDesc(AsmTypes.BOOL, AsmTypes.OBJECT))
									.insn(Opcodes.POP)
							, Instructions.create()
									.aload(1)
									.aload(2)
									.invokeStatic(HOOK, "addLocalizedLine", AsmUtil.composeRuntimeMethodDesc(AsmTypes.VOID, AsmTypes.FLUIDSTACK, AsmTypes.LIST))
					);
					success();
				}
				return mv;
			}
		};
		return newcv;
	}
}
