package cargo.schedule;

import cargo.model.Car;
import cargo.model.Request;
import cargo.model.Standstill;
import cargo.model.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    private class Plan {
        private Car car;
        private List<Object> path;

        public Plan(Car car, List<Object> path) {
            this.car = car;
            this.path = path;
        }

        public Car getCar() {
            return car;
        }

        public List<Object> getPath() {
            return path;
        }
    }
    private List<Plan> plan;

    public Solution(Schedule solution) {
        plan = new ArrayList<>();
        Map<Integer, Storage> storageMap = new HashMap<>();
        Map<Standstill, Request> edges = new HashMap<>();
        for (Storage storage : solution.getStorageList()) {
            storageMap.put(storage.getStorageId(), storage);
        }
        for (Request request : solution.getRequestList()) {
            Standstill prev = request.getPrevious();
            if (prev != null) edges.put(prev, request);
        }
        for (Car car : solution.getCarList()) {
            Standstill cur = car;
            List<Request> path = new ArrayList<>();
            while (edges.containsKey(cur)) {
                Request next = edges.get(cur);
                path.add(next);
                cur = next;
            }

            plan.add(new Plan(car, getPathWithStorage(car, path, storageMap)));
        }
    }

    private boolean inTheSameCarWithPreviousRequest(List<Request> path, int idx) {
        if (idx == 0) return false;
        Request prev = path.get(idx - 1);
        Request cur = path.get(idx);
        return prev.getStorageId() == cur.getStorageId();
    }

    private List<Object> getPathWithStorage(Car car, List<Request> path, Map<Integer, Storage> storageMap) {
        List<Object> pathWithStorage = new ArrayList<>();
        for (int l = 1, r = l; l < path.size(); l = r + 1, r = l) {
            double load = 0;
            Request lrequest = ((Request) path.get(l));
            while (r + 1 < path.size() && ((Request) path.get(r + 1)).getWeight() + load <= car.getCapacity()) {
                Request rrequest = ((Request) path.get(r + 1));
                if (lrequest.getStorageId() != rrequest.getStorageId()) {
                    break;
                }
                load += ((Request) path.get(r + 1)).getWeight();
                r++;
            }
            // 仓库取货
            pathWithStorage.add(storageMap.get(lrequest.getStorageId()));
            for (int i = l; i <= r; i++) {
                pathWithStorage.add(path.get(i));
            }
        }
        return pathWithStorage;
    }

    public List<Plan> getPlan() {
        return plan;
    }
}
