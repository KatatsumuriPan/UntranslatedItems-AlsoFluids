package kpan.uti_alsofluids.asm.tf.integration.gregtech;

import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.AsmUtil;
import kpan.uti_alsofluids.asm.core.adapters.MyClassVisitor;
import kpan.uti_alsofluids.asm.core.adapters.ReplaceRefMethodAdapter;
import org.objectweb.asm.ClassVisitor;

public class TF_GTFluid$GTMaterialFluid {

	private static final String TARGET = "gregtech.api.fluids.GTFluid$GTMaterialFluid";
	private static final String HOOK = AsmTypes.HOOK + "integration/gregtech/" + "HK_" + "GTMaterialFluid";

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (TARGET.equals(className)) {
			MyClassVisitor newcv = new ReplaceRefMethodAdapter(cv, HOOK, TARGET, "getLocalizedName", AsmUtil.toMethodDesc(AsmTypes.STRING, AsmTypes.FLUIDSTACK));
			newcv.setSuccessExpectedMin(0);//サーバーだと見つからない
			return newcv;
		}
		return cv;
	}
}
