package plastfps.bust.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import plastfps.bust.client.config.ClientConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class PotionHudRenderer {
	private PotionHudRenderer() {
	}

	public static void render(GuiGraphics graphics) {
		if (!ClientConfig.isPotionHud()) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) {
			return;
		}

		List<MobEffectInstance> effects = new ArrayList<>(mc.player.getActiveEffects());
		if (effects.isEmpty()) {
			return;
		}
		effects.sort(Comparator.comparingInt(MobEffectInstance::getDuration).reversed());

		int x = HudPositions.getX(HudElement.POTION, graphics);
		int y = HudPositions.getY(HudElement.POTION, graphics);
		int lineY = y;

		graphics.fill(x - 4, y - 4, x + 106, y + Math.min(effects.size(), 8) * 12 + 4, 0x60000000);

		for (MobEffectInstance effect : effects) {
			if (lineY > y + 110) {
				break;
			}
			Component name = effect.getEffect().value().getDisplayName();
			if (effect.getAmplifier() > 0) {
				name = name.copy().append(" ").append(Component.literal(String.valueOf(effect.getAmplifier() + 1)));
			}
			int color = effect.getEffect().value().getColor();
			graphics.drawString(mc.font, name, x, lineY, color | 0xFF000000, true);
			String time = formatDuration(effect);
			graphics.drawString(mc.font, time, x + 72, lineY, 0xFFCCCCCC, true);
			lineY += 12;
		}
	}

	private static String formatDuration(MobEffectInstance effect) {
		if (effect.isInfiniteDuration()) {
			return "∞";
		}
		int ticks = effect.getDuration();
		int totalSeconds = ticks / 20;
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;
		if (minutes > 0) {
			return minutes + ":" + String.format("%02d", seconds);
		}
		return seconds + "s";
	}
}
