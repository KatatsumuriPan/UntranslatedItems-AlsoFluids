package kpan.uti_alsofluids.config;

import kpan.uti_alsofluids.ModReference;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigEventHandler {

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) {
		if (event.getModID().equals(ModReference.MOD_ID)) {
			ConfigHandler.syncAll();
		}
	}
}
