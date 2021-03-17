package socialnetwork.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Retea {
    private final Map<Long, HashSet<Long>> adjMap;
    private final Map<Long, Long> visited;

    public Retea( Iterable<Prietenie> friends ) {
        this.adjMap = new HashMap<>();
        this.visited = new HashMap<>();
        friends.forEach(x -> addEdge(x.getId().getLeft(), x.getId().getRight()));
    }

    private void addEdge( Long s, Long d ) {
        adjMap.putIfAbsent(s, new HashSet<>());
        adjMap.putIfAbsent(d, new HashSet<>());
        adjMap.get(s).add(d);
        adjMap.get(s).add(s);
        adjMap.get(d).add(s);
        adjMap.get(d).add(d);
        visited.put(s, (long) 0);
        visited.put(d, (long) 0);
    }

    private void findDFS( Long vertex ) {
        visited.put(vertex, (long) 1);
        for (Long child : adjMap.get(vertex)) {
            if (visited.get(child) == (long) 0) {
                findDFS(child);
            }
        }
    }

    public int ConnectedComponents() {
        int count = 0;
        for (Long vertex : visited.keySet()) {
            if (visited.get(vertex) == (long) 0) {
                findDFS(vertex);
                count++;
            }
        }
        return count;
    }

}
