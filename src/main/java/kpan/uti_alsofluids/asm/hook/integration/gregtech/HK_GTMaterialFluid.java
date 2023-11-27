package kpan.uti_alsofluids.asm.hook.integration.gregtech;

import bre.nti.LanguageMapUs;
import gregtech.api.fluids.GTFluid.GTMaterialFluid;
import kpan.uti_alsofluids.ModMain;
import kpan.uti_alsofluids.util.MyReflectionHelper;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.FluidStack;

public class HK_GTMaterialFluid {

	public static String getLocalizedName(GTMaterialFluid self, FluidStack stack) {
		if (ModMain.proxy.hasClientSide()) {
			return ClientOnly.getLocalizedName(self);
		} else {
			return getLocalizedName(self);
		}
	}

	private static class ClientOnly {

		private static LanguageMapUs langmapus = LanguageMapUs.getInstanceUs();
		private static int recursion = 0;

		public static String getLocalizedName(GTMaterialFluid materialFluid) {
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
					return HK_GTMaterialFluid.getLocalizedName(materialFluid);
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

	private static String getLocalizedName(GTMaterialFluid materialFluid) {
		String localizedName;
		String customMaterialTranslation = "fluid." + materialFluid.getMaterial().getUnlocalizedName();

		if (I18n.hasKey(customMaterialTranslation)) {
			localizedName = I18n.format(customMaterialTranslation);
		} else {
			localizedName = I18n.format(materialFluid.getMaterial().getUnlocalizedName());
		}

		String translationKey = MyReflectionHelper.getPrivateField(materialFluid, "translationKey");
		if (translationKey != null) {
			return I18n.format(translationKey, localizedName);
		}
		return localizedName;
	}
}
