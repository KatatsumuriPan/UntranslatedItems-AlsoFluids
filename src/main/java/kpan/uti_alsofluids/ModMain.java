package kpan.uti_alsofluids;

import kpan.uti_alsofluids.proxy.CommonProxy;
import kpan.uti_alsofluids.util.handlers.RegistryHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//文字コードをMS932にすると日本語ベタ打ちしたものがゲーム時に文字化けしないが
//色々と問題があるので
//.langをちゃんと使うのを推奨

@Mod(modid = ModReference.MOD_ID, name = ModReference.MOD_NAME
		, guiFactory = ModReference.SRC_DIR + ".config.ModGuiFactory"
		, dependencies = ""
		, acceptableRemoteVersions = BuildInfo.MAJOR_VERSION + "." + BuildInfo.MINOR_VERSION
//
//, serverSideOnly = true //サーバーのみにする場合に必要(acceptableRemoteVersionsを*に変えないとダメ)、デバッグ時はオフにする
)
public class ModMain {

	@Instance
	public static ModMain instance;

	@SidedProxy(clientSide = ModReference.CLIENT_PROXY_CLASS, serverSide = ModReference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;

	public static MinecraftServer server;
	public static final Logger LOGGER = LogManager.getLogger();

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		RegistryHandler.preInitRegistries(event);
	}

	@EventHandler
	public static void init(FMLInitializationEvent event) { RegistryHandler.initRegistries(); }

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) { RegistryHandler.postInitRegistries(); }

	@EventHandler
	public static void serverInit(FMLServerStartingEvent event) { RegistryHandler.serverRegistries(event); }

	@EventHandler
	public static void onServerAboutToStart(FMLServerAboutToStartEvent event) {
		server = event.getServer();
	}
}
