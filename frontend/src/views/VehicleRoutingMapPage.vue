<template>
  <el-container>
    <el-header>
      <MyHeader />
    </el-header>
    <el-main>
      <div class="display-area">
        <VehicleRoutingMap ref="vehicleRoutingMap" />
        <el-card style="max-width: 300px; min-width: 300px; max-height: 396px;">
          <el-input
            type="textarea"
            autosize
            style="overflow-y: scroll; max-height: 366px;"
            v-model="textarea"
            placeholder="请生成随机数据或编辑数据">
          </el-input>
        </el-card>
      </div>

      <el-divider></el-divider>
      <div style="text-align: center;">
        <Status :stage="stage"></Status>
        <el-button type="primary" @click="start">开始运行</el-button>
        <el-button type="primary" @click="random">生成随机数据</el-button>
      </div>
    </el-main>
  </el-container>
</template>
  
<script>
import MyHeader from '@/components/MyHeader.vue';
import VehicleRoutingMap from '@/components/VehicleRoutingMap.vue';
import { ElMessageBox } from 'element-plus';
import Status from '@/components/Status.vue';

const LIMIT = 120;

export default {
  components: {
    VehicleRoutingMap, MyHeader, Status
  },
  data() {
    return {
      id: 0,
      textarea: "",
      data: null,
      uuid: null,
      pollingCount: 0,
      pollTimer: null,
      stage: "empty"
    }
  },
  mounted() {
    this.startPolling();
  },
  beforeUnmount() {
    this.stopPolling();
  },
  methods: {
    start() {
      if (this.textarea === null) {
        ElMessageBox.alert("请先生成测试数据");
        return;
      }
      this.data = JSON.parse(this.textarea);
      this.$http.post("/solve/routing", this.data)
        .then((res) => {
          res = res["data"];
          if (!res["success"]) {
            this.stage = "busy";
            this.uuid = null;
            this.pollingCount = 0;
            return;
          }
          this.stage = "pending";
          this.uuid = res["data"]["uuid"];
          this.pollingCount = LIMIT;
        });
    },
    startPolling() {
      this.pollTimer = setInterval(() => {
        this.getResult();
      }, 10000);
    },
    stopPolling() {
      clearInterval(this.pollTimer);
    },

    // 轮询获取答案
    getResult() {
      if (this.pollingCount <= 0 || !this.uuid) return;
      this.$http.post("/result/routing", { uuid: this.uuid })
        .then((res) => {
          res = res["data"];
          if (!res["success"]) {
            return;
          }
          let vehicleRoutingMap = this.$refs.vehicleRoutingMap;
          vehicleRoutingMap.plot(res["data"]);
          this.pollingCount--;
          this.stage = "solving";
          if (this.pollingCount === 0)
            this.stage = "finish";
        });
    },

    // 随机数据生成
    random() {
      const carList = this.randomCar(5);
      const storageList = this.randomStorage(1);
      const requestList = this.randomRequest(1000, storageList);
      let data = {
        "carList": carList,
        "storageList": storageList,
        "requestList": requestList
      };
      this.textarea = JSON.stringify(data);
      this.data = data;
    },
    randomLocation() {
        const address = "loc." + (++this.id);
        const lat = Math.random() * 100;
        const log = Math.random() * 100;
        return { "address": address, "latitude": lat, "longitude": log };
    },
    randomARequest(storageList) {
      const idx = this.getRandomIntegerInRange(0, storageList.length - 1);
      return {
        "orderId": ++this.id,
        "storageId": storageList[idx].storageId,
        "weight": Math.random() * 10 + 5,
        "location": this.randomLocation()
      }
    },
    randomRequest(length, storageList) {
        const requestList = [];
        for (let i = 0; i < length; i++)
            requestList.push(this.randomARequest(storageList));
        return requestList;
    },
    randomACar() {
      return {
        "carId": ++this.id,
        "capacity": 150,
        "location": this.randomLocation()
      };
    },
    randomCar(length) {
        const carList = [];
        for (let i = 0; i < length; i++)
            carList.push(this.randomACar());
        return carList;
    },
    randomAStorage() {
      return {
        "storageId": (++this.id),
        "location": this.randomLocation()
      };
    },
    randomStorage(length) {
      const storageList = [];
      for (let i = 0; i < length; i++)
        storageList.push(this.randomAStorage());
      return storageList;
    },
    getRandomIntegerInRange(l, r) {
      const min = Math.ceil(l);   // 向上取整，确保最小值为 l 的整数倍
      const max = Math.floor(r);  // 向下取整，确保最大值为 r 的整数倍
      return Math.floor(Math.random() * (max - min + 1)) + min;
    }
  }
}
</script>

<style scoped>
.display-area {
  display: flex;
}
.display-area div {
  margin: 0px 10px;
}
</style>