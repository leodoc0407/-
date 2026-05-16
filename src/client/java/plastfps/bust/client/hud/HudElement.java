package plastfps.bust.client.hud;

public enum HudElement {
	ARMOUR(72, 20),
	POTION(110, 120),
	INVENTORY(9 * 18 + 8, 3 * 18 + 8 + 18 + 6),
	COOLDOWN(9 * 20, 24);

	private final int defaultWidth;
	private final int defaultHeight;

	HudElement(int defaultWidth, int defaultHeight) {
		this.defaultWidth = defaultWidth;
		this.defaultHeight = defaultHeight;
	}

	public int defaultWidth() {
		return defaultWidth;
	}

	public int defaultHeight() {
		return defaultHeight;
	}
}
