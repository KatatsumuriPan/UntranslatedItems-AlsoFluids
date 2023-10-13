package kpan.uti_alsofluids.asm.hook.integration.gregtech;

import bre.nti.LanguageMapUs;
import gregtech.api.fluids.MaterialFluid;
import kpan.uti_alsofluids.ModMain;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.FluidStack;

public class HK_MaterialFluid {

	public static String getLocalizedName(MaterialFluid self, FluidStack stack) {
		if (ModMain.proxy.hasClientSide()) {
			return ClientOnly.getLocalizedName(self);
		} else {
			return getLocalizedName(self);
		}
	}

	private static class ClientOnly {

		private static LanguageMapUs langmapus = LanguageMapUs.getInstanceUs();
		private static int recursion = 0;

		public static String getLocalizedName(MaterialFluid materialFluid) {
			String s = materialFluid.getUnlocalizedName();
			if (s == null)
				return "";
			synchronized (langmapus.getDisplayNameLock) {
				try {
					if (langmapus.getDisplayNameThread != null) {
						recursion++;
					} else {
						langmapus.getDisplayNameThread = Thread.currentThread();
					}
					return HK_MaterialFluid.getLocalizedName(materialFluid);
				} catch (Exception e) {
					e.printStackTrace();
					return s;
				} finally {
					if (recursion == 0) {
						langmapus.getDisplayNameThread = null;
					} else {
						recursion--;
					}
				}
			}
		}
	}

	private static String getLocalizedName(MaterialFluid materialFluid) {
		String localizedName;
		String customTranslationKey = "fluid." + materialFluid.getMaterial().getUnlocalizedName();

		if (I18n.hasKey(customTranslationKey)) {
			localizedName = I18n.format(customTranslationKey);
		} else {
			localizedName = I18n.format(materialFluid.getUnlocalizedName());
		}

		if (materialFluid.getFluidType() != null) {
			return I18n.format(materialFluid.getFluidType().getLocalization(), localizedName);
		}
		return localizedName;
	}
}
