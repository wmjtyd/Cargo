package path.schedule;

import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import path.model.Request;
import path.model.Storage;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;

import java.util.ArrayList;
import java.util.List;

@PlanningSolution
public class Schedule {
    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "storageProvider")
    private List<Storage> storageList;

    @PlanningEntityCollectionProperty
    @ValueRangeProvider(id = "requestProvider")
    private List<Request> requestList;

    @PlanningScore
    private HardMediumSoftLongScore score;

    public Schedule() {
    }

    public Schedule(Storage storage, List<Request> requestList) {
        this.storageList = new ArrayList<>();
        this.storageList.add(storage);
        this.requestList = requestList;
    }

    public Storage getStorage() {
        return storageList.get(0);
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public void setStorageList(List<Storage> storageList) {
        this.storageList = storageList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }
}
