package kpan.uti_alsofluids.asm.tf.integration.p455w0rd.wft;

import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.AsmUtil;
import kpan.uti_alsofluids.asm.core.adapters.Instructions;
import kpan.uti_alsofluids.asm.core.adapters.MyClassVisitor;
import kpan.uti_alsofluids.asm.core.adapters.ReplaceInstructionsAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TF_GuiWFT {

	private static final String TARGET = "p455w0rd.wft.client.gui.GuiWFT";
	private static final String HOOK = AsmTypes.HOOK + "integration/ae2/" + "HK_" + "GuiFluidTerminal";

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (TARGET.equals(className)) {
			ClassVisitor newcv = new MyClassVisitor(cv, className) {
				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
					if ("func_191948_b".equals(name)) {
						mv = new ReplaceInstructionsAdapter(mv, name,
								Instructions.create()
										.invokeVirtual(AsmTypes.FLUIDSTACK, "getLocalizedName", AsmUtil.toMethodDesc(AsmTypes.STRING))
										.invokeInterface("java/util/List", "add", "(Ljava/lang/Object;)Z")
										.insn(Opcodes.POP),
								Instructions.create()
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
