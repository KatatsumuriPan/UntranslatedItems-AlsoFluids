package kpan.uti_alsofluids.util.handlers;

import kpan.uti_alsofluids.ModMain;
import kpan.uti_alsofluids.config.ConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.DataSerializerEntry;

@EventBusSubscriber
public class RegistryHandler {

	public static void preInitRegistries(FMLPreInitializationEvent event) {
		ConfigHandler.preInit(event);
		MinecraftForge.EVENT_BUS.register(new RegistryHandler());
		ModMain.proxy.registerOnlyClient();

	}

	public static void initRegistries() {
	}

	public static void postInitRegistries() {
	}

	public static void serverRegistries(FMLServerStartingEvent event) {
	}

	@SubscribeEvent
	public void onBlockRegister(RegistryEvent.Register<Block> event) {
	}

	@SubscribeEvent
	public void onItemRegister(RegistryEvent.Register<Item> event) {
	}

	@SubscribeEvent
	public void onEnchantmentRegister(RegistryEvent.Register<Enchantment> event) {
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
	}

	@SubscribeEvent
	public static void onDataSerializerRegister(RegistryEvent.Register<DataSerializerEntry> event) {
	}

	//モデル登録時
	@SubscribeEvent
	public void onModelRegister(ModelRegistryEvent event) {
	}

}
