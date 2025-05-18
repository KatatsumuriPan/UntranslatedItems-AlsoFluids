package kpan.uti_alsofluids.asm.hook.integration.gregtech;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.RichTooltip;
import gregtech.api.mui.sync.GTFluidSyncHandler;
import gregtech.api.util.FluidTooltipUtil;
import gregtech.common.mui.widget.GTFluidSlot;
import java.util.function.Consumer;
import kpan.uti_alsofluids.asm.hook.LocalizedName;
import net.minecraftforge.fluids.FluidStack;

public class HK_GTFluidSlot {

	public static Consumer<RichTooltip> getTooltipBuilder(GTFluidSlot self) {
		return (tooltip) -> {
			if (self.isSynced()) {
				GTFluidSyncHandler syncHandler = (GTFluidSyncHandler) self.getSyncHandler();
				FluidStack fluid = syncHandler.getFluid();
				if (fluid == null) {
					fluid = syncHandler.getLockedFluid();
				}

				if (fluid != null) {
					tooltip.addLine(IKey.str(fluid.getLocalizedName()));
					tooltip.addLine(IKey.str(LocalizedName.getLocalizedName(fluid)));
					if (syncHandler.showAmount()) {
						tooltip.addLine(IKey.lang("gregtech.fluid.amount", fluid.amount, syncHandler.getCapacity()));
					}

					if (syncHandler.isPhantom() && syncHandler.showAmount()) {
						tooltip.addLine(IKey.lang("modularui.fluid.phantom.control"));
					}

					for (String s : FluidTooltipUtil.getFluidTooltip(fluid)) {
						if (!s.isEmpty()) {
							tooltip.addLine(IKey.str(s));
						}
					}

					if (syncHandler.showAmount()) {
						GTFluidSlot.addIngotMolFluidTooltip(fluid, tooltip);
					}

				}
			}
		};
	}

}
