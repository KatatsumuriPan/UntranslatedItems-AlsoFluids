package kpan.uti_alsofluids.asm.hook;

import bre.nti.LanguageMapUs;
import kpan.uti_alsofluids.ModMain;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings("deprecation")
public class HK_FluidRegistry_WATER {

	public static String getLocalizedName(Fluid fluid, FluidStack stack) {
		String s = "tile.water.name";
		if (HK_Fluid.setThreadAlready)
			return I18n.translateToLocal(s);
		if (ModMain.proxy.hasClientSide())
			return ClientOnly.getLocalizedName(s);
		else
			return I18n.translateToLocal(s);
	}

	private static class ClientOnly {

		private static LanguageMapUs langmapus = LanguageMapUs.getInstanceUs();
		private static int recursion = 0;

		public static String getLocalizedName(String unlocalizedName) {
			synchronized (langmapus.getDisplayNameLock) {
				try {
					if (langmapus.getDisplayNameThread != null) {
						recursion++;
					} else {
						langmapus.getDisplayNameThread = Thread.currentThread();
					}
					return I18n.translateToLocal(unlocalizedName);
				} catch (Exception e) {
					e.printStackTrace();
					return unlocalizedName;
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
}
