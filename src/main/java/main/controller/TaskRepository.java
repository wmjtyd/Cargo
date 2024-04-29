package main.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class TaskRepository {
    private class TaskItem {
        public Object solution;
        public LocalDateTime timestamp;

        public TaskItem(Object solution, LocalDateTime timestamp) {
            this.solution = solution;
            this.timestamp = timestamp;
        }
    }

    private Map<UUID, TaskItem> solutions;
    private static TaskRepository instance = new TaskRepository();

    private TaskRepository() {
        solutions = new ConcurrentHashMap<>();
    }

    public static TaskRepository getIns() {
        return instance;
    }

    public Object getSolution(UUID id) {
        TaskItem taskItem = solutions.get(id);
        return taskItem != null ? taskItem.solution : null;
    }

    public void putSolution(UUID id, Object solution) {
        LocalDateTime timestamp = LocalDateTime.now();
        solutions.put(id, new TaskItem(solution, timestamp));
        //log.debug("putSolution uuid = {}, LocalDateTime = {}", id, timestamp);
    }

    // 定期100s清理结果
    @Scheduled(fixedRate = 100000)
    public void cleanUp() {
        LocalDateTime target = LocalDateTime.now().minusSeconds(500);
        Iterator<Map.Entry<UUID, TaskItem>> iterator = TaskRepository.getIns().solutions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, TaskItem> entry = iterator.next();
            if (entry.getValue().timestamp.isBefore(target)) {
                iterator.remove();
            }
        }
        log.debug("solution size = {}", TaskRepository.getIns().solutions.size());
    }
}
