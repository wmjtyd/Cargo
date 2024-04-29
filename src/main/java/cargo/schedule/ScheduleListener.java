package cargo.schedule;

import com.alibaba.fastjson.JSON;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import cargo.schedule.Schedule;

public class ScheduleListener implements SolverEventListener<Schedule> {
    @Override
    public void bestSolutionChanged(BestSolutionChangedEvent<Schedule> event) {
        Schedule problem = event.getNewBestSolution();
        HardMediumSoftLongScore score = (HardMediumSoftLongScore) event.getNewBestScore();
        Solution solution = new Solution(problem);
        System.out.println(JSON.toJSONString(solution));
    }
}