package kpan.uti_alsofluids.asm.hook.integration.gregtech;

import kpan.uti_alsofluids.asm.hook.LocalizedName;
import kpan.uti_alsofluids.config.ConfigHolder;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class HK_PhantomFluidWidget {

	public static void addBothNames(List<String> tooltip, FluidStack stack) {
		tooltip.add(stack.getLocalizedName());
		if (LocalizedName.showLocalize() && ConfigHolder.client.GregTechCEu.showLocalizedName)
			tooltip.add(TextFormatting.GRAY + LocalizedName.getLocalizedName(stack));
	}
}
