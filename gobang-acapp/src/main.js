import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import Vue3Lottie from 'vue3-lottie'
import 'vue3-lottie/dist/style.css'

const app = createApp(App)
const pinia = createPinia();

app.use(Vue3Lottie).use(pinia);
app.mount('#app');
