package kpan.uti_alsofluids.asm.tf.integration.p455w0rd.wpt;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.AsmUtil;
import kpan.uti_alsofluids.asm.core.adapters.Instructions;
import kpan.uti_alsofluids.asm.core.adapters.MyClassVisitor;
import kpan.uti_alsofluids.asm.core.adapters.ReplaceInstructionsAdapter;
import net.minecraftforge.fml.common.Loader;

public class TF_GuiCraft__ {

	private static final String TARGET = "p455w0rd.wpt.client.gui.GuiCraftConfirm";
	private static final String TARGET2 = "p455w0rd.wpt.client.gui.GuiCraftingCPU";
	private static final String HOOK = AsmTypes.HOOK + "integration/ae2/" + "HookGuiCraft__";

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (TARGET.equals(className) || TARGET2.equals(className)) {
			ClassVisitor newcv = new MyClassVisitor(cv, className) {
				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
					if ("drawFG".equals(name)) {
						if (Loader.isModLoaded("ae2fc")) {
							mv = new ReplaceInstructionsAdapter(mv, name,
									Instructions.create()
											.invokeStatic("appeng.util.Platform", "getItemDisplayName", AsmUtil.toMethodDesc(AsmTypes.STRING, AsmTypes.OBJECT)),
									Instructions.create()
											.invokeStatic("com/glodblock/github/coremod/CoreModHooks", "displayFluid", AsmUtil.toMethodDesc(AsmTypes.ITEMSTACK, AsmTypes.ITEMSTACK))//2重に呼んでも問題ない
											.invokeStatic(HOOK, "getBothNames", AsmUtil.toMethodDesc(AsmTypes.STRING, AsmTypes.OBJECT)));
						} else {
							mv = new ReplaceInstructionsAdapter(mv, name,
									Instructions.create()
											.invokeStatic("appeng.util.Platform", "getItemDisplayName", AsmUtil.toMethodDesc(AsmTypes.STRING, AsmTypes.OBJECT)),
									Instructions.create()
											.invokeStatic(HOOK, "getBothNames", AsmUtil.toMethodDesc(AsmTypes.STRING, AsmTypes.OBJECT)));
						}
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
