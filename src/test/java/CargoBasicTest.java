import cargo.model.Car;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import cargo.model.Location;
import cargo.model.Request;
import cargo.model.Storage;
import cargo.schedule.Schedule;
import cargo.schedule.ScheduleListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CargoBasicTest {
    private static int id = 0;
    private static Random random = new Random();
    private static ArrayList<Integer> storageSet = new ArrayList<>();

    private Location randomLocation() {
        String address = "loc." + id;
        double lat = random.nextDouble() * 100;
        double log = random.nextDouble() * 100;
        return new Location(address, lat, log);
    }

    private Request randomARequest() {
        int length = storageSet.size();
        int idx = random.nextInt(length);
        return new Request(++id+"", storageSet.get(idx), random.nextDouble() * 10 + 5, randomLocation());
    }

    private List<Request> randomRequest(int length) {
        List<Request> requestList = new ArrayList<>();
        for (int i = 0; i < length; i++)
            requestList.add(randomARequest());
        return requestList;
    }

    private Car randomACar() {
        return new Car(++id, 500, randomLocation());
    }

    private List<Car> randomCar(int length) {
        List<Car> carList = new ArrayList<>();
        for (int i = 0; i < length; i++)
            carList.add(randomACar());
        return carList;
    }

    private Storage randomAStorage() {
        storageSet.add(++id);
        return new Storage(id, randomLocation());
    }

    private List<Storage> randomStorage(int length) {
        List<Storage> storageList = new ArrayList<>();
        for (int i = 0; i < length; i++)
            storageList.add(randomAStorage());
        return storageList;
    }

    @Test
    public void testBasic() {
        SolverFactory solverFactory = SolverFactory.createFromXmlResource("cargo.xml");
        Solver solver = solverFactory.buildSolver();
        List<Car> carList = randomCar(5);
        List<Storage> storageList = randomStorage(5);
        List<Request> requestList = randomRequest(50);
        Schedule problem = new Schedule(
            carList,
            requestList,
            storageList
        );
        ScheduleListener listener = new ScheduleListener();
        solver.addEventListener(listener);
        solver.solve(problem);
    }

    @Test
    public void testFromJsonString() {
        String json = "{\"carList\":[{\"carId\":1,\"capacity\":500,\"location\":{\"address\":\"loc.2\",\"latitude\":53.70992312621124,\"longitude\":17.27331776498562}},{\"carId\":3,\"capacity\":500,\"location\":{\"address\":\"loc.4\",\"latitude\":57.00580126875305,\"longitude\":69.4175010725796}}],\"storageList\":[{\"storageId\":5,\"location\":{\"address\":\"loc.6\",\"latitude\":57.140978524307016,\"longitude\":99.64830712034363}},{\"storageId\":7,\"location\":{\"address\":\"loc.8\",\"latitude\":83.9579550405597,\"longitude\":86.5449065562941}}],\"requestList\":[{\"orderId\":9,\"storageId\":7,\"weight\":7.773008935598162,\"location\":{\"address\":\"loc.10\",\"latitude\":78.75417966512408,\"longitude\":23.26514371346402}},{\"orderId\":11,\"storageId\":5,\"weight\":6.612956724033372,\"location\":{\"address\":\"loc.12\",\"latitude\":37.53240636443391,\"longitude\":92.44983969044347}},{\"orderId\":13,\"storageId\":5,\"weight\":13.61925756465152,\"location\":{\"address\":\"loc.14\",\"latitude\":7.609864515735554,\"longitude\":36.62964785942591}},{\"orderId\":15,\"storageId\":7,\"weight\":7.220078474373979,\"location\":{\"address\":\"loc.16\",\"latitude\":78.83626641977557,\"longitude\":74.29496418738566}},{\"orderId\":17,\"storageId\":5,\"weight\":8.26248978642987,\"location\":{\"address\":\"loc.18\",\"latitude\":22.69883523593832,\"longitude\":31.91863559930235}},{\"orderId\":19,\"storageId\":5,\"weight\":13.691143437003046,\"location\":{\"address\":\"loc.20\",\"latitude\":39.34381885203133,\"longitude\":53.949930558684}},{\"orderId\":21,\"storageId\":5,\"weight\":9.39155324307567,\"location\":{\"address\":\"loc.22\",\"latitude\":82.57724664162734,\"longitude\":41.63826450566472}},{\"orderId\":23,\"storageId\":7,\"weight\":11.414905143916362,\"location\":{\"address\":\"loc.24\",\"latitude\":69.85330201199234,\"longitude\":39.13347063448642}},{\"orderId\":25,\"storageId\":7,\"weight\":13.209525425623198,\"location\":{\"address\":\"loc.26\",\"latitude\":3.9427816327449294,\"longitude\":4.827938433009682}},{\"orderId\":27,\"storageId\":5,\"weight\":12.967143740514825,\"location\":{\"address\":\"loc.28\",\"latitude\":77.14076467098188,\"longitude\":10.62537532942882}}]}";
        Schedule problem = JSON.parseObject(json, Schedule.class);
        System.out.println(JSON.toJSONString(problem));
        JSONObject jsonObject = JSON.parseObject(json);
        System.out.println(jsonObject);
    }
}
