package path.schedule;

import path.model.Location;
import path.model.Request;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;
import org.optaplanner.core.api.score.calculator.EasyScoreCalculator;
import path.model.Standstill;

import java.util.List;

public class Score
    implements EasyScoreCalculator<Schedule, HardMediumSoftLongScore> {
    @Override
    public HardMediumSoftLongScore calculateScore(Schedule solution) {
        long hard = 0;
        double distance = 0;
        List<Request> requestList = solution.getRequestList();
        for (Request request : requestList) {
            Standstill prev = request.getPrevious();
            if (prev == null) hard++;
            else {
                Location current = request.getLocation();
                distance += current.getDistanceTo(prev.getLocation());
            }
        }
        long sum = (long) distance;
        return HardMediumSoftLongScore.of(-hard, -sum, -sum);
    }
}
