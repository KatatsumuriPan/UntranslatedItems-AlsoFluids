package kpan.uti_alsofluids.asm.hook.integration.gregtech;

import gregtech.api.gui.IRenderContext;
import gregtech.api.gui.widgets.AdvancedTextWidget;
import gregtech.api.util.Position;
import gregtech.api.util.Size;
import kpan.uti_alsofluids.asm.hook.LocalizedName;
import kpan.uti_alsofluids.config.ConfigHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FluidNameTextWidget extends AdvancedTextWidget {

	private final int color;
	private List<ITextComponent> usNameText = new ArrayList<>();//client
	private List<ITextComponent> localizedNameText = null;//client

	public FluidNameTextWidget(int xPosition, int yPosition, Consumer<List<ITextComponent>> text, int color) {
		super(xPosition, yPosition, text, color);
		this.color = color;
	}

	@Override
	public void readUpdateInfo(int id, PacketBuffer buffer) {
		if (id == 1) {
			usNameText.clear();
			if (localizedNameText != null)
				localizedNameText.clear();
			int count = buffer.readVarInt();

			for (int i = 0; i < count; ++i) {
				String jsonText = buffer.readString(32767);
				usNameText.add(ITextComponent.Serializer.jsonToComponent(jsonText));
			}
			if (LocalizedName.showLocalize() && ConfigHolder.client.GregTechCEu.showLocalizedName) {
				if (count == 1 && usNameText.get(0) instanceof TextComponentTranslation) {
					TextComponentTranslation tct = (TextComponentTranslation) usNameText.get(0);
					if (tct.getFormatArgs().length == 0) {
						if (localizedNameText == null)
							localizedNameText = new ArrayList<>();
						localizedNameText.add(tct.createCopy());
					}
				}
			}

			formatDisplayText();
			updateComponentTextSize();
		}

	}

	@Override
	public AdvancedTextWidget setMaxWidthLimit(int maxWidthLimit) {
		this.maxWidthLimit = maxWidthLimit;
		if (isClientSide()) {
			updateComponentTextSize();
		}
		return this;
	}

	@Override
	protected ITextComponent getTextUnderMouse(int mouseX, int mouseY) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		Position position = getPosition();
		int selectedLine = (mouseY - position.y) / (fontRenderer.FONT_HEIGHT + 2);
		if (mouseX < position.x)
			return null;
		if (selectedLine >= 0 && selectedLine < usNameText.size()) {
			ITextComponent selectedComponent = usNameText.get(selectedLine);
			int mouseOffset = mouseX - position.x;
			int currentOffset = 0;

			for (ITextComponent lineComponent : selectedComponent) {
				currentOffset += fontRenderer.getStringWidth(lineComponent.getUnformattedComponentText());
				if (currentOffset >= mouseOffset) {
					return lineComponent;
				}
			}
		}
		if (localizedNameText != null) {
			selectedLine -= usNameText.size();
			if (selectedLine >= 0 && selectedLine < localizedNameText.size()) {
				ITextComponent selectedComponent = localizedNameText.get(selectedLine);
				int mouseOffset = mouseX - position.x;
				int currentOffset = 0;

				for (ITextComponent lineComponent : selectedComponent) {
					currentOffset += fontRenderer.getStringWidth(lineComponent.getUnformattedComponentText());
					if (currentOffset >= mouseOffset) {
						return lineComponent;
					}
				}
			}
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	private void updateComponentTextSize() {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		int maxStringWidth = 0;
		int totalHeight = 0;

		for (ITextComponent textComponent : usNameText) {
			maxStringWidth = Math.max(maxStringWidth, fontRenderer.getStringWidth(textComponent.getFormattedText()));
			totalHeight += fontRenderer.FONT_HEIGHT + 2;
		}
		if (localizedNameText != null) {
			for (ITextComponent textComponent : localizedNameText) {
				maxStringWidth = Math.max(maxStringWidth, fontRenderer.getStringWidth(textComponent.getFormattedText()));
				totalHeight += fontRenderer.FONT_HEIGHT + 2;
			}
		}

		totalHeight -= 2;
		setSize(new Size(maxStringWidth, totalHeight));
		if (uiAccess != null) {
			uiAccess.notifySizeChange();
		}

	}

	@SideOnly(Side.CLIENT)
	private void formatDisplayText() {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		int maxTextWidthResult = maxWidthLimit == 0 ? Integer.MAX_VALUE : maxWidthLimit;
		synchronized (LocalizedName.langmapus.getDisplayNameLock) {
			LocalizedName.langmapus.getDisplayNameThread = Thread.currentThread();
			try {
				usNameText = usNameText.stream().flatMap((c) -> {
					return GuiUtilRenderComponents.splitText(c, maxTextWidthResult, fontRenderer, true, true).stream();
				}).collect(Collectors.toList());
			} finally {
				LocalizedName.langmapus.getDisplayNameThread = null;
			}
		}
		if (localizedNameText != null) {
			localizedNameText = localizedNameText.stream().flatMap((c) -> {
				return GuiUtilRenderComponents.splitText(c, maxTextWidthResult, fontRenderer, true, true).stream();
			}).collect(Collectors.toList());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawInBackground(int mouseX, int mouseY, float partialTicks, IRenderContext context) {
		super.drawInBackground(mouseX, mouseY, partialTicks, context);
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		Position position = getPosition();

		int y = position.y;
		for (ITextComponent iTextComponent : usNameText) {
			fontRenderer.drawString(iTextComponent.getFormattedText(), position.x, y, color);
			y += fontRenderer.FONT_HEIGHT + 2;
		}
		if (localizedNameText != null) {
			for (ITextComponent iTextComponent : localizedNameText) {
				fontRenderer.drawString(iTextComponent.getFormattedText(), position.x, y, color);
				y += fontRenderer.FONT_HEIGHT + 2;
			}
		}

	}

}
