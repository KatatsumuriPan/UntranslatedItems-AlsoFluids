package kpan.uti_alsofluids.asm.tf.integration.gregtech;

import java.util.function.Consumer;
import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.AsmUtil;
import kpan.uti_alsofluids.asm.core.adapters.Instructions;
import kpan.uti_alsofluids.asm.core.adapters.Instructions.OpcodeMethod;
import kpan.uti_alsofluids.asm.core.adapters.MyClassVisitor;
import kpan.uti_alsofluids.asm.core.adapters.ReplaceInstructionsAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TF_MetaTileEntityReservoirHatch {

	private static final String TARGET = "gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityReservoirHatch";
	private static final String HOOK = AsmTypes.HOOK + "integration/gregtech/" + "HK_" + "MetaTileEntityReservoirHatch";

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (TARGET.equals(className)) {
			ClassVisitor newcv = new MyClassVisitor(cv, className) {
				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
					if (access == Opcodes.ACC_PUBLIC && name.equals("buildUI")) {
						mv = new ReplaceInstructionsAdapter(mv, name,
								Instructions.create()
										.dynamicRep()
										.labelRep()
										.methodRep(OpcodeMethod.VIRTUAL, null, "textBuilder")
								,
								instructions -> Instructions.create()
										.invokeStatic(HOOK, "getTextBuilder", AsmUtil.toMethodDesc(Consumer.class, Types.GT_FLUID_SYNC_HANDLER))
										.addInstr(instructions.get(1)) // label
										.addInstr(instructions.get(2)) // textBuilder()
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
