package cargo.schedule;

import main.controller.TaskRepository;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;

import java.util.UUID;

public class ScheduleNetworkListener implements SolverEventListener<Schedule> {
    private UUID taskId;

    public ScheduleNetworkListener(UUID id) {
        taskId = id;
    }

    @Override
    public void bestSolutionChanged(BestSolutionChangedEvent<Schedule> event) {
        Schedule problem = event.getNewBestSolution();
        Solution solution = new Solution(problem);
        TaskRepository.getIns().putSolution(taskId, solution);
    }
}
