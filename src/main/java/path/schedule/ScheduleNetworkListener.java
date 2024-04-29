package path.schedule;

import main.controller.TaskRepository;
import main.config.AppConfig;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
