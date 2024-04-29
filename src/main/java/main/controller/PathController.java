package main.controller;

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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import path.schedule.Schedule;
import path.schedule.ScheduleNetworkListener;
import path.schedule.Solution;

import java.util.UUID;

@Controller
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class PathController {
    private SolverFactory factory;
    private ThreadPoolTaskExecutor executor;
    private TaskRepository taskRepository;

    @Autowired
    public PathController(
        @Qualifier("pathSolverFactory") SolverFactory factory,
        ThreadPoolTaskExecutor executor) {
        this.factory = factory;
        this.executor = executor;
        this.taskRepository = TaskRepository.getIns();
    }

    /** 请求示例
     *  {
     *      "storageList": [ {
     *          "location": {
     *              "address": "loc.1",
     *              "latitude": 1,
     *              "longitude": 2
     *          }
     *      } ],
     *      "requestList": [ {
     *          "location": {
     *              "address": "loc.2",
     *              "latitude": 1,
     *              "longitude": 3
     *          }
     *      }, {
     *          "location": {
     *              "address": "loc.3",
     *              "latitude": 2,
     *              "longitude": 1
     *          }
     *      } ]
     *  }
     *  响应示例
     *  {
     *      "success": true,
     *      "msg": "",
     *      "data": {
     *          "uuid": <UUID>
     *      }
     *  }
     */
    @RequestMapping(value = "/solve/path", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject solve(@RequestBody JSONObject data) {
        UUID id = UUID.randomUUID();
        Schedule schedule = data.toJavaObject(Schedule.class);

        try {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    log.debug("/solve/path uuid = {}", id);
                    Solver solver = factory.buildSolver();
                    ScheduleNetworkListener listener = new ScheduleNetworkListener(id);
                    solver.addEventListener(listener);
                    solver.solve(schedule);
                }
            });
        } catch (TaskRejectedException e) {
            return Response.msg(false, "busy", null);
        }

        data = new JSONObject();
        data.put("uuid", id);
        return Response.msg(true, "", data);
    }

    /** 请求示例
     *  {
     *      "uuid": <UUID>
     *  }
     *  响应示例
     *  {
     *      "success": true,
     *      "msg": "",
     *      "data": {
     *          ......
     *      }
     *  }
     *  {
     *      "success": false,
     *      "msg": "pending",
     *      "data": {}
     *  }
     */
    @RequestMapping(value = "/result/path", method = RequestMethod.POST)
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
