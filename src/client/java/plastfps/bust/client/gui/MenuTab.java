package plastfps.bust.client.gui;

public enum MenuTab {
	VISUALS("plastfps.screen.visuals", "plastfps.tab.visuals"),
	HUD("plastfps.screen.hud", "plastfps.tab.hud"),
	GPS("plastfps.screen.gps", "plastfps.tab.gps");

	private final String titleKey;
	private final String tabKey;

	MenuTab(String titleKey, String tabKey) {
		this.titleKey = titleKey;
		this.tabKey = tabKey;
	}

	public String titleKey() {
		return titleKey;
	}

	public String tabKey() {
		return tabKey;
	}
}
