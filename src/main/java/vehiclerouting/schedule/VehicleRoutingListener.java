package vehiclerouting.schedule;

import main.controller.TaskRepository;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import vehiclerouting.domain.VehicleRoutingSolution;

import java.util.UUID;
/**
 * 这个类将监听 OptaPlanner 的求解过程，每次找到更好的解决方案时更新
 * */
public class VehicleRoutingListener implements SolverEventListener<VehicleRoutingSolution> {
    private UUID taskId;

    public VehicleRoutingListener(UUID id) {
        this.taskId = id;
    }

    @Override
    public void bestSolutionChanged(BestSolutionChangedEvent<VehicleRoutingSolution> event) {
        VehicleRoutingSolution solution = event.getNewBestSolution();
        TaskRepository.getIns().putSolution(taskId, solution);
    }
}