package plastfps.bust.client.hud;

import net.minecraft.client.gui.GuiGraphics;
import plastfps.bust.client.config.ClientConfig;

public final class HudPositions {
	private HudPositions() {
	}

	public static int getX(HudElement element, GuiGraphics graphics) {
		return getX(element, graphics.guiWidth(), graphics.guiHeight());
	}

	public static int getY(HudElement element, GuiGraphics graphics) {
		return getY(element, graphics.guiWidth(), graphics.guiHeight());
	}

	public static int getX(HudElement element, int screenWidth, int screenHeight) {
		return switch (element) {
			case ARMOUR -> resolve(ClientConfig.getArmourHudX(), defaultArmourX(screenWidth));
			case POTION -> resolve(ClientConfig.getPotionHudX(), defaultPotionX(screenWidth));
			case INVENTORY -> resolve(ClientConfig.getInventoryHudX(), defaultInventoryX(screenWidth));
			case COOLDOWN -> resolve(ClientConfig.getCooldownHudX(), defaultCooldownX(screenWidth));
		};
	}

	public static int getY(HudElement element, int screenWidth, int screenHeight) {
		return switch (element) {
			case ARMOUR -> resolve(ClientConfig.getArmourHudY(), defaultArmourY(screenHeight));
			case POTION -> resolve(ClientConfig.getPotionHudY(), defaultPotionY(screenHeight));
			case INVENTORY -> resolve(ClientConfig.getInventoryHudY(), defaultInventoryY(screenHeight));
			case COOLDOWN -> resolve(ClientConfig.getCooldownHudY(), defaultCooldownY(screenHeight));
		};
	}

	public static void setPosition(HudElement element, int x, int y) {
		switch (element) {
			case ARMOUR -> {
				ClientConfig.setArmourHudX(x);
				ClientConfig.setArmourHudY(y);
			}
			case POTION -> {
				ClientConfig.setPotionHudX(x);
				ClientConfig.setPotionHudY(y);
			}
			case INVENTORY -> {
				ClientConfig.setInventoryHudX(x);
				ClientConfig.setInventoryHudY(y);
			}
			case COOLDOWN -> {
				ClientConfig.setCooldownHudX(x);
				ClientConfig.setCooldownHudY(y);
			}
		}
		ClientConfig.save();
	}

	public static void clampToScreen(HudElement element, int x, int y, int screenWidth, int screenHeight) {
		int maxX = Math.max(0, screenWidth - element.defaultWidth());
		int maxY = Math.max(0, screenHeight - element.defaultHeight());
		setPosition(element, Math.clamp(x, 0, maxX), Math.clamp(y, 0, maxY));
	}

	private static int resolve(int saved, int fallback) {
		return saved >= 0 ? saved : fallback;
	}

	private static int defaultArmourX(int screenWidth) {
		return screenWidth / 2 - 91 - 76;
	}

	private static int defaultArmourY(int screenHeight) {
		return screenHeight - 22;
	}

	private static int defaultPotionX(int screenWidth) {
		return screenWidth - 118;
	}

	private static int defaultPotionY(int screenHeight) {
		return 8;
	}

	private static int defaultInventoryX(int screenWidth) {
		return 8;
	}

	private static int defaultInventoryY(int screenHeight) {
		return 8;
	}

	private static int defaultCooldownX(int screenWidth) {
		return screenWidth / 2 + 96;
	}

	private static int defaultCooldownY(int screenHeight) {
		return screenHeight - 28;
	}
}
