<template>
  <el-container>
    <el-header>
      <MyHeader />
    </el-header>
    <el-main>
      <div class="display-area">
        <OpenLayersMap ref="openLayersMap" />
            <el-card style="max-width: 300px; min-width: 300px;">
              <el-input
                type="textarea"
                autosize
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
import OpenLayersMap from '@/components/OpenLayersMap.vue'; // 引入新组件
import { ElMessageBox } from 'element-plus';
import Status from '@/components/Status.vue';

const LIMIT = 60;

export default {
  components: {
    OpenLayersMap, MyHeader, Status
  },
  data() {
    return {
      id: 0,
      textarea: "请生成随机数据",
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

      try {
        // 将 textarea 的字符串内容转换为 JSON 对象
        this.data = JSON.parse(this.textarea);
      } catch (e) {
        ElMessageBox.alert("数据格式有误，请输入有效的JSON数据。");
        return;
      }

      let self = this;
      this.$http.post("/solve/cargo", this.data)
        .then(function(res) {
          res = res["data"];
          if (!res["success"]) {
            self.stage = "busy";
            self.uuid = null;
            self.pollingCount = 0;
            return;
          }
          self.stage = "pending";
          self.uuid = res["data"]["uuid"];
          self.pollingCount = LIMIT;
        });
    },
    startPolling() {
      let self = this;
      this.pollTimer = setInterval(function() {
        self.getResult();
      }, 1000);
    },
    stopPolling() {
      clearInterval(this.pollTimer);
    },

    // 轮询获取答案
    getResult() {
      if (this.pollingCount <= 0 || !this.uuid) return;
      let self = this;
      this.$http.post("/result/cargo", { uuid: this.uuid })
        .then(function(res) {
          res = res["data"];
          if (!res["success"]) {
            return;
          }
          let openLayersMap = self.$refs.openLayersMap; // 修改这里
          if (openLayersMap && typeof openLayersMap.plot === 'function') {
            openLayersMap.plot(res["data"]); // 确保 OpenLayersMap.vue 有 plot 方法
          }
          self.pollingCount--;
          self.stage = "solving";
          if (self.pollingCount === 0)
            self.stage = "finish";
        });
    },

    // 随机数据生成
    random() {
      let data = {
        "carList": this.randomCar(4),
        "requestList": this.randomRequest(60),
        "storageList": this.randomStorage(3)
      };
      this.textarea = JSON.stringify(data);
      this.data = data;
    },
    randomLocation() {
        let address = "loc." + this.id;
        let lat = Math.random() * 100;
        let log = Math.random() * 100;
        return { "address": address, "latitude": lat, "longitude": log };
    },
    randomARequest() {
      return {
        "orderId": ++this.id,
        "weight": Math.random() * 10 + 5,
        "location": this.randomLocation()
      }
    },
    randomRequest(length) {
        let requestList = [];
        for (let i = 0; i < length; i++)
            requestList.push(this.randomARequest());
        return requestList;
    },
    randomACar() {
      return {
        "carId": ++this.id,
        "capacity": 500,
        "location": this.randomLocation()
      };
    },
    randomCar(length) {
        let carList = [];
        for (let i = 0; i < length; i++)
            carList.push(this.randomACar());
        return carList;
    },
    randomAStorage() {
      return {
        "location": this.randomLocation()
      };
    },
    randomStorage(length) {
      let storageList = [];
      for (let i = 0; i < length; i++)
        storageList.push(this.randomAStorage());
      return storageList;
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