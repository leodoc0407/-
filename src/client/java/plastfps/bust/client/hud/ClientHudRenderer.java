package plastfps.bust.client.hud;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import plastfps.bust.client.config.ClientConfig;

public final class ClientHudRenderer {
	private static final Component TITLE = Component.literal("plast client boostit")
		.withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD);

	private ClientHudRenderer() {
	}

	public static void render(GuiGraphics graphics) {
		if (!ClientConfig.isClientHud()) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) {
			return;
		}
		int centerX = graphics.guiWidth() / 2;
		int y = Math.max(24, (int) (graphics.guiHeight() * 0.11f));
		graphics.drawCenteredString(mc.font, TITLE, centerX, y, 0xFFFFFFFF);
	}
}
