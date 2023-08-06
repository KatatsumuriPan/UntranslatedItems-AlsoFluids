package kpan.uti_alsofluids.asm.hook;

import bre.nti.LanguageMapUs;
import kpan.uti_alsofluids.config.ConfigHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.FluidStack;

public class LocalizedName {

	public static LanguageMapUs langmapus = LanguageMapUs.getInstanceUs();

	public static boolean showLocalize() {
		//ModLib.isLangを使いたかったがmcp名環境では非常に面倒なため断念
		if (Minecraft.getMinecraft().gameSettings != null && Minecraft.getMinecraft().gameSettings.language.equals("en_US"))
			return false;
		return ConfigHolder.client.showLocalizedName;
	}

	public static String getLocalizedName(FluidStack fluidStack) {
		String localizedItemName = "";
		if (fluidStack == null)
			return "< null >";
		synchronized (langmapus.localizedLock) {
			try {
				langmapus.localizedThread = Thread.currentThread();
				localizedItemName = fluidStack.getLocalizedName();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				langmapus.localizedThread = null;
			}
		}
		return localizedItemName;
	}

	public static String getUsLocalizedName(String unlocalizedName) {
		return I18n.translateToLocal(unlocalizedName);
	}

	public static String getLocalizedName(String unlocalizedName) {
		synchronized (langmapus.localizedLock) {
			try {
				return I18n.translateToLocal(unlocalizedName);
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			} finally {
				langmapus.localizedThread = null;
			}
		}
	}
}
