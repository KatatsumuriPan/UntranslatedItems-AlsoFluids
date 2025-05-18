package kpan.uti_alsofluids.asm.hook.integration.gregtech;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.text.RichText;
import gregtech.api.mui.sync.GTFluidSyncHandler;
import java.util.function.Consumer;

public class HK_MetaTileEntityReservoirHatch {

	public static Consumer<RichText> getTextBuilder(GTFluidSyncHandler fluidSyncHandler) {
		return (richText) -> {
			richText.addLine(IKey.lang("gregtech.gui.fluid_amount"));
			String usName = fluidSyncHandler.getFluidLocalizedName();
			if (usName != null) {
				richText.addLine(IKey.str(usName));

				String localizedName = HK_MetaTileEntityFluidHatch.getFluidTrueLocalizedName(fluidSyncHandler);
				if (localizedName != null) {
					richText.addLine(IKey.str(localizedName));
				}

				richText.addLine(IKey.str(fluidSyncHandler.getFormattedFluidAmount()));
			}
		};
	}

}
