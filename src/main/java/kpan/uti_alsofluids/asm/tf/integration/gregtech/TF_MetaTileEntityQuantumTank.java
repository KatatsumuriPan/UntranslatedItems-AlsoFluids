package kpan.uti_alsofluids.asm.tf.integration.gregtech;

import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.adapters.Instructions;
import kpan.uti_alsofluids.asm.core.adapters.Instructions.OpcodeInt;
import kpan.uti_alsofluids.asm.core.adapters.MyClassVisitor;
import kpan.uti_alsofluids.asm.core.adapters.ReplaceInstructionsAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TF_MetaTileEntityQuantumTank {

	private static final String TARGET = "gregtech.common.metatileentities.storage.MetaTileEntityQuantumTank";
	private static final String FLUID_NAME_TEXT_WIDGET = AsmTypes.HOOK + "integration/gregtech/" + "FluidNameTextWidget";

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (TARGET.equals(className)) {
			ClassVisitor newcv = new MyClassVisitor(cv, className) {
				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
					if (name.equals("createUI")) {
						mv = new ReplaceInstructionsAdapter(mv, name,
								Instructions.create()
										.typeInsn(Opcodes.NEW, "gregtech/api/gui/widgets/AdvancedTextWidget")
										.insn(Opcodes.DUP)
										.intInsn(OpcodeInt.BIPUSH, 11)
										.intInsn(OpcodeInt.BIPUSH, 40)
										.aload(0)
										.aload(2)
										.label(7)
										.invokespecial(TARGET, "getFluidNameText", "(Lgregtech/api/gui/widgets/TankWidget;)Ljava/util/function/Consumer;")
										.ldcInsn(16777215)
										.invokespecial("gregtech/api/gui/widgets/AdvancedTextWidget", "<init>", "(IILjava/util/function/Consumer;I)V")
								,
								Instructions.create()
										.typeInsn(Opcodes.NEW, FLUID_NAME_TEXT_WIDGET)
										.insn(Opcodes.DUP)
										.intInsn(OpcodeInt.BIPUSH, 11)
										.intInsn(OpcodeInt.BIPUSH, 40)
										.aload(0)
										.aload(2)
										.label(7)
										.invokespecial(TARGET, "getFluidNameText", "(Lgregtech/api/gui/widgets/TankWidget;)Ljava/util/function/Consumer;")
										.ldcInsn(16777215)
										.invokespecial(FLUID_NAME_TEXT_WIDGET, "<init>", "(IILjava/util/function/Consumer;I)V")
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
