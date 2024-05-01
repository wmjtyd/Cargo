package main.controller;

import cargo.schedule.Schedule;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import main.util.Response;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vehiclerouting.bootstrap.DemoDataGenerator;
import vehiclerouting.domain.*;
import vehiclerouting.domain.geo.DistanceCalculator;
import vehiclerouting.domain.geo.EuclideanDistanceCalculator;
import vehiclerouting.schedule.VehicleRoutingListener;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class VehicleRoutingController {
    private SolverFactory factory;
    private ThreadPoolTaskExecutor executor;
    private TaskRepository taskRepository;

    private DistanceCalculator distanceCalculator;

    @Autowired
    public VehicleRoutingController(
            @Qualifier("vehicleRoutingSolverFactory") SolverFactory factory,
            ThreadPoolTaskExecutor executor) {
        this.factory = factory;
        this.executor = executor;
        this.taskRepository = TaskRepository.getIns();
        this.distanceCalculator = new EuclideanDistanceCalculator(); // 或其他适当的实现
    }

    @RequestMapping(value = "/solve/routing", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject solve(@RequestBody JSONObject data) {

        // 打印接收到的 JSONObject
        System.out.println("Received JSON data: " + data.toString());

        UUID id = UUID.randomUUID();

        Schedule schedule = data.toJavaObject(Schedule.class);

        VehicleRoutingSolution problem = convertToVehicleRoutingSolution(schedule);

        try {
            executor.execute(() -> {
                Solver<VehicleRoutingSolution> solver = factory.buildSolver();
                VehicleRoutingListener listener = new VehicleRoutingListener(id);
                solver.addEventListener(listener);
                solver.solve(problem);
            });
        } catch (TaskRejectedException e) {
            return Response.msg(false, "Server is busy", null);
        }

        JSONObject responseData = new JSONObject();
        responseData.put("uuid", id);
        return Response.msg(true, "Solution process started", responseData);
    }

    @RequestMapping(value = "/result/routing", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getResult(@RequestBody JSONObject data) {
        UUID id = data.getObject("uuid", UUID.class);
        VehicleRoutingSolution solution = (VehicleRoutingSolution) taskRepository.getSolution(id);
        if (solution == null) {
            return Response.msg(false, "Solution is still processing", null);
        }
        log.info("uuid is: {}, VehicleRoutingSolution: {}", id, solution);
        return Response.msg(true, "Solution found", solution.getVehicleList());
    }

    @RequestMapping(value = "/solve_test/routing", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject solve_test() {

        UUID id = UUID.randomUUID();
        VehicleRoutingSolution problem = new DemoDataGenerator().generateDemoData();

        try {
            executor.execute(() -> {
                Solver<VehicleRoutingSolution> solver = factory.buildSolver();
                VehicleRoutingListener listener = new VehicleRoutingListener(id);
                solver.addEventListener(listener);
                solver.solve(problem);
            });
        } catch (TaskRejectedException e) {
            return Response.msg(false, "Server is busy", null);
        }

        JSONObject responseData = new JSONObject();
        responseData.put("uuid", id);
        return Response.msg(true, "Solution process started", responseData);
    }

    public VehicleRoutingSolution convertToVehicleRoutingSolution(Schedule schedule) {

        final AtomicLong sequence = new AtomicLong();

        VehicleRoutingSolution vrs = new VehicleRoutingSolution();

        // 转换 Storage 到 Depot
        List<Depot> depotList = schedule.getStorageList().stream()
                .map(storage -> new Depot(sequence.incrementAndGet()+"",
                        new Location(sequence.incrementAndGet(), storage.getLocation().getLatitude(), storage.getLocation().getLongitude())))
                .collect(Collectors.toList());

        // 转换 Request 到 Customer
        List<Customer> customerList = schedule.getRequestList().stream()
                .map(request -> new Customer(request.getOrderId(),
                        new Location(sequence.incrementAndGet(), request.getLocation().getLatitude(), request.getLocation().getLongitude()),
                        (int) request.getWeight()))
                .collect(Collectors.toList());

        // 转换 Car 到 Vehicle
        List<Vehicle> vehicleList = schedule.getCarList().stream()
                .map(car -> new Vehicle(car.getCarId()+"", (int) car.getCapacity(),
                        new Depot(sequence.incrementAndGet()+"",
                                new Location(sequence.incrementAndGet(), car.getLocation().getLatitude(), car.getLocation().getLongitude()))
                ))
                .collect(Collectors.toList());

        // 设置值到 VehicleRoutingSolution 实例
        vrs.setDepotList(depotList);
        vrs.setCustomerList(customerList);
        vrs.setVehicleList(vehicleList);

        List<Location> locationList = Stream.concat(
                Stream.concat(
                        customerList.stream().map(Customer::getLocation),
                        depotList.stream().map(Depot::getLocation)),
                vehicleList.stream().map(vehicle -> vehicle.getDepot().getLocation())
        ).collect(Collectors.toList());

        distanceCalculator.initDistanceMaps(locationList);
        vrs.setLocationList(locationList);
        return vrs;
    }
}


