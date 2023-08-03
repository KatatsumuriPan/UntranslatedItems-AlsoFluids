package kpan.uti_alsofluids.asm.tf;

import org.objectweb.asm.ClassVisitor;

import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.AsmUtil;
import kpan.uti_alsofluids.asm.core.adapters.ReplaceRefMethodAdapter;

public class TF_FluidRegistry_WATER {

	private static final String TARGET = "net.minecraftforge.fluids.FluidRegistry$1";
	private static final String HOOK = AsmTypes.HOOK + "HK_" + "FluidRegistry_WATER";

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (TARGET.equals(className)) {
			ClassVisitor newcv = new ReplaceRefMethodAdapter(cv, HOOK, TARGET, "getLocalizedName", AsmUtil.toMethodDesc(AsmTypes.STRING, AsmTypes.FLUIDSTACK));
			return newcv;
		}
		return cv;
	}
}
