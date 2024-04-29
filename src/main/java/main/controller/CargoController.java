package main.controller;

import cargo.schedule.Schedule;
import cargo.schedule.ScheduleNetworkListener;
import cargo.schedule.Solution;
import com.alibaba.fastjson.JSONObject;
import main.config.AppConfig;
import main.util.Response;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class CargoController {
    private SolverFactory factory;
    private ThreadPoolTaskExecutor executor;
    private TaskRepository taskRepository;

    @Autowired
    public CargoController(
        @Qualifier("cargoSolverFactory") SolverFactory factory,
        ThreadPoolTaskExecutor executor) {
        this.factory = factory;
        this.executor = executor;
        this.taskRepository = TaskRepository.getIns();
    }

    @RequestMapping(value = "/solve/cargo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject solve(@RequestBody JSONObject data) {
        UUID id = UUID.randomUUID();
        Schedule schedule = data.toJavaObject(Schedule.class);

        try {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Solver solver = factory.buildSolver();
                    ScheduleNetworkListener listener = new ScheduleNetworkListener(id);
                    solver.addEventListener(listener);
                    solver.solve(schedule);
                }
            });
        } catch (TaskRejectedException e) {
            // 任务队列已满，拒绝此任务
            return Response.msg(false, "busy", null);
        }

        // 成功发起一个异步任务
        data = new JSONObject();
        data.put("uuid", id);
        return Response.msg(true, "", data);
    }

    @RequestMapping(value = "/result/cargo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getResult(@RequestBody JSONObject data) {
        UUID id = data.getObject("uuid", UUID.class);
        Solution solution = (Solution) taskRepository.getSolution(id);
        if (solution == null) {
            return Response.msg(false, "pending", null);
        }
        return Response.msg(true, "", solution);
    }
}
