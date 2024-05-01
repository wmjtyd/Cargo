<template>
  <el-card>
    <svg id="map" width="1100" height="350">
    </svg>
  </el-card>
</template>

<script>
import * as d3 from "d3";

export default {
  methods: {
    getCarColor(carId) {
      const colors = [
        '#F9D371', '#3DB2FF', 'green', '#6E85B2', '#F47340',
        '#F6A9A9', '#EF2F88', '#5F7A61', '#1ee3cf', '#C2F784',
        '#FFF89A', 'rgba(22, 119, 255, .5)', '#93FFD8', '#BAFFB4',
        '#D3DEDC', '#AEFEFF', '#9B0000', '#F5F5F5', 'red', '#08ffc8',
        '#FFE3E3'
      ];
      return colors[carId % colors.length];
    },
    plot(data) {
      // 打印接收到的数据
      console.log("Received data for plotting:", data);
      const plans = data.plans;  // 正确访问 plans 而非 plan
      function realX(location) {
        return location["latitude"] * 6 + 10;
      }
      function realY(location) {
        return location["longitude"] * 3 + 10;
      }
      const parent = d3.select("#map");
      parent.selectAll("*").remove();
      plans.forEach((route, index) => {  // 正确遍历 plans 数组
        const carColor = this.getCarColor(route.car.carId);
        const carStandstill = route.car;
        const requestStandstills = route.path;
        const dots = [carStandstill].concat(requestStandstills);

        const lineGenerator = d3.line()
          .x(dot => realX(dot.location))
          .y(dot => realY(dot.location));
        const pathString = lineGenerator(dots);

        parent.append("path")
          .attr("d", pathString)
          .attr("fill", "none")
          .attr("stroke", carColor)
          .attr("stroke-width", 2)
          .attr("stroke-dasharray", "5,5");

        dots.forEach(dot => {
          const editor = parent.append("circle")
            .attr("r", 4)
            .attr("cx", realX(dot.location))
            .attr("cy", realY(dot.location))
            .attr("fill", carColor);
          if (dot.carId) {
            editor.attr("stroke-width", 2)
              .attr("stroke", "red");
          } else if (!dot.orderId) {
            editor.attr("stroke-width", 2)
              .attr("stroke", "black");
          }
        });
      });
    }
  }
}
</script>