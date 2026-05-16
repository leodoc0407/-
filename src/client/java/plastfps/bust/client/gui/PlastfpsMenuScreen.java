package plastfps.bust.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import plastfps.bust.client.config.ClientConfig;
import plastfps.bust.client.gps.GpsManager;
import plastfps.bust.client.gps.GpsWaypoint;
import plastfps.bust.client.util.ColorUtil;

public class PlastfpsMenuScreen extends Screen {
	private final Screen parent;
	private final MenuTab tab;

	public PlastfpsMenuScreen(Screen parent) {
		this(parent, MenuTab.VISUALS);
	}

	public PlastfpsMenuScreen(Screen parent, MenuTab tab) {
		super(Component.translatable(tab.titleKey()));
		this.parent = parent;
		this.tab = tab;
	}

	@Override
	protected void init() {
		super.init();
		int centerX = this.width / 2;
		int tabY = 28;
		int rowY = tabY + 28;
		int boxLeft = centerX - 160;
		int maxWidth = 320;

		int tabCount = MenuTab.values().length;
		int tabWidth = Math.min(96, 288 / tabCount);
		int tabsLeft = centerX - (tabWidth * tabCount) / 2;
		for (MenuTab value : MenuTab.values()) {
			boolean active = value == this.tab;
			MenuTab target = value;
			Button tabButton = Button.builder(Component.translatable(value.tabKey()), b ->
				Minecraft.getInstance().setScreen(new PlastfpsMenuScreen(this.parent, target))
			).bounds(tabsLeft, tabY, tabWidth - 2, 20).build();
			tabButton.active = !active;
			this.addRenderableWidget(tabButton);
			tabsLeft += tabWidth;
		}

		rowY = switch (this.tab) {
			case VISUALS -> initVisualsTab(boxLeft, rowY, maxWidth);
			case HUD -> initHudTab(boxLeft, rowY, maxWidth);
			case GPS -> initGpsTab(boxLeft, rowY, maxWidth);
		};

		int doneY = Math.min(rowY + 12, this.height - 28);
		this.addRenderableWidget(
			Button.builder(CommonComponents.GUI_DONE, b -> Minecraft.getInstance().setScreen(this.parent))
				.bounds(centerX - 50, doneY, 100, 20)
				.build()
		);
	}

	private int initVisualsTab(int boxLeft, int rowY, int maxWidth) {
		rowY = addToggle(boxLeft, rowY, maxWidth, "plastfps.option.fps_boost", "plastfps.option.fps_boost.tooltip",
			ClientConfig.isFpsBoost(), ClientConfig::setFpsBoost);
		rowY = addToggle(boxLeft, rowY, maxWidth, "plastfps.option.full_bright", "plastfps.option.full_bright.tooltip",
			ClientConfig.isFullBright(), ClientConfig::setFullBright);
		rowY = addToggle(boxLeft, rowY, maxWidth, "plastfps.option.world_customizer", "plastfps.option.world_customizer.tooltip",
			ClientConfig.isWorldCustomizer(), checked -> {
				ClientConfig.setWorldCustomizer(checked);
				rebuild();
			});
		if (ClientConfig.isWorldCustomizer()) {
			rowY = addColorField(boxLeft, rowY, maxWidth, "plastfps.field.sky_color", ClientConfig.getSkyColor(),
				ClientConfig::setSkyColor);
		}
		rowY = addToggle(boxLeft, rowY, maxWidth, "plastfps.option.aspect_ratio", "plastfps.option.aspect_ratio.tooltip",
			ClientConfig.isAspectRatio(), checked -> {
				ClientConfig.setAspectRatio(checked);
				rebuild();
			});
		if (ClientConfig.isAspectRatio()) {
			float normalized = (ClientConfig.getAspectStretch() - 0.5f) / 1.5f;
			AbstractSliderButton aspectSlider = new AbstractSliderButton(boxLeft, rowY, maxWidth, 20,
				Component.empty(), normalized) {
				@Override
				protected void updateMessage() {
					float stretch = 0.5f + (float) this.value * 1.5f;
					setMessage(Component.translatable("plastfps.slider.aspect", String.format("%.2f", stretch)));
				}

				@Override
				protected void applyValue() {
					ClientConfig.setAspectStretch(0.5f + (float) this.value * 1.5f);
					ClientConfig.save();
				}
			};
			aspectSlider.setTooltip(Tooltip.create(Component.translatable("plastfps.option.aspect_ratio.tooltip")));
			this.addRenderableWidget(aspectSlider);
			rowY += 24;
		}
		rowY = addToggle(boxLeft, rowY, maxWidth, "plastfps.option.jump_circle", "plastfps.option.jump_circle.tooltip",
			ClientConfig.isJumpCircle(), checked -> {
				ClientConfig.setJumpCircle(checked);
				rebuild();
			});
		if (ClientConfig.isJumpCircle()) {
			rowY = addColorField(boxLeft, rowY, maxWidth, "plastfps.field.jump_circle_color", ClientConfig.getJumpCircleColor(),
				ClientConfig::setJumpCircleColor);
		}
		rowY = addToggle(boxLeft, rowY, maxWidth, "plastfps.option.target_hud", "plastfps.option.target_hud.tooltip",
			ClientConfig.isTargetHud(), ClientConfig::setTargetHud);
		rowY = addToggle(boxLeft, rowY, maxWidth, "plastfps.option.inventory_help", "plastfps.option.inventory_help.tooltip",
			ClientConfig.isInventoryHelp(), ClientConfig::setInventoryHelp);
		rowY = addToggle(boxLeft, rowY, maxWidth, "plastfps.option.item_name", "plastfps.option.item_name.tooltip",
			ClientConfig.isItemName(), ClientConfig::setItemName);
		return rowY;
	}

