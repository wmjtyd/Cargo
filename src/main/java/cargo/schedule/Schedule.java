package cargo.schedule;

import cargo.model.Car;
import cargo.model.Request;
import cargo.model.Storage;
import com.alibaba.fastjson.annotation.JSONField;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;

import java.util.List;

@PlanningSolution
public class Schedule {
    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "carProvider")
    private List<Car> carList;

    @PlanningEntityCollectionProperty
    @ValueRangeProvider(id = "requestProvider")
    private List<Request> requestList;

    private List<Storage> storageList;

    @PlanningScore
    private HardMediumSoftLongScore score;

    public Schedule() {
    }

    public Schedule(List<Car> carList, List<Request> requestList, List<Storage> storageList) {
        this.carList = carList;
        this.requestList = requestList;
        this.storageList = storageList;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public List<Storage> getStorageList() {
        return storageList;
    }
}
