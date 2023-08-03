package kpan.uti_alsofluids.asm.hook.integration.ae2;

import kpan.uti_alsofluids.asm.hook.LocalizedName;
import kpan.uti_alsofluids.config.ConfigHolder;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class HK_GuiFluidTerminal {

	public static void addBothNames(List<String> tooltip, FluidStack stack) {
		tooltip.add(stack.getLocalizedName());
		if (LocalizedName.showLocalize() && ConfigHolder.client.AppliedEnergistcs2.showLocalizedNameOnTerminal)
			tooltip.add(TextFormatting.GRAY + LocalizedName.getLocalizedName(stack));
	}
}
