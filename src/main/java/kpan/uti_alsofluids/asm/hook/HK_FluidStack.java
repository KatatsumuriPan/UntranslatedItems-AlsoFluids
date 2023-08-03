package kpan.uti_alsofluids.asm.hook;

import bre.nti.LanguageMapUs;
import kpan.uti_alsofluids.ModMain;
import net.minecraftforge.fluids.FluidStack;

public class HK_FluidStack {

	public static String getLocalizedName(FluidStack stack) {
		if (ModMain.proxy.hasClientSide())
			return ClientOnly.getLocalizedName(stack);
		else
			return stack.getFluid().getLocalizedName(stack);
	}

	private static class ClientOnly {

		private static LanguageMapUs langmapus = LanguageMapUs.getInstanceUs();

		private static int recursion = 0;

		public static String getLocalizedName(FluidStack stack) {
			synchronized (langmapus.getDisplayNameLock) {
				try {
					if (langmapus.getDisplayNameThread != null) {
						recursion++;
					} else {
						langmapus.getDisplayNameThread = Thread.currentThread();
						HK_Fluid.setThreadAlready = true;
					}
					return stack.getFluid().getLocalizedName(stack);
				} catch (Exception e) {
					e.printStackTrace();
					return stack.getUnlocalizedName();
				} finally {
					if (recursion == 0) {
						langmapus.getDisplayNameThread = null;
						HK_Fluid.setThreadAlready = false;
					} else {
						recursion--;
					}
				}
			}
		}
	}
}
