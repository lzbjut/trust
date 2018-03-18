import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * MonitorVehicleTracker
 * <p/>
 * Monitor-based vehicle tracker implementation
 *
 * @author Brian Goetz and Tim Peierls
 */
 public class MonitorVehicleTracker {
    private final Map<String, MutablePoint> locations;

    public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
        this.locations =deepCopy(locations);
    }

    public synchronized Map<String, MutablePoint> getLocations() {
        return locations;
    }

    public synchronized MutablePoint getLocation(String id) {
        MutablePoint loc = locations.get(id);
        return loc == null ? null : new MutablePoint(loc);
    }

    public synchronized void setLocation(String id, int x, int y) {
        MutablePoint loc = locations.get(id);
        if (loc == null)
            throw new IllegalArgumentException("No such ID: " + id);
        loc.x = x;
        loc.y = y;
    }

    private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> m) {
        Map<String, MutablePoint> result = new HashMap<String, MutablePoint>();

        for (String id : m.keySet())
            result.put(id, new MutablePoint(m.get(id)));

        return Collections.unmodifiableMap(result);
    }

    public static void main(String qrgs[]){
        Map<String, MutablePoint> locations=new HashMap<String,MutablePoint>();

        MonitorVehicleTracker monitorVehicleTracker=new MonitorVehicleTracker(locations);
        Map<String, MutablePoint> locations1=monitorVehicleTracker.getLocations();
        System.out.println(locations==locations1);

        locations1=monitorVehicleTracker.getLocations();
        locations1.put("test",new MutablePoint());
        locations1=monitorVehicleTracker.getLocations();
        System.out.println(locations1);
        AtomicLong as=new AtomicLong(213213);

    }
}

