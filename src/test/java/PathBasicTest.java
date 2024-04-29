import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import path.model.Location;
import path.model.Request;
import path.model.Storage;
import path.schedule.Schedule;
import path.schedule.ScheduleListener;
import path.schedule.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PathBasicTest {
    private static int id = 0;
    private static Random random = new Random();

    private Location randomLocation() {
        String address = "loc." + id;
        double lat = random.nextDouble() * 100;
        double log = random.nextDouble() * 100;
        return new Location(address, lat, log);
    }

    private Request randomARequest() {
        return new Request(++id+"", randomLocation());
    }

    public List<Request> randomRequest(int length) {
        List<Request> requestList = new ArrayList<>();
        for (int i = 0; i < length; i++)
            requestList.add(randomARequest());
        return requestList;
    }

    public Storage randomStorage() {
        return new Storage(randomLocation());
    }

    @Test
    public void testBasic() {
        SolverFactory solverFactory = SolverFactory.createFromXmlResource("path.xml");
        Solver solver = solverFactory.buildSolver();
        Schedule problem = new Schedule(
            randomStorage(),
            randomRequest(50)
        );
        ScheduleListener listener = new ScheduleListener();
        solver.addEventListener(listener);
        solver.solve(problem);
    }

    @Test
    public void testJson() {
        Schedule problem = new Schedule(
            randomStorage(),
            randomRequest(5)
        );
        Solution solution = new Solution(problem);
        System.out.println(JSON.toJSONString(solution));
    }
}
