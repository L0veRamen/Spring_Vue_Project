import Vue from 'vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import locale from "../node_modules/element-ui/lib/locale/lang/en.js";
import App from './App.vue'
import router from './router'
import store from "./store";
import './assets/global.css'
import request from "./utils/request";

Vue.config.productionTip = false;
Vue.use(ElementUI, {locale});

Vue.prototype.request = request

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
