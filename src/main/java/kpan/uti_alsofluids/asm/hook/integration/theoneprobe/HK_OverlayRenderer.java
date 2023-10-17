package kpan.uti_alsofluids.asm.hook.integration.theoneprobe;

import kpan.uti_alsofluids.asm.hook.LocalizedName;
import kpan.uti_alsofluids.config.ConfigHolder;
import kpan.uti_alsofluids.util.MyReflectionHelper;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IOverlayStyle;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.apiimpl.elements.AbstractElementPanel;
import mcjty.theoneprobe.apiimpl.elements.ElementHorizontal;
import mcjty.theoneprobe.apiimpl.elements.ElementIcon;
import mcjty.theoneprobe.apiimpl.elements.ElementItemStack;
import mcjty.theoneprobe.apiimpl.elements.ElementText;
import mcjty.theoneprobe.apiimpl.elements.ElementVertical;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;

public class HK_OverlayRenderer {

	public static void renderElements(ProbeInfo probeInfo, IOverlayStyle style, double sw, double sh, @Nullable IElement extra, Pair<Integer, BlockPos> key) {

	}

	public static ProbeInfo onRegisterProbeInfo(int dim, BlockPos pos, ProbeInfo probeInfo) {
		if (dim != Minecraft.getMinecraft().player.dimension)
			return probeInfo;
		IBlockState state = Minecraft.getMinecraft().world.getBlockState(pos);
		Block block = state.getBlock();
		if (block instanceof net.minecraftforge.fluids.BlockFluidBase || block instanceof net.minecraft.block.BlockLiquid) {
			Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
			if (fluid != null) {
				List<IElement> children = getFluidProbiInfoVerticalChildren(probeInfo);
				if (children != null) {
					if (LocalizedName.showLocalize() && ConfigHolder.client.TheOneProbe.showLocalizedNameBlock) {
						FluidStack fluidStack = new FluidStack(fluid, 1000);
						String localized = LocalizedName.getLocalizedName(fluidStack);
						if (!fluidStack.getLocalizedName().equals(localized))
							children.add(1, new ElementText(TextStyleClass.NAME + localized));
					}
				}
			}
		}
		return probeInfo;
	}

	@Nullable
	private static List<IElement> getFluidProbiInfoVerticalChildren(ProbeInfo probeInfo) {
		List<IElement> children = probeInfo.getElements();
		if (children.size() == 0)
			return null;
		IElement element = children.get(0);
		if (!(element instanceof ElementHorizontal))
			return null;
		ElementHorizontal horizontal = (ElementHorizontal) element;
		List<IElement> children_h = MyReflectionHelper.getPrivateField(AbstractElementPanel.class, horizontal, "children");
		if (children_h.size() != 2)
			return null;
		if (!(children_h.get(0) instanceof ElementItemStack) && !(children_h.get(0) instanceof ElementIcon))
			return null;
		if (!(children_h.get(1) instanceof ElementVertical))
			return null;
		ElementVertical vertical = (ElementVertical) children_h.get(1);
		List<IElement> children_v = MyReflectionHelper.getPrivateField(AbstractElementPanel.class, vertical, "children");
		if (children_v.size() != 2)
			return null;
		return children_v;
	}
}
