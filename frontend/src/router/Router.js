import { createWebHashHistory } from "vue-router";
import { createRouter } from "vue-router";

import MainPage from "@/views/MainPage.vue";
import PathMapPage from "@/views/PathMapPage.vue";
import CargoMapPage from "@/views/CargoMapPage.vue";
import OpenLayersMapPage from "@/views/OpenLayersMapPage.vue";

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
    }
  ]
})

export default router;