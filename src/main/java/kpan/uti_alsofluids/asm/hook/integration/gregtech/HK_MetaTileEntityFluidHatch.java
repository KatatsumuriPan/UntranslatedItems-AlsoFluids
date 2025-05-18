package kpan.uti_alsofluids.asm.hook.integration.gregtech;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.text.RichText;
import gregtech.api.mui.sync.GTFluidSyncHandler;
import java.util.function.Consumer;
import kpan.uti_alsofluids.asm.hook.LocalizedName;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class HK_MetaTileEntityFluidHatch {

	public static Consumer<RichText> getTextBuilder(GTFluidSyncHandler fluidSyncHandler) {
		return (richText) -> {
			richText.addLine(IKey.lang("gregtech.gui.fluid_amount"));
			String usName = fluidSyncHandler.getFluidLocalizedName();
			if (usName != null) {
				if (usName.length() > 25) {
					usName = usName.substring(0, 25) + "...";
				}
				richText.addLine(IKey.str(usName));

				String localizedName = getFluidTrueLocalizedName(fluidSyncHandler);
				if (localizedName != null) {
					if (localizedName.length() > 25) {
						localizedName = localizedName.substring(0, 25) + "...";
					}
					richText.addLine(IKey.str(localizedName));
				}

				richText.addLine(IKey.str(fluidSyncHandler.getFormattedFluidAmount()));
			}
		};
	}

	@Nullable
	public static String getFluidTrueLocalizedName(GTFluidSyncHandler fluidSyncHandler) {
		FluidStack tankFluid = fluidSyncHandler.getFluid();
		if (tankFluid == null && fluidSyncHandler.canLockFluid()) {
			tankFluid = fluidSyncHandler.getLockedFluid();// phantom状態だと取得できないので正確にはイコールではない
		}

		return tankFluid == null ? "" : LocalizedName.getLocalizedName(tankFluid);
	}

}
