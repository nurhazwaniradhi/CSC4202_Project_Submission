import java.util.*;

class Location implements Comparable<Location> {
    String name;
    ArrayList<Path> paths;
    double minDistance = Double.POSITIVE_INFINITY;
    Location previous;

    Location(String name) {
        this.name = name;
        this.paths = new ArrayList<>();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Location other) {
        return Double.compare(minDistance, other.minDistance);
    }
}

class Path {
    Location target;
    double distance;
    double safetyScore;

    Path(Location target, double distance, double safetyScore) {
        this.target = target;
        this.distance = distance;
        this.safetyScore = safetyScore;
    }
}

public class SafePathFinder {

    static void findSafestPath(Location start, Location destination) {
        start.minDistance = 0;
        PriorityQueue<Location> queue = new PriorityQueue<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Location current = queue.poll();

            for (Path path : current.paths) {
                Location next = path.target;
                double weight = path.distance + path.safetyScore;
                double distanceThroughCurrent = current.minDistance + weight;

                if (distanceThroughCurrent < next.minDistance) {
                    queue.remove(next);
                    next.minDistance = distanceThroughCurrent;
                    next.previous = current;
                    queue.add(next);
                }
            }
        }

        printPaths(destination);
    }

    static void printPaths(Location destination) {
        List<Location> path = getPathTo(destination);

        System.out.println("Safest Path:");
        System.out.println("=========================");
        printPathDetails(path);
    }

    static List<Location> getPathTo(Location target) {
        List<Location> path = new ArrayList<>();
        for (Location location = target; location != null; location = location.previous) {
            path.add(location);
        }
        Collections.reverse(path);
        return path;
    }

    static void printPathDetails(List<Location> path) {
        double totalSafetyScore = 0.0;
        double totalDistance = 0.0;

        Location prev = null;
        for (Location loc : path) {
            if (prev != null) {
                Path pathToLoc = null;
                for (Path p : prev.paths) {
                    if (p.target.equals(loc)) {
                        pathToLoc = p;
                        break;
                    }
                }

                if (pathToLoc != null) {
                    totalSafetyScore += pathToLoc.safetyScore;
                    totalDistance += pathToLoc.distance;
                    System.out.println(String.format("From %s to %s | Distance: %.2f | Safety Score: %.2f",
                            prev.name, loc.name, pathToLoc.distance, pathToLoc.safetyScore));
                }
            }
            prev = loc;
        }

        System.out.println(String.format("\nTotal Distance: %.2f", totalDistance));
        System.out.println(String.format("Total Safety Score: %.2f", totalSafetyScore));
    }

    public static void main(String[] args) {
        Location mall = new Location("The Mines Shopping Mall");
        Location kolej12 = new Location("Kolej 12");
        Location checkpoint1 = new Location("Jalan Sg Besi Indah");
        Location checkpoint2 = new Location("Jalan Anggrerik");
        Location checkpoint3 = new Location("Jalan Senja Residence");
        Location checkpoint4 = new Location("Jalan SILK");
        Location checkpoint5 = new Location("Jalan Cempaka");

        mall.paths.addAll(Arrays.asList(
            new Path(checkpoint1, 10, 1.2),
            new Path(checkpoint2, 20, 5.5)
        ));
        checkpoint1.paths.addAll(Arrays.asList(
            new Path(checkpoint3, 15, 2.3),
            new Path(checkpoint4, 30, 1.1)
        ));
        checkpoint2.paths.addAll(Arrays.asList(
            new Path(checkpoint3, 5, 7.0),
            new Path(checkpoint5, 25, 4.2)
        ));
        checkpoint3.paths.addAll(Arrays.asList(
            new Path(kolej12, 10, 1.0),
            new Path(checkpoint5, 10, 2.5)
        ));
        checkpoint4.paths.addAll(Arrays.asList(
            new Path(kolej12, 20, 1.3)
        ));
        checkpoint5.paths.addAll(Arrays.asList(
            new Path(kolej12, 15, 2.0)
        ));

        findSafestPath(mall, kolej12);
    }
}
