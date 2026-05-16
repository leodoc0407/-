package plastfps.bust.client.config;



import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;
import plastfps.bust.client.gps.GpsWaypoint;



import java.io.IOException;

import java.io.Reader;

import java.io.Writer;

import java.nio.file.Files;

import java.nio.file.Path;



public final class ClientConfig {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("plastfps-pvp-client.json");



	public boolean fpsBoost;

	public boolean fullBright;

	public boolean worldCustomizer;

	public int skyColor = 0x78A7FF;

	public boolean aspectRatio;

	public float aspectStretch = 1.0f;

	public boolean jumpCircle;

	public int jumpCircleColor = 0x00FFAA;

	public boolean targetHud;

	public boolean inventoryHelp;

	public boolean itemName;

	public boolean armourHud;
	public boolean potionHud;
	public boolean inventoryHud;
	public boolean cooldownHud;

	public Boolean clientHud = Boolean.TRUE;

	public int armourHudX = -1;
	public int armourHudY = -1;
	public int potionHudX = -1;
	public int potionHudY = -1;
	public int inventoryHudX = -1;
	public int inventoryHudY = -1;
	public int cooldownHudX = -1;
	public int cooldownHudY = -1;

	public GpsWaypoint gpsWaypoint = new GpsWaypoint();

	private static ClientConfig instance = defaults();



	private static ClientConfig defaults() {

		ClientConfig c = new ClientConfig();

		c.fpsBoost = false;

		c.fullBright = false;

		c.worldCustomizer = false;

		c.skyColor = 0x78A7FF;

		c.aspectRatio = false;

		c.aspectStretch = 1.0f;

		c.jumpCircle = false;

		c.jumpCircleColor = 0x00FFAA;

		c.targetHud = false;

		c.inventoryHelp = false;

		c.itemName = false;
		c.armourHud = false;
		c.potionHud = false;
		c.inventoryHud = false;
		c.cooldownHud = false;
		c.clientHud = Boolean.TRUE;
		c.armourHudX = -1;
		c.armourHudY = -1;
		c.potionHudX = -1;
		c.potionHudY = -1;
		c.inventoryHudX = -1;
		c.inventoryHudY = -1;
		c.cooldownHudX = -1;
		c.cooldownHudY = -1;
		c.gpsWaypoint = new GpsWaypoint();

		return c;

	}



	public static void load() {

		if (!Files.isRegularFile(FILE)) {

			instance = defaults();

			save();

			return;

		}

		try (Reader reader = Files.newBufferedReader(FILE)) {

			ClientConfig loaded = GSON.fromJson(reader, ClientConfig.class);

			if (loaded == null) {

				instance = defaults();

			} else {

				instance = loaded;

				clampValues();

			}

		} catch (IOException e) {

			instance = defaults();

		}

	}



	public static void save() {

		clampValues();

		try {

			Files.createDirectories(FILE.getParent());

			try (Writer writer = Files.newBufferedWriter(FILE)) {

				GSON.toJson(instance, writer);

			}

		} catch (IOException ignored) {

		}

	}



	private static void clampValues() {

		instance.aspectStretch = Math.clamp(instance.aspectStretch, 0.5f, 2.0f);

		instance.skyColor = instance.skyColor & 0xFFFFFF;

		instance.jumpCircleColor = instance.jumpCircleColor & 0xFFFFFF;

	}



	public static boolean isFpsBoost() {

		return instance.fpsBoost;

	}



	public static void setFpsBoost(boolean value) {

		instance.fpsBoost = value;

	}



	public static boolean isFullBright() {

		return instance.fullBright;

	}



	public static void setFullBright(boolean value) {

		instance.fullBright = value;

	}



	public static boolean isWorldCustomizer() {

		return instance.worldCustomizer;

	}



	public static void setWorldCustomizer(boolean value) {

		instance.worldCustomizer = value;

	}



	public static int getSkyColor() {

		return instance.skyColor & 0xFFFFFF;

	}



