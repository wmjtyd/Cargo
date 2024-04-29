import { createApp } from "vue";
import App from "./App.vue";
import router from "./router/Router";
import axios from "axios";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";

axios.defaults.baseURL = "http://localhost:8080"

const app = createApp(App);
app.config.globalProperties.$http = axios;
app.use(router);
app.use(ElementPlus);
app.mount('#app');