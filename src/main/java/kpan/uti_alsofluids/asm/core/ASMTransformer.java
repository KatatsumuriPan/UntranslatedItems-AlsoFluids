package kpan.uti_alsofluids.asm.core;

import kpan.uti_alsofluids.asm.tf.TF_Fluid;
import kpan.uti_alsofluids.asm.tf.TF_FluidStack;
import kpan.uti_alsofluids.asm.tf.integration.ae2.TF_GuiCraft__;
import kpan.uti_alsofluids.asm.tf.integration.ae2.TF_GuiFluidSlot;
import kpan.uti_alsofluids.asm.tf.integration.ae2.TF_GuiFluidTank;
import kpan.uti_alsofluids.asm.tf.integration.ae2.TF_GuiFluidTerminal;
import kpan.uti_alsofluids.asm.tf.integration.gregtech.TF_MetaTileEntityFluidHatch;
import kpan.uti_alsofluids.asm.tf.integration.gregtech.TF_MetaTileEntityQuantumTank;
import kpan.uti_alsofluids.asm.tf.integration.gregtech.TF_PhantomFluidWidget;
import kpan.uti_alsofluids.asm.tf.integration.gregtech.TF_TankWidget;
import kpan.uti_alsofluids.asm.tf.integration.jei.TF_FluidStackRenderer;
import kpan.uti_alsofluids.asm.tf.integration.theoneprobe.TF_OverlayRenderer;
import kpan.uti_alsofluids.asm.tf.integration.unlocalizeditems.TF_LanguageMapUs;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class ASMTransformer implements IClassTransformer {

	/**
	 * クラスが最初に読み込まれた時に呼ばれる。
	 *
	 * @param name            クラスの難読化名(区切りは'.')
	 * @param transformedName クラスの易読化名
	 * @param bytes           オリジナルのクラス
	 */
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		try {
			MyAsmNameRemapper.init();
			if (bytes == null)
				return bytes;
			if (transformedName.equals("mcjty.theoneprobe.rendering.OverlayRenderer")) {
				ClassReader cr = new ClassReader(bytes);
				ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				ClassVisitor cv = cw;
				cv = TF_OverlayRenderer.appendVisitor(cv, transformedName);
				cr.accept(cv, 0);
				return cw.toByteArray();
			}
			//byte配列を読み込み、利用しやすい形にする。
			ClassReader cr = new ClassReader(bytes);
			//これのvisitを呼ぶことによって情報が溜まっていく。
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);//maxStack,maxLocal,frameの全てを計算
			//Adapterを通して書き換え出来るようにする。
			ClassVisitor cv = cw;
			cv = TF_Fluid.appendVisitor(cv, transformedName);
			cv = TF_FluidStack.appendVisitor(cv, transformedName);
			cv = TF_FluidStackRenderer.appendVisitor(cv, transformedName);
			cv = TF_GuiCraft__.appendVisitor(cv, transformedName);
			cv = kpan.uti_alsofluids.asm.tf.integration.p455w0rd.wct.TF_GuiCraft__.appendVisitor(cv, transformedName);
			cv = kpan.uti_alsofluids.asm.tf.integration.p455w0rd.wpt.TF_GuiCraft__.appendVisitor(cv, transformedName);
			cv = kpan.uti_alsofluids.asm.tf.integration.p455w0rd.wft.TF_GuiWFT.appendVisitor(cv, transformedName);
			cv = TF_GuiFluidSlot.appendVisitor(cv, transformedName);
			cv = TF_GuiFluidTank.appendVisitor(cv, transformedName);
			cv = TF_GuiFluidTerminal.appendVisitor(cv, transformedName);
			cv = TF_LanguageMapUs.appendVisitor(cv, transformedName);
			cv = TF_MetaTileEntityQuantumTank.appendVisitor(cv, transformedName);
			cv = TF_MetaTileEntityFluidHatch.appendVisitor(cv, transformedName);
			cv = TF_TankWidget.appendVisitor(cv, transformedName);
			cv = TF_PhantomFluidWidget.appendVisitor(cv, transformedName);

			if (cv == cw)
				return bytes;

			//元のクラスと同様の順番でvisitメソッドを呼んでくれる
			cr.accept(cv, 0);

			byte[] new_bytes = cw.toByteArray();

			//Writer内の情報をbyte配列にして返す。
			return new_bytes;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

}