	private int initGpsTab(int boxLeft, int rowY, int maxWidth) {
		Minecraft mc = Minecraft.getInstance();
		GpsWaypoint existing = ClientConfig.getGpsWaypoint();
		int defaultX = existing.active ? existing.x : (mc.player != null ? (int) Math.floor(mc.player.getX()) : 0);
		int defaultY = existing.active ? existing.y : (mc.player != null ? (int) Math.floor(mc.player.getY()) : 64);
		int defaultZ = existing.active ? existing.z : (mc.player != null ? (int) Math.floor(mc.player.getZ()) : 0);

		int fieldW = 72;
		EditBox xField = new EditBox(this.font, boxLeft + 18, rowY, fieldW, 18, Component.literal("X"));
		xField.setMaxLength(6);
		xField.setValue(String.valueOf(defaultX));
		xField.setFilter(s -> s.isEmpty() || s.equals("-") || s.matches("-?\\d*"));
		this.addRenderableWidget(xField);

		EditBox yField = new EditBox(this.font, boxLeft + 18 + fieldW + 28, rowY, fieldW, 18, Component.literal("Y"));
		yField.setMaxLength(6);
		yField.setValue(String.valueOf(defaultY));
		yField.setFilter(s -> s.isEmpty() || s.equals("-") || s.matches("-?\\d*"));
		this.addRenderableWidget(yField);

		EditBox zField = new EditBox(this.font, boxLeft + 18 + (fieldW + 28) * 2, rowY, fieldW, 18, Component.literal("Z"));
		zField.setMaxLength(6);
		zField.setValue(String.valueOf(defaultZ));
		zField.setFilter(s -> s.isEmpty() || s.equals("-") || s.matches("-?\\d*"));
		this.addRenderableWidget(zField);

		this.addRenderableWidget(
			Button.builder(Component.literal("X"), b -> xField.setFocused(true))
				.bounds(boxLeft, rowY, 16, 20).build()
		);
		this.addRenderableWidget(
			Button.builder(Component.literal("Y"), b -> yField.setFocused(true))
				.bounds(boxLeft + fieldW + 28, rowY, 16, 20).build()
		);
		this.addRenderableWidget(
			Button.builder(Component.literal("Z"), b -> zField.setFocused(true))
				.bounds(boxLeft + (fieldW + 28) * 2, rowY, 16, 20).build()
		);

		rowY += 28;

		this.addRenderableWidget(
			Button.builder(Component.translatable("plastfps.gps.use_current"), b -> {
				if (mc.player != null) {
					xField.setValue(String.valueOf((int) Math.floor(mc.player.getX())));
					yField.setValue(String.valueOf((int) Math.floor(mc.player.getY())));
					zField.setValue(String.valueOf((int) Math.floor(mc.player.getZ())));
				}
			}).bounds(boxLeft, rowY, maxWidth, 20).build()
		);
		rowY += 24;

		this.addRenderableWidget(
			Button.builder(Component.translatable("plastfps.gps.create"), b -> {
				Integer x = parseCoord(xField.getValue());
				Integer y = parseCoord(yField.getValue());
				Integer z = parseCoord(zField.getValue());
				if (x != null && y != null && z != null) {
					GpsManager.setMarker(x, y, z);
					rebuild();
				}
			}).bounds(boxLeft, rowY, maxWidth, 20).build()
		);
		rowY += 24;

		Button deleteButton = Button.builder(Component.translatable("plastfps.gps.delete"), b -> {
			GpsManager.clearMarker();
			rebuild();
		}).bounds(boxLeft, rowY, maxWidth, 20).build();
		deleteButton.active = existing.active;
		this.addRenderableWidget(deleteButton);
		return rowY + 24;
	}

