package kpan.uti_alsofluids.asm.tf.integration.ftbquests;

import kpan.uti_alsofluids.asm.core.AsmTypes;
import kpan.uti_alsofluids.asm.core.AsmUtil;
import kpan.uti_alsofluids.asm.core.adapters.InjectInstructionsAdapter;
import kpan.uti_alsofluids.asm.core.adapters.Instructions;
import kpan.uti_alsofluids.asm.core.adapters.MixinAccessorAdapter;
import kpan.uti_alsofluids.asm.core.adapters.MyClassVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TF_ButtonTask {

	private static final String TARGET = "com.feed_the_beast.ftbquests.gui.tree.ButtonTask";
	private static final String HOOK = AsmTypes.HOOK + "integration/ftbquests/" + "HK_" + "ButtonTask";
	private static final String ACC = AsmTypes.ACC + "integration/ftbquests/" + "ACC_" + "ButtonTask";

	public static ClassVisitor appendVisitor(ClassVisitor cv, String className) {
		if (!TARGET.equals(className))
			return cv;
		ClassVisitor newcv = new MyClassVisitor(cv, className) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				if ("<init>".equals(name)) {
					mv = InjectInstructionsAdapter.injectBeforeReturns(mv, name,
							Instructions.create()
									.aload(0)
									.aload(2)
									.invokeStatic(HOOK, "onConstruct", AsmUtil.composeRuntimeMethodDesc(AsmTypes.VOID, TARGET, "com.feed_the_beast.ftbquests.quest.task.Task")));
					success();
				}
				if ("addMouseOverText".equals(name)) {
					mv = InjectInstructionsAdapter.after(mv, name
							, Instructions.create()
									.invokeVirtual(TARGET, "getTitle", AsmUtil.composeRuntimeMethodDesc(AsmTypes.STRING))
									.invokeInterface(AsmTypes.LIST, "add", AsmUtil.composeRuntimeMethodDesc(AsmTypes.BOOL, AsmTypes.OBJECT))
									.insn(Opcodes.POP)
							, Instructions.create()
									.aload(0)
									.aload(1)
									.invokeStatic(HOOK, "addLocalizedLine", AsmUtil.composeRuntimeMethodDesc(AsmTypes.VOID, TARGET, AsmTypes.LIST))
					);
					success();
				}
				return mv;
			}
		}.setSuccessExpected(2);
		newcv = new MixinAccessorAdapter(newcv, className, ACC);
		return newcv;
	}
}
