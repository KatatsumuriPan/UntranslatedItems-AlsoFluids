package kpan.uti_alsofluids.proxy;

import kpan.uti_alsofluids.ModReference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerSingleModel(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}

	@Override
	public void registerMultiItemModel(Item item, int meta, String filename, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(ModReference.MOD_ID, filename), id));
	}
	@Override
	public void registerOnlyClient() {
		//		MinecraftForge.EVENT_BUS.register(new MouseHandler());
	}

	@Override
	public boolean hasClientSide() { return true; }

}
