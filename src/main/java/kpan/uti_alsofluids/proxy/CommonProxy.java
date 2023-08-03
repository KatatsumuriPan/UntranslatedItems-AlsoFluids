package kpan.uti_alsofluids.proxy;

import net.minecraft.item.Item;

public class CommonProxy {
	public void registerSingleModel(Item item, int meta, String id) {}
	public void registerMultiItemModel(Item item, int meta, String filename, String id) {}
	public void registerOnlyClient() {}
	public boolean hasClientSide() { return false; }
}
