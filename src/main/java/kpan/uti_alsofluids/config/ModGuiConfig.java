package kpan.uti_alsofluids.config;

import kpan.uti_alsofluids.ModReference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ModGuiConfig extends GuiConfig {

	public ModGuiConfig(GuiScreen parentScreen) {
		super(parentScreen, ConfigHandler.getConfigElements(ConfigHandler.config), ModReference.MOD_ID, false, false, ModReference.MOD_NAME);
	}

}
