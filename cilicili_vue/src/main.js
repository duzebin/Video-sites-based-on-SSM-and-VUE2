import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import header from '@/components/my-header'
import 'element-ui/lib/theme-chalk/index.css';
import '@/assets/global.css';
import '@/assets/iconfont/iconfont.css';
import '@/assets/iconfont/iconfont.js';

Vue.config.productionTip = false;
Vue.use(ElementUI);

Vue.component('my-header',header);


new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
