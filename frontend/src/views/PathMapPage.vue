<template>
  <el-container>
    <el-header>
      <MyHeader />
    </el-header>
    <el-main>
      <div class="display-area">
        <PathMap ref="pathMap" />
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
import PathMap from '@/components/PathMap.vue';
import { ElMessageBox } from 'element-plus';
import Status from '@/components/Status.vue';

const LIMIT = 30;

export default {
  components: {
    PathMap, MyHeader, Status
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
      if (this.data === null) {
        ElMessageBox.alert("请先生成测试数据");
        return;
      }
      let self = this;
      this.$http.post("/solve/path", this.data)
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
      this.$http.post("/result/path", { uuid: this.uuid })
        .then(function(res) {
          res = res["data"];
          if (!res["success"]) {
            return;
          }
          let pathMap = self.$refs.pathMap;
          pathMap.plot(res["data"]);
          self.pollingCount--;
          self.stage = "solving";
          if (self.pollingCount === 0)
            self.stage = "finish";
        });
    },

    // 随机数据生成
    random() {
      let data = {
        "requestList": this.randomRequest(5000),
        "storageList": this.randomStorage()
      };
      this.textarea = JSON.stringify(data);
      this.data = data;
    },
    randomLocation() {
      let address = "loc." + (++this.id);
      let lat = Math.random() * 100;
      let log = Math.random() * 100;
      return { "address": address, "latitude": lat, "longitude": log};
    },
    randomARequest() {
      return {
        "orderId": ++this.id,
        "location": this.randomLocation()
      };
    },
    randomRequest(length) {
        let requestList = [];
        for (let i = 0; i < length; i++)
            requestList.push(this.randomARequest());
        return requestList;
    },
    randomStorage() {
        return {
          "location": this.randomLocation()
        };
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