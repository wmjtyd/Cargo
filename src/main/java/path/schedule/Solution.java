package path.schedule;

import path.model.Request;
import path.model.Standstill;
import path.model.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    private Storage storage;
    private List<Request> path;

    public Solution(Schedule solution) {
        storage = solution.getStorage();
        Map<Standstill, Request> edges = new HashMap<>();
        for (Request request : solution.getRequestList()) {
            Standstill prev = request.getPrevious();
            if (prev != null) edges.put(prev, request);
        }
        path = new ArrayList<>();
        Standstill cur = storage;
        while (edges.containsKey(cur)) {
            Request next = edges.get(cur);
            path.add(next);
            cur = next;
        }
    }

    public Storage getStorage() {
        return storage;
    }

    public List<Request> getPath() {
        return path;
    }
}
