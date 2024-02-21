package kpan.uti_alsofluids.asm.hook.integration.betterquesting;

import kpan.uti_alsofluids.asm.hook.LocalizedName;
import kpan.uti_alsofluids.config.ConfigHolder;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class HK_PanelFluidSlot {
	public static void addLocalizedLine(FluidStack fluidStack, List<String> list) {
		if (LocalizedName.showLocalize() && ConfigHolder.client.BetterQuesting.showLocalizedName) {
			String localized = LocalizedName.getLocalizedName(fluidStack);
			if (localized != null && !list.get(list.size() - 1).equals(localized))
				list.add(TextFormatting.GRAY + localized);
		}
	}
}
