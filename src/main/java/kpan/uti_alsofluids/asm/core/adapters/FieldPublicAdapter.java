package kpan.uti_alsofluids.asm.core.adapters;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import kpan.uti_alsofluids.asm.core.MyAsmNameRemapper;
import kpan.uti_alsofluids.asm.core.MyAsmNameRemapper.FieldRemap;

public class FieldPublicAdapter extends MyClassVisitor {

	protected final String runtimeName;

	public FieldPublicAdapter(ClassVisitor cv, FieldRemap field) {
		super(cv, field.deobfOwner);
		runtimeName = MyAsmNameRemapper.runtimeField(field);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		if (name.equals(runtimeName)) {
			access &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED);
			access |= Opcodes.ACC_PUBLIC;
			success();
		}
		return super.visitField(access, name, desc, signature, value);
	}

}
