package kpan.uti_alsofluids.asm.hook.integration.ae2;

import kpan.uti_alsofluids.asm.hook.LocalizedName;
import kpan.uti_alsofluids.config.ConfigHolder;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class HK_GuiFluidTank {

	public static String getBothNames(Fluid fluid, FluidStack stack) {
		StringBuilder sb = new StringBuilder();
		String us_name = stack.getLocalizedName();
		sb.append(us_name);
		if (LocalizedName.showLocalize() && ConfigHolder.client.AppliedEnergistcs2.showLocalizedNameOnTerminal) {
			String localized = LocalizedName.getLocalizedName(stack);
			if (!us_name.equals(localized)) {
				sb.append('\n');
				sb.append(TextFormatting.GRAY + localized);
			}
		}
		return sb.toString();
	}
}
