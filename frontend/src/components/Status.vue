<template>
  <el-button :type="buttonType" style="pointer-events: none; cursor: not-allowed;" :loading="isLoading">
    {{ buttonContent }}
  </el-button>
</template>

<script>

// stage: empty
// stage: busy
// stage: pending
// stage: solving
// stage: finish
export default {
  props: {
    stage: {
      type: String,
      default: "empty"
    },
  },
  computed: {
    buttonType() {
      let stage = this.stage;
      if (stage === "empty") return "info";
      else if (stage === "busy") return "danger";
      else if (stage === "pending") return "warning";
      else if (stage === "solving") return "primary";
      else return "success";
    },
    buttonContent() {
      let stage = this.stage;
      if (stage === "empty") return "无任务";
      else if (stage === "busy") return "任务提交失败，前方任务太多";
      else if (stage === "pending") return "任务排队中";
      else if (stage === "solving") return "正在解决任务";
      else return "任务已解决";
    },
    isLoading() {
      let stage = this.stage;
      if (stage === "empty" || stage === "busy" || stage === "finish") return false;
      return true;
    }
  }
}
</script>