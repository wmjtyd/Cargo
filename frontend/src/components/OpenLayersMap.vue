<template>
  <div ref="mapContainer" class="map-container"></div>
</template>

<script>
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import VectorSource from 'ol/source/Vector';
import { Vector as VectorLayer } from 'ol/layer';
import { LineString, Point } from 'ol/geom';
import { Style, Stroke, Circle, Fill } from 'ol/style';
import Feature from 'ol/Feature';
import { fromLonLat } from 'ol/proj';

export default {
  name: 'OpenLayersMap',
  data() {
    return {
      map: null,
      vectorSource: new VectorSource(),
    };
  },
  mounted() {
    this.map = new Map({
      target: this.$refs.mapContainer,
      layers: [
        new TileLayer({
          source: new OSM()
        }),
        new VectorLayer({
          source: this.vectorSource
        })
      ],
      view: new View({
        center: fromLonLat([-71.1486951, 42.3363567]), // 示例：xxx的经纬度 -71.1486951 对应的是经度（longitude）, 42.3363567 对应的是纬度（latitude）
        zoom: 11
      })
    });
  },
  methods: {
    getCarColor(carId) {
      // 定义一个颜色数组或函数，为不同的车辆分配不同的颜色
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

      this.vectorSource.clear(); // 清除当前所有特征

      // 遍历每条路线
      data.plan.forEach((route) => {
        let carColor = this.getCarColor(route.car.carId);
        let carLocation = [route.car.location.longitude, route.car.location.latitude];
        let pathLocations = route.path
          .filter(p => p.orderId && p.location) // 确保每个点都有 orderId 和 location
          .map(p => [p.location.longitude, p.location.latitude]);

        // 将车辆位置添加到路径点的开头
        pathLocations.unshift(carLocation);

        // 打印 pathLocations 数组以确认 carLocation 已被添加
        // console.log("Path locations after unshift:", pathLocations);

        // 创建并添加路径特征
        let routeFeature = new Feature({
          geometry: new LineString(pathLocations)
        });
        routeFeature.setStyle(new Style({
          stroke: new Stroke({
            color: carColor,
            width: 3
          })
        }));
        this.vectorSource.addFeature(routeFeature);

        // 创建并添加路径点特征
        pathLocations.forEach((pointCoords, index) => {
          let pointFeature = new Feature({
            geometry: new Point(pointCoords)
          });
          pointFeature.setStyle(new Style({
            image: new Circle({
              radius: 5,
              fill: new Fill({color: carColor}),
              stroke: new Stroke({color: 'white', width: 1})
            })
          }));
          this.vectorSource.addFeature(pointFeature);
        });

        // 在添加特征后打印确认
        console.log(`Route feature added for carId ${route.car.carId}:`, routeFeature);
        console.log(`Point features added for carId ${route.car.carId}:`, pathLocations);
       });

        // 检查是否适配了视图
        this.map.getView().fit(this.vectorSource.getExtent(), {
          padding: [50, 50, 50, 50],
          maxZoom: 16,
        });
        console.log("Fit view to features.");
    }
  }
};
</script>

<style>
.map-container {
  height: 100%;
  width: 100%;
}
</style>
