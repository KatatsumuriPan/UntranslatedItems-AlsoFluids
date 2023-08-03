package kpan.uti_alsofluids.config;

import kpan.uti_alsofluids.config.ConfigAnnotations.BooleanValue;
import kpan.uti_alsofluids.config.ConfigAnnotations.Comment;
import net.minecraftforge.common.config.Configuration;

public class ConfigHolder {

	//	@Comment("Common settings(Blocks, items, etc.)") //
	//	public static Common common = new Common();

	public static class Common {

	}

	@Comment("Client only settings(Rendering, resources, etc.)") //
	public static Client client = new Client();

	public static class Client {

		@Comment("Show the localized name of the fluid.") //
		@BooleanValue(defaultValue = true)
		public boolean showLocalizedName = true;
		public AppliedEnergistcs2_ AppliedEnergistcs2 = new AppliedEnergistcs2_();
		public GregTechCEu_ GregTechCEu = new GregTechCEu_();
		public JustEnoughItems_ JEI = new JustEnoughItems_();
		public TheOneProbe_ TheOneProbe = new TheOneProbe_();

		public static class AppliedEnergistcs2_ {
			@Comment("Show the localized name of the fluid on a terminal.") //
			@BooleanValue(defaultValue = true)
			public boolean showLocalizedNameOnTerminal = true;
			@Comment("Show the localized name of the fluid on a crafting status gui.") //
			@BooleanValue(defaultValue = true)
			public boolean showLocalizedNameOnCraftingStatus = true;
		}

		public static class GregTechCEu_ {
			@Comment("Show the localized name of the fluid.") //
			@BooleanValue(defaultValue = true)
			public boolean showLocalizedName = true;
		}

		public static class JustEnoughItems_ {
			@Comment("Show the localized name of the fluid.") //
			@BooleanValue(defaultValue = true)
			public boolean showLocalizedName = true;
		}

		public static class TheOneProbe_ {
			@Comment("Show the localized name of the fluid block.") //
			@BooleanValue(defaultValue = true)
			public boolean showLocalizedNameBlock = true;
			//		@Comment("Show the localized name of the fluid in the tank.") //
			//		public boolean showLocalizedNameTank = true;
			//		@Comment("Show the localized name of the fluid in the tank.") //
			//		@Config.RangeInt(min = 0, max = 2) //
			//		public int showTankMode = 0;
		}
	}

	//	@Comment("Server settings(Behaviors, phisics, etc.)") //
	//	public static Server server = new Server();

	public static class Server {

	}

	public static void updateVersion(Configuration config) {
		String loadedConfigVersion = config.getLoadedConfigVersion();
		if (loadedConfigVersion == null) {
			config.getBoolean("showLocalizedName", "client", config.getBoolean("showLocalizedName", "general", true, ""), "");
			config.getBoolean("showLocalizedNameOnCraftingStatus", "client.AppliedEnergistcs2", config.getBoolean("showLocalizedNameOnCraftingStatus", "general.appliedenergistcs2", true, ""), "");
			config.getBoolean("showLocalizedNameOnTerminal", "client.AppliedEnergistcs2", config.getBoolean("showLocalizedNameOnTerminal", "general.appliedenergistcs2", true, ""), "");
			config.getBoolean("showLocalizedName", "client.GregTechCEu", config.getBoolean("showLocalizedName", "general.gregtechceu", true, ""), "");
			config.getBoolean("showLocalizedName", "client.JEI", config.getBoolean("showLocalizedName", "general.jei", true, ""), "");
			config.getBoolean("showLocalizedNameBlock", "client.TheOneProbe", config.getBoolean("showLocalizedNameBlock", "general.theoneprobe", true, ""), "");
			config.removeCategory(config.getCategory("general"));
			return;
		}
		switch (loadedConfigVersion) {
			case "2":
				break;
			default:
				throw new RuntimeException("Unknown config version:" + loadedConfigVersion);
		}
	}

	public static String getVersion() {return "2";}
}
