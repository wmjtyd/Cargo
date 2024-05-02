<template>
  <div id="map" style="width: 1400px; height: 500px;"></div>
</template>

<script>
import { Loader } from '@googlemaps/js-api-loader';

export default {
  data() {
    return {
      googleMap: null,
      mapOverlays: []
    };
  },
  mounted() {
    this.loadMap();
  },
  methods: {
    loadMap() {
      const loader = new Loader({
        apiKey: 'YOUR_API_KEY', // 替换为您的 Google Maps API 密钥
        version: 'weekly'
      });
      loader.load().then(() => {
        this.googleMap = new google.maps.Map(document.getElementById('map'), {
          center: { lat: 42.3363567, lng: -87.7470571 }, // 根据需要调整地图中心
          zoom: 8
        });
      });
    },
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
    clearMap_backup() {
      // 清除所有覆盖物
      this.mapOverlays.forEach(overlay => overlay.setMap(null));
      this.mapOverlays = []; // 清空覆盖物列表
    },
    clearMap() {
      if (this.googleMap) {
        this.googleMap = null;
        const mapContainer = document.getElementById('map');
        mapContainer.innerHTML = ''; // 清除地图容器的内容
      }
      this.loadMap(); // 重新加载地图
    },
    plot(data) {
      this.clearMap(); // 在绘制新数据之前清除地图

      // 使用 setTimeout 确保地图加载完成后再执行绘制
      setTimeout(() => {
        data.plans.forEach(plan => {
          const carColor = this.getCarColor(plan.car.carId);
          const routePath = plan.path.map(p => ({ lat: p.location.latitude, lng: p.location.longitude }));
          const carStandstill = { lat: plan.car.location.latitude, lng: plan.car.location.longitude };

          // 将车辆初始位置与路径点连接
          const fullPath = [carStandstill, ...routePath]; // 完整路径包括起始点和路径点

          // 创建多个Polyline，每个只连接相邻的两点
          for (let i = 0; i < fullPath.length - 1; i++) {
            const segment = new google.maps.Polyline({
              path: [fullPath[i], fullPath[i + 1]],
              geodesic: true,
              strokeColor: carColor,
              strokeOpacity: 1.0,
              strokeWeight: 2
            });
            segment.setMap(this.googleMap);
            this.mapOverlays.push(segment);
          }

          // 特别标记车辆的初始位置，使用红色箭头标记
          const carMarker = new google.maps.Marker({
            position: carStandstill,
            map: this.googleMap,
            icon: {
              path: google.maps.SymbolPath.BACKWARD_CLOSED_ARROW,
              scale: 5,
              fillColor: 'red', // 红色填充
              fillOpacity: 1,
              strokeColor: '#000',  // 黑色描边
              strokeWeight: 2
            }
          });
          this.mapOverlays.push(carMarker);

          // 标记路线上的其他点
          routePath.forEach(loc => {
            const pointMarker = new google.maps.Marker({
              position: loc,
              map: this.googleMap,
              icon: {
                path: google.maps.SymbolPath.CIRCLE,
                scale: 4,
                fillColor: carColor,
                fillOpacity: 1,
                strokeWeight: 1
              }
            });
            this.mapOverlays.push(pointMarker);
          });
        });
      }, 500); // 设置延迟500毫秒后执行绘制操作
    }
  }
}
</script>