package kpan.uti_alsofluids.config;

import kpan.uti_alsofluids.ModTagsGenerated;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ModGuiConfig extends GuiConfig {

	public ModGuiConfig(GuiScreen parentScreen) {
		super(parentScreen, ConfigHandler.getConfigElements(ConfigHandler.config), ModTagsGenerated.MODID, false, false, ModTagsGenerated.MODNAME);
	}

}
