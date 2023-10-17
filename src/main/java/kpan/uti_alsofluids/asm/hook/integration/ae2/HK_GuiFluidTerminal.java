package kpan.uti_alsofluids.asm.hook.integration.ae2;

import kpan.uti_alsofluids.asm.hook.LocalizedName;
import kpan.uti_alsofluids.config.ConfigHolder;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class HK_GuiFluidTerminal {

	public static void addBothNames(List<String> tooltip, FluidStack stack) {
		String us_name = stack.getLocalizedName();
		tooltip.add(us_name);
		if (LocalizedName.showLocalize() && ConfigHolder.client.AppliedEnergistcs2.showLocalizedNameOnTerminal) {
			String localized = LocalizedName.getLocalizedName(stack);
			if (!us_name.equals(localized))
				tooltip.add(TextFormatting.GRAY + localized);
		}
	}
}
