package cargo.schedule;

import cargo.model.Car;
import cargo.model.Location;
import cargo.model.Storage;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;
import org.optaplanner.core.api.score.calculator.EasyScoreCalculator;
import cargo.model.Request;
import cargo.model.Standstill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Score implements EasyScoreCalculator<Schedule, HardMediumSoftLongScore> {
    private class Result {
        public long distance;

        public Result(long distance) {
            this.distance = distance;
        }
    }

    @Override
    public HardMediumSoftLongScore calculateScore(Schedule solution) {
        long hard = 0, mid = 0, soft = 0;
        Map<Integer, Storage> storageMap = new HashMap<>();
        Map<Standstill, Standstill> edges = new HashMap<>();
        for (Storage storage : solution.getStorageList()) {
            storageMap.put(storage.getStorageId(), storage);
        }
        for (Request request : solution.getRequestList()) {
            Standstill prev = request.getPrevious();
            if (prev != null) edges.put(prev, request);
            else ++hard;
        }
        for (Car car : solution.getCarList()) {
            Standstill cur = car;
            List<Standstill> path = new ArrayList<>();
            path.add(cur);
            while (edges.containsKey(cur)) {
                Standstill next = edges.get(cur);
                path.add(next);
                cur = next;
            }
            Result result = calculatePath(path, storageMap);
            soft += result.distance;
            mid = Math.max(mid, result.distance);
        }
        soft = 0;
        return HardMediumSoftLongScore.of(-hard, -mid, -soft);
    }

    private boolean inTheSameCarWithPreviousRequest(List<Standstill> path, int idx) {
        if (idx == 1) return false;
        Request prev = (Request) path.get(idx - 1);
        Request cur = (Request) path.get(idx);
        return prev.getStorageId() == cur.getStorageId();
    }

    public double moveTo(Location current, Standstill target) {
        Location targetLocation = target.getLocation();
        return current.getDistanceTo(targetLocation);
    }

    public Result calculatePath(List<Standstill> path, Map<Integer, Storage> storageMap) {
        double dist = 0;
        Car car = (Car) path.get(0);
        Location cur = car.getLocation();
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
            dist += moveTo(cur, storageMap.get(lrequest.getStorageId()));
            cur = storageMap.get(lrequest.getStorageId()).getLocation();
            for (int i = l; i <= r; i++) {
                dist++;
                dist += moveTo(cur, path.get(i));
                cur = path.get(i).getLocation();
            }
        }
        dist *= 1000000;
        return new Result((long) dist);
    }
}