	private static Integer parseCoord(String value) {
		if (value == null || value.isEmpty() || value.equals("-")) {
			return null;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private int initHudTab(int boxLeft, int rowY, int maxWidth) {
		rowY = addToggle(boxLeft, rowY, maxWidth, "plastfps.option.armour_hud", "plastfps.option.armour_hud.tooltip",
			ClientConfig.isArmourHud(), ClientConfig::setArmourHud);
		rowY = addToggle(boxLeft, rowY, maxWidth, "plastfps.option.potion_hud", "plastfps.option.potion_hud.tooltip",
			ClientConfig.isPotionHud(), ClientConfig::setPotionHud);
		rowY = addToggle(boxLeft, rowY, maxWidth, "plastfps.option.inventory_hud", "plastfps.option.inventory_hud.tooltip",
			ClientConfig.isInventoryHud(), ClientConfig::setInventoryHud);
		rowY = addToggle(boxLeft, rowY, maxWidth, "plastfps.option.cooldown_hud", "plastfps.option.cooldown_hud.tooltip",
			ClientConfig.isCooldownHud(), ClientConfig::setCooldownHud);
		rowY = addToggle(boxLeft, rowY, maxWidth, "plastfps.option.client_hud", "plastfps.option.client_hud.tooltip",
			ClientConfig.isClientHud(), ClientConfig::setClientHud);
		return rowY;
	}

	private int addToggle(int x, int y, int width, String labelKey, String tooltipKey, boolean checked, ToggleHandler handler) {
		Checkbox checkbox = new Checkbox(x, y, width, Component.translatable(labelKey), this.font, checked,
			(box, value) -> {
				handler.apply(value);
				ClientConfig.save();
			});
		checkbox.setTooltip(Tooltip.create(Component.translatable(tooltipKey)));
		this.addRenderableWidget(checkbox);
		return y + 22;
	}

	private int addColorField(int x, int y, int width, String labelKey, int currentColor, ColorHandler handler) {
		EditBox field = new EditBox(this.font, x + 110, y - 2, width - 110, 18, Component.translatable(labelKey));
		field.setMaxLength(7);
		field.setValue(ColorUtil.toHexRgb(currentColor));
		field.setHint(Component.literal("RRGGBB"));
		field.setResponder(text -> {
			int parsed = ColorUtil.parseHexRgb(text);
			if (parsed >= 0) {
				handler.apply(parsed);
				ClientConfig.save();
			}
		});
		this.addRenderableWidget(field);
		this.addRenderableWidget(
			Button.builder(Component.translatable(labelKey), b -> field.setFocused(true))
				.bounds(x, y, 108, 20)
				.build()
		);
		return y + 24;
	}

	private void rebuild() {
		ClientConfig.save();
		Minecraft.getInstance().setScreen(new PlastfpsMenuScreen(this.parent, this.tab));
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 12, 0xFFFFFFFF);
		if (this.tab == MenuTab.HUD) {
			guiGraphics.drawCenteredString(
				this.font,
				Component.translatable("plastfps.hud.drag_hint"),
				this.width / 2,
				this.height - 42,
				0xFFAAAAAA
			);
		}
		if (this.tab == MenuTab.GPS && ClientConfig.getGpsWaypoint().active) {
			GpsWaypoint waypoint = ClientConfig.getGpsWaypoint();
			guiGraphics.drawCenteredString(
				this.font,
				Component.translatable("plastfps.gps.active", waypoint.x, waypoint.y, waypoint.z),
				this.width / 2,
				this.height - 42,
				0xFF55FFAA
			);
		}
	}

	@FunctionalInterface
	private interface ToggleHandler {
		void apply(boolean value);
	}

	@FunctionalInterface
	private interface ColorHandler {
		void apply(int rgb);
	}
}
