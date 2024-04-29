<template>
  <el-card>
    <svg id="map" width="1100" height="350">
    </svg>
  </el-card>
</template>

<script>
import * as d3 from "d3";

export default {
  mounted() {
  },
  methods: {
    plot(data) {
      let storage = data["storage"];
      let path = data["path"];
      function realX(data) {
        return data["location"]["latitude"] * 6 + 10;
      }
      function realY(data) {
        return data["location"]["longitude"] * 3 + 10;
      }
      let dot = [storage, ...path];
      console.log("dot = ", dot);
      let parent = d3.select("#map");
      parent.selectAll("*").remove();
      let elements = parent.selectAll("circle")
        .data(dot)
        .enter()
        .append("circle");
      let circleAttr = elements.attr("r", 2)
        .attr("cx", realX)
        .attr("cy", realY);
      
      let lineGenerator = d3.line()
        .x(realX)
        .y(realY);
      let pathString = lineGenerator(dot);
      parent.append("path")
        .attr("fill", "none")
        .attr("stroke", "black")
        .attr("stroke-dasharray", [5, 5])
        .attr("d", pathString);
    }
  }
}
</script>