package kpan.uti_alsofluids.asm.hook.integration.theoneprobe;

import io.netty.buffer.ByteBuf;
import kpan.uti_alsofluids.asm.hook.LocalizedName;
import kpan.uti_alsofluids.config.ConfigHolder;
import kpan.uti_alsofluids.util.MyByteBufUtil;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class ElementFluidBlockLabel implements IElement {

	public static int elementId = -1;
	private static final String INVALID_TEXT = "Invalid Fluid";

	public final @Nullable FluidStack fluidStack;

	public ElementFluidBlockLabel(ByteBuf buf) {
		fluidStack = MyByteBufUtil.readFluidStack(buf);
	}

	@Override
	public int getHeight() {
		if (fluidStack == null)
			return 10;
		if (LocalizedName.showLocalize() && ConfigHolder.client.TheOneProbe.showLocalizedNameBlock)
			return 20;
		else
			return 10;
	}

	@Override
	public int getID() { return elementId; }

	@Override
	public int getWidth() {
		if (fluidStack == null)
			return ElementTextRender.getWidth(INVALID_TEXT);
		if (LocalizedName.showLocalize() && ConfigHolder.client.TheOneProbe.showLocalizedNameBlock)
			return Math.max(ElementTextRender.getWidth(fluidStack.getLocalizedName()), ElementTextRender.getWidth(LocalizedName.getLocalizedName(fluidStack)));
		else
			return ElementTextRender.getWidth(fluidStack.getLocalizedName());
	}

	@Override
	public void render(int x, int y) {
		String us_name = fluidStack.getLocalizedName();
		ElementTextRender.render(us_name, x, y);
		if (LocalizedName.showLocalize() && ConfigHolder.client.TheOneProbe.showLocalizedNameBlock) {
			String localized = LocalizedName.getLocalizedName(fluidStack);
			if (!us_name.equals(localized))
				ElementTextRender.render(localized, x, y + 10);
		}
	}

	@Override
	public void toBytes(ByteBuf arg0) {
		MyByteBufUtil.writeFluidStack(arg0, fluidStack);
	}

}
