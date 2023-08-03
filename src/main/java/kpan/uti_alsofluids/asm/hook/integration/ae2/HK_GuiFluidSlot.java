package kpan.uti_alsofluids.asm.hook.integration.ae2;

import kpan.uti_alsofluids.asm.hook.LocalizedName;
import kpan.uti_alsofluids.config.ConfigHolder;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

public class HK_GuiFluidSlot {

	public static String getBothNames(FluidStack stack) {
		StringBuilder sb = new StringBuilder();
		sb.append(stack.getLocalizedName());
		if (LocalizedName.showLocalize() && ConfigHolder.client.AppliedEnergistcs2.showLocalizedNameOnTerminal) {
			sb.append('\n');
			sb.append(TextFormatting.GRAY + LocalizedName.getLocalizedName(stack));
		}
		return sb.toString();
	}
}
