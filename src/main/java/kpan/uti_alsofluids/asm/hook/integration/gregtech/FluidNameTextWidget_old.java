package kpan.uti_alsofluids.asm.hook.integration.gregtech;

import gregtech.api.gui.IRenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.util.Position;
import gregtech.api.util.Size;
import kpan.uti_alsofluids.asm.hook.LocalizedName;
import kpan.uti_alsofluids.config.ConfigHolder;
import kpan.uti_alsofluids.util.MyByteBufUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FluidNameTextWidget_old extends Widget {
	protected int maxWidthLimit;

	protected final Supplier<String> textSupplier;//server

	private String fluidName;//server,client

	private final int color;//server,client

	private boolean showLocalizedLine = true;//client

	private List<ITextComponent> notTweakedText = null;//client

	public FluidNameTextWidget_old(int xPosition, int yPosition, Supplier<String> text, int color) {
		super(new Position(xPosition, yPosition), Size.ZERO);
		textSupplier = text;
		this.color = color;
	}

	@Override
	public void detectAndSendChanges() {
		String new_name = textSupplier.get();
		if (!new_name.equals(fluidName)) {
			fluidName = new_name;
			writeUpdateInfo(1, buffer -> {
				buffer.writeVarInt(0);//サーバーにもmodがインストールされてるか検出用
				MyByteBufUtil.writeString(buffer, new_name);
			});
		}
	}

	@SideOnly(Side.CLIENT)
	private void updateComponentTextSize() {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		int maxStringWidth = 0;
		int totalHeight = 0;
		if (notTweakedText == null) {
			FluidStack fluidStack = FluidRegistry.getFluidStack(fluidName, 1000);
			if (fluidStack != null) {
				if (showLocalizedLine) {
					maxStringWidth = Math.max(fontRenderer.getStringWidth(fluidStack.getLocalizedName()), fontRenderer.getStringWidth(LocalizedName.getLocalizedName(fluidStack)));
					totalHeight = fontRenderer.FONT_HEIGHT * 2 + 2 * 1;
				} else {
					maxStringWidth = fontRenderer.getStringWidth(fluidStack.getLocalizedName());
					totalHeight = fontRenderer.FONT_HEIGHT;
				}
				setSize(new Size(maxStringWidth, totalHeight));
				if (uiAccess != null)
					uiAccess.notifySizeChange();
			}
		} else {
			for (ITextComponent textComponent : notTweakedText) {
				maxStringWidth = Math.max(maxStringWidth, fontRenderer.getStringWidth(textComponent.getFormattedText()));
				totalHeight += fontRenderer.FONT_HEIGHT + 2;
			}
			totalHeight -= 2;
			setSize(new Size(maxStringWidth, totalHeight));
			if (uiAccess != null)
				uiAccess.notifySizeChange();
		}
	}

	@Override
	public void readUpdateInfo(int id, PacketBuffer buffer) {
		if (id == 1) {
			int count = buffer.readVarInt();
			if (count == 0) {
				//サーバーにもインストールされてる
				fluidName = MyByteBufUtil.readString(buffer);
				showLocalizedLine = LocalizedName.showLocalize() && ConfigHolder.client.GregTechCEu.showLocalizedName;
				updateComponentTextSize();
			} else {
				//残念ながらクライアントのみ
				notTweakedText = new ArrayList<>(count);
				for (int i = 0; i < count; i++) {
					String jsonText = buffer.readString(32767);
					notTweakedText.add(ITextComponent.Serializer.jsonToComponent(jsonText));
				}
//				if (notTweakedText.size() == 1 && notTweakedText.get(0) instanceof TextComponentTranslation) {
//					TextComponentTranslation tct = (TextComponentTranslation) notTweakedText.get(0);
//					if (tct.getFormatArgs().length == 0) {
//						notTweakedText = null;
//						fluidName = tct.;
//						showLocalizedLine = LocalizedName.showLocalize() && ConfigHolder.client.GregTechCEu.showLocalizedName;
//						updateComponentTextSize();
//					}
//				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawInBackground(int mouseX, int mouseY, float partialTicks, IRenderContext context) {
		super.drawInBackground(mouseX, mouseY, partialTicks, context);
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		if (notTweakedText == null) {
			if (showLocalizedLine != LocalizedName.showLocalize() && ConfigHolder.client.GregTechCEu.showLocalizedName) {
				showLocalizedLine = LocalizedName.showLocalize() && ConfigHolder.client.GregTechCEu.showLocalizedName;
				updateComponentTextSize();
			}
			Position position = getPosition();
			FluidStack fluidStack = FluidRegistry.getFluidStack(fluidName, 1000);
			if (fluidStack != null) {
				fontRenderer.drawString(fluidStack.getLocalizedName(), position.x, position.y, color);
				if (showLocalizedLine)
					fontRenderer.drawString(LocalizedName.getLocalizedName(fluidStack), position.x, position.y + fontRenderer.FONT_HEIGHT + 2, color);
			}
		} else {
			Position position = getPosition();
			for (int i = 0; i < notTweakedText.size(); i++) {
				fontRenderer.drawString(notTweakedText.get(i).getFormattedText(), position.x, position.y + i * (fontRenderer.FONT_HEIGHT + 2), color);
			}
		}
	}

//	@SideOnly(Side.CLIENT)
//	private void formatDisplayText() {
//		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
//		int maxTextWidthResult = (maxWidthLimit == 0) ? Integer.MAX_VALUE : maxWidthLimit;
//		notTweakedText = notTweakedText.stream().flatMap(c -> GuiUtilRenderComponents.splitText(c, maxTextWidthResult, fontRenderer, true, true).stream()).collect(Collectors.toList());
//	}


}
