package kpan.uti_alsofluids.asm.hook.integration.ftbquests;

import com.feed_the_beast.ftbquests.gui.tree.ButtonTask;
import com.feed_the_beast.ftbquests.quest.task.FluidTask;
import com.feed_the_beast.ftbquests.quest.task.ItemTask;
import com.feed_the_beast.ftbquests.quest.task.Task;
import kpan.uti_alsofluids.asm.acc.integration.ftbquests.ACC_ButtonTask;
import kpan.uti_alsofluids.asm.hook.LocalizedName;
import kpan.uti_alsofluids.config.ConfigHolder;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class HK_ButtonTask {

	public static void onConstruct(ButtonTask self, Task t) {
		if (t instanceof ItemTask || t instanceof FluidTask) {
			t.clearCachedData();
			synchronized (LocalizedName.langmapus.localizedLock) {
				try {
					LocalizedName.langmapus.localizedThread = Thread.currentThread();
					((ACC_ButtonTask) self).set_localizedLine(t.getTitle());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					LocalizedName.langmapus.localizedThread = null;
				}
			}
			t.clearCachedData();
		}
	}

	public static void addLocalizedLine(ButtonTask self, List<String> list) {
		if (LocalizedName.showLocalize() && ConfigHolder.client.FTBQuests.showLocalizedName) {
			String localized = ((ACC_ButtonTask) self).get_localizedLine();
			if (localized != null && !list.get(list.size() - 1).equals(localized))
				list.add(TextFormatting.GRAY + localized);
		}
	}
}
