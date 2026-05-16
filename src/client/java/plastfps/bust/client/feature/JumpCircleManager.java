package plastfps.bust.client.feature;

import net.minecraft.client.player.LocalPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class JumpCircleManager {
	private static final long CIRCLE_LIFETIME_MS = 900L;
	private static final List<JumpCircle> CIRCLES = new ArrayList<>();
	private static boolean wasOnGround = true;

	private JumpCircleManager() {
	}

	public static void tick(LocalPlayer player) {
		long now = System.currentTimeMillis();
		Iterator<JumpCircle> it = CIRCLES.iterator();
		while (it.hasNext()) {
			if (now - it.next().spawnTimeMs() > CIRCLE_LIFETIME_MS) {
				it.remove();
			}
		}

		boolean onGround = player.onGround();
		if (wasOnGround && !onGround && player.getDeltaMovement().y > 0.08) {
			CIRCLES.add(new JumpCircle(player.getX(), player.getY(), player.getZ(), now));
		}
		wasOnGround = onGround;
	}

	public static List<JumpCircle> circles() {
		return CIRCLES;
	}

	public record JumpCircle(double x, double y, double z, long spawnTimeMs) {
	}
}
