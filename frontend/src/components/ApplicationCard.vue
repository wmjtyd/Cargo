<template>
  <el-card style="max-width: 400px;" shadow="hover">
    <template #header>
      <div style="font-weight: 600; font-size: 20px;">
        <span>{{ name }}</span>
      </div>
    </template>
    <p>
      {{ introduction }}
    </p>
  </el-card>
</template>

<script>
export default {
  props: {
    name: {
      type: String,
      default: "NULL"
    },
    introduction: {
      type: String,
      default: "NULL"
    }
  }
}
</script>