import { createWebHashHistory } from "vue-router";
import { createRouter } from "vue-router";

import MainPage from "@/views/MainPage.vue";
import PathMapPage from "@/views/PathMapPage.vue";
import CargoMapPage from "@/views/CargoMapPage.vue";
import OpenLayersMapPage from "@/views/OpenLayersMapPage.vue";
import VehicleRoutingMapPage from "@/views/VehicleRoutingMapPage.vue";
import OptaplannerGoogleMapPage from "@/views/OptaplannerGoogleMapPage.vue";

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: "/",
      component: MainPage
    },
    { path: "/pathMap",
      component: PathMapPage,
    },
    { path: "/cargoMap",
      component: CargoMapPage
    },
    {
      path: '/openlayersMap',
      component: OpenLayersMapPage
    },
    {
      path: '/vehicleroutingMap',
      component: VehicleRoutingMapPage
    },
    {
      path: '/optaplannerGoogleMap',
      component: OptaplannerGoogleMapPage
    }
  ]
})

export default router;