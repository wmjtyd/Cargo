<template>
  <el-container>
    <el-header>
      <MyHeader />
    </el-header>
    <el-main>
      <div class="applications">
        <ApplicationCard v-for="application in applications"
          :name="application.name"
          :introduction="application.introduction"
          @click="jumpTo(application.url)">
          {{ application.name }}
        </ApplicationCard>
      </div>
    </el-main>
  </el-container>
</template>

<script>
import MyHeader from "@/components/MyHeader.vue";
import ApplicationCard from "@/components/ApplicationCard.vue";
import Status from "@/components/Status.vue";

export default {
  components: {
    MyHeader, ApplicationCard,
    Status
  },
  data() {
    return {
      applications: [
        { name: "Cargo",
          introduction: "Cargo应用致力于解决，存在N辆货车，每辆货车载重各不相同，存在M个仓库，仓库中有无限量的货物，存在K个送货点，每个送货点要求不同单位重量的货物，现在尝试让所有送货点都被满足，同时所有货车中行驶路程最长的那个货车，行驶路程尽可能短，这样一个组合优化问题",
          url: "/cargoMap"
        },
        { name: "Path",
          introduction: "Path应用致力于解决，存在一辆载重无限的货车，存在1个仓库，仓库中有无限量的货物，存在K个送货点，现在尝试让所有送货点都被满足，同时货车行驶路程尽可能短，这样一个TSP问题",
          url: "/pathMap"
        },
        // 在 data 返回的 applications 数组中添加:
        {
          name: "OpenLayers Map",
          introduction: "使用 OpenLayers 显示 OpenStreetMap 地图的示例页面。",
          url: "/openlayersMap"
        }
      ]
    }
  },
  methods: {
    jumpTo(url) {
      this.$router.push(url);
    }
  }
}
</script>

<style scoped>
.applications {
  display: flex;
  flex-wrap: wrap;
}
.applications > div {
  margin: 0px 20px 20px;
}
</style>