	public static void setSkyColor(int rgb) {

		instance.skyColor = rgb & 0xFFFFFF;

	}



	public static boolean isAspectRatio() {

		return instance.aspectRatio;

	}



	public static void setAspectRatio(boolean value) {

		instance.aspectRatio = value;

	}



	public static float getAspectStretch() {

		return instance.aspectStretch;

	}



	public static void setAspectStretch(float value) {

		instance.aspectStretch = Math.clamp(value, 0.5f, 2.0f);

	}



	public static boolean isJumpCircle() {

		return instance.jumpCircle;

	}



	public static void setJumpCircle(boolean value) {

		instance.jumpCircle = value;

	}



	public static int getJumpCircleColor() {

		return instance.jumpCircleColor & 0xFFFFFF;

	}



	public static void setJumpCircleColor(int rgb) {

		instance.jumpCircleColor = rgb & 0xFFFFFF;

	}



	public static boolean isTargetHud() {

		return instance.targetHud;

	}



	public static void setTargetHud(boolean value) {

		instance.targetHud = value;

	}

	public static boolean isInventoryHelp() {

		return instance.inventoryHelp;

	}

	public static void setInventoryHelp(boolean value) {

		instance.inventoryHelp = value;

	}

	public static boolean isItemName() {

		return instance.itemName;

	}

	public static void setItemName(boolean value) {
		instance.itemName = value;
	}

	public static boolean isArmourHud() {
		return instance.armourHud;
	}

	public static void setArmourHud(boolean value) {
		instance.armourHud = value;
	}

	public static boolean isPotionHud() {
		return instance.potionHud;
	}

	public static void setPotionHud(boolean value) {
		instance.potionHud = value;
	}

	public static boolean isInventoryHud() {
		return instance.inventoryHud;
	}

	public static void setInventoryHud(boolean value) {
		instance.inventoryHud = value;
	}

	public static boolean isCooldownHud() {
		return instance.cooldownHud;
	}

	public static void setCooldownHud(boolean value) {
		instance.cooldownHud = value;
	}

	public static boolean isClientHud() {
		return instance.clientHud == null || instance.clientHud;
	}

	public static void setClientHud(boolean value) {
		instance.clientHud = value;
	}

	public static int getArmourHudX() {
		return instance.armourHudX;
	}

	public static int getArmourHudY() {
		return instance.armourHudY;
	}

	public static void setArmourHudX(int value) {
		instance.armourHudX = value;
	}

	public static void setArmourHudY(int value) {
		instance.armourHudY = value;
	}

	public static int getPotionHudX() {
		return instance.potionHudX;
	}

	public static int getPotionHudY() {
		return instance.potionHudY;
	}

	public static void setPotionHudX(int value) {
		instance.potionHudX = value;
	}

	public static void setPotionHudY(int value) {
		instance.potionHudY = value;
	}

	public static int getInventoryHudX() {
		return instance.inventoryHudX;
	}

	public static int getInventoryHudY() {
		return instance.inventoryHudY;
	}

	public static void setInventoryHudX(int value) {
		instance.inventoryHudX = value;
	}

	public static void setInventoryHudY(int value) {
		instance.inventoryHudY = value;
	}

	public static int getCooldownHudX() {
		return instance.cooldownHudX;
	}

	public static int getCooldownHudY() {
		return instance.cooldownHudY;
	}

	public static void setCooldownHudX(int value) {
		instance.cooldownHudX = value;
	}

	public static void setCooldownHudY(int value) {
		instance.cooldownHudY = value;
	}

	public static GpsWaypoint getGpsWaypoint() {
		if (instance.gpsWaypoint == null) {
			instance.gpsWaypoint = new GpsWaypoint();
		}
		return instance.gpsWaypoint;
	}

	public static void setGpsWaypoint(GpsWaypoint waypoint) {
		instance.gpsWaypoint = waypoint == null ? new GpsWaypoint() : waypoint;
	}
}


