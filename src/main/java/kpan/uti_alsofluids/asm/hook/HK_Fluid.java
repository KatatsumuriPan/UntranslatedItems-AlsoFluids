package kpan.uti_alsofluids.asm.hook;

import bre.nti.LanguageMapUs;
import kpan.uti_alsofluids.ModMain;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class HK_Fluid {


	public static String getLocalizedName(Fluid fluid, FluidStack stack) {
		if (ModMain.proxy.hasClientSide()) {
			return ClientOnly.getLocalizedName(fluid);
		} else {
			String s = fluid.getUnlocalizedName();
			if (s == null)
				return "";
			return I18n.translateToLocal(s);
		}
	}

	private static class ClientOnly {

		private static LanguageMapUs langmapus = LanguageMapUs.getInstanceUs();
		private static int recursion = 0;

		public static String getLocalizedName(Fluid fluid) {
			String s = fluid.getUnlocalizedName();
			if (s == null)
				return "";
			synchronized (langmapus.getDisplayNameLock) {
				try {
					if (langmapus.getDisplayNameThread != null) {
						recursion++;
					} else {
						langmapus.getDisplayNameThread = Thread.currentThread();
					}
					return I18n.translateToLocal(s);
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
}
