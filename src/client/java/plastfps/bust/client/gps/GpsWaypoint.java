package plastfps.bust.client.gps;

public final class GpsWaypoint {
	public boolean active;
	public int x;
	public int y;
	public int z;
	public String dimension = "";

	public GpsWaypoint copy() {
		GpsWaypoint w = new GpsWaypoint();
		w.active = this.active;
		w.x = this.x;
		w.y = this.y;
		w.z = this.z;
		w.dimension = this.dimension;
		return w;
	}
}
