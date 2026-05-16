package plastfps.bust.client.util;

public final class ColorUtil {
	private ColorUtil() {
	}

	public static int parseHexRgb(String text) {
		if (text == null) {
			return -1;
		}
		String s = text.trim();
		if (s.startsWith("#")) {
			s = s.substring(1);
		}
		if (s.length() != 6) {
			return -1;
		}
		try {
			return Integer.parseInt(s, 16) & 0xFFFFFF;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public static String toHexRgb(int rgb) {
		return String.format("%06X", rgb & 0xFFFFFF);
	}
}
