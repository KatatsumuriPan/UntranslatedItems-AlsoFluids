package kpan.uti_alsofluids.asm.hook.integration.ae2;

import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import kpan.uti_alsofluids.asm.hook.LocalizedName;
import kpan.uti_alsofluids.config.ConfigHolder;

public class HookGuiCraft__ {

	public static String getBothNames(Object o) {
		String us_name = Platform.getItemDisplayName(o);
		if (LocalizedName.showLocalize() && ConfigHolder.client.AppliedEnergistcs2.showLocalizedNameOnCraftingStatus) {
			String localized = getLocalizedName(o);
			if (!us_name.equals(localized))
				return us_name + "\n" + localized;
		}
		return us_name;
	}

	public static String getLocalizedName(Object o) {
		String localizedItemName = "";
		synchronized (LocalizedName.langmapus.localizedLock) {
			try {
				LocalizedName.langmapus.localizedThread = Thread.currentThread();
				if (o instanceof AEItemStack)
					o = ((AEItemStack) o).asItemStackRepresentation();
				localizedItemName = Platform.getItemDisplayName(o);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				LocalizedName.langmapus.localizedThread = null;
			}
		}
		return localizedItemName;
	}
}
