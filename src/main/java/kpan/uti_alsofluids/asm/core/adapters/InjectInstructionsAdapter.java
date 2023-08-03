package kpan.uti_alsofluids.asm.core.adapters;

import org.objectweb.asm.MethodVisitor;

import kpan.uti_alsofluids.asm.core.adapters.Instructions.Instr;

public class InjectInstructionsAdapter extends ReplaceInstructionsAdapter {

	private final int injectIndex;

	public InjectInstructionsAdapter(MethodVisitor mv, String name, Instructions targets, Instructions instructions, int injectIndex) {
		super(mv, name, targets, instructions);
		if (injectIndex < 0)
			injectIndex = targets.size() + injectIndex + 1;
		this.injectIndex = injectIndex;
	}

	@Override
	protected void visitAllInstructions() {
		for (int i = 0; i < holds.size(); i++) {
			if (i == injectIndex)
				super.visitAllInstructions();
			holds.get(i).visit(mv, this);
		}
		if (injectIndex >= holds.size())
			super.visitAllInstructions();
	}

	public static InjectInstructionsAdapter before(MethodVisitor mv, String name, Instructions targets, Instructions instructions) {
		return new InjectInstructionsAdapter(mv, name, targets, instructions, 0);
	}

	public static InjectInstructionsAdapter after(MethodVisitor mv, String name, Instructions targets, Instructions instructions) {
		return new InjectInstructionsAdapter(mv, name, targets, instructions, -1);
	}

	public static MethodVisitor injectFirst(MethodVisitor mv, String nameForDebug, final Instructions instructions) {
		return new MyMethodVisitor(mv, nameForDebug) {
			@Override
			public void visitCode() {
				super.visitCode();
				for (Instr instruction : instructions) {
					instruction.visit(mv, this);
				}
				success();
			}
		};
	}
	@Deprecated
	public static MethodVisitor injectLast(MethodVisitor mv, String nameForDebug, final Instructions instructions) {
		return new MyMethodVisitor(mv, nameForDebug) {
			@Override
			public void visitEnd() {
				for (Instr instruction : instructions) {
					instruction.visit(mv, this);
				}
				success();
				super.visitEnd();
			}
		};
	}
}
