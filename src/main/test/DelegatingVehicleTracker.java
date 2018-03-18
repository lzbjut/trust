import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * DelegatingVehicleTracker
 * <p/>
 * Delegating thread safety to a ConcurrentHashMap
 *
 * @author Brian Goetz and Tim Peierls
 */
public class DelegatingVehicleTracker {
    private final ConcurrentMap<String, Point> locations;
    private final Map<String, Point> unmodifiableMap;

    public DelegatingVehicleTracker(Map<String, Point> points) {
        locations = new ConcurrentHashMap<String, Point>(points);
        unmodifiableMap = Collections.unmodifiableMap(locations);
    }

    public Map<String, Point> getLocations() {
        return unmodifiableMap;
    }

    public Point getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        locations.put(id, new Point(x, y));
    }
    Vector<Integer> sada=new Vector<>();

    // Alternate version of getLocations (Listing 4.8)
    public Map<String, Point> getLocationsAsStatic() {

        return Collections.unmodifiableMap(
                new HashMap<String, Point>(locations));
    }

    public static void main(String args[]){
        Map<String, Point> points=new HashMap<>();
        DelegatingVehicleTracker delegatingVehicleTracker=new DelegatingVehicleTracker(points);
        Map<String, Point> point1=delegatingVehicleTracker.getLocations();
        delegatingVehicleTracker.setLocation("asdasda",123,123);
        System.out.println(delegatingVehicleTracker.getLocations().get("asdasda"));
    }
}

