package plastfps.bust.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import org.lwjgl.glfw.GLFW;

import plastfps.bust.client.config.ClientConfig;
import plastfps.bust.client.feature.GroundItemRenderer;
import plastfps.bust.client.feature.InventoryHelpHudOverlay;
import plastfps.bust.client.feature.JumpCircleManager;
import plastfps.bust.client.feature.JumpCircleRenderer;
import plastfps.bust.client.feature.TargetHudTracker;
import plastfps.bust.client.gui.PlastfpsMenuScreen;
import plastfps.bust.client.gps.GpsHudRenderer;
import plastfps.bust.client.gps.GpsWorldRenderer;
import plastfps.bust.client.hud.HudDragManager;
import plastfps.bust.client.hud.HudRenderer;

public class PlastfpsPvpClient implements ClientModInitializer {
	private static KeyMapping openMenuKey;
	private static boolean fullBrightActive;
	private static double gammaBeforeFullBright = 1.0;

	@Override
	public void onInitializeClient() {
		ClientConfig.load();

		openMenuKey = KeyBindingHelper.registerKeyBinding(
			new KeyMapping("key.plastfps.open_menu", GLFW.GLFW_KEY_INSERT, KeyMapping.CATEGORY_MISC)
		);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openMenuKey.consumeClick()) {
				Minecraft mc = Minecraft.getInstance();
				mc.setScreen(new PlastfpsMenuScreen(mc.screen));
			}

			applyFullBright(client);

			if (client.player != null) {
				if (ClientConfig.isJumpCircle()) {
					JumpCircleManager.tick(client.player);
				}
				TargetHudTracker.tick(client);
			}
		});

		WorldRenderEvents.LAST.register(context -> {
			JumpCircleRenderer.render(context);
			GroundItemRenderer.render(context);
			GpsWorldRenderer.render(context);
		});

		HudRenderCallback.EVENT.register((graphics, tickCounter) -> {
			InventoryHelpHudOverlay.render(graphics);
			float partialTick = tickCounter.getGameTimeDeltaPartialTick(false);
			HudRenderer.render(graphics, partialTick);
			GpsHudRenderer.render(graphics);
		});

		ScreenMouseEvents.beforeMouseClick(ChatScreen.class).register((screen, mouseX, mouseY, button) ->
			HudDragManager.handleClick(mouseX, mouseY, button)
		);
		ScreenMouseEvents.beforeMouseDrag(ChatScreen.class).register((screen, mouseX, mouseY, button, dragX, dragY) ->
			HudDragManager.handleDrag(mouseX, mouseY)
		);
		ScreenMouseEvents.beforeMouseRelease(ChatScreen.class).register((screen, mouseX, mouseY, button) ->
			HudDragManager.handleRelease()
		);
	}

	private static void applyFullBright(Minecraft client) {
		boolean want = ClientConfig.isFullBright();
		if (want) {
			if (!fullBrightActive) {
				gammaBeforeFullBright = client.options.gamma().get();
				fullBrightActive = true;
			}
			client.options.gamma().set(16.0);
		} else {
			if (fullBrightActive) {
				client.options.gamma().set(gammaBeforeFullBright);
				fullBrightActive = false;
			}
		}
	}
}
