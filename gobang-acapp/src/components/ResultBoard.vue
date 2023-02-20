<template>
  <div class="result-board">
    <Vue3Lottie
      v-if="gobangStore.loser === 'all'"
      :animationData="draw"
      :loop="false"
      :speed="1"
      :height="animation_height"
      @onComplete="onComplete"
    />
    <Vue3Lottie
      v-else-if="
        (gobangStore.loser === 'A' &&
          userStore.user.id === parseInt(gobangStore.a_id)) ||
        (gobangStore.loser === 'B' &&
          userStore.user.id === parseInt(gobangStore.b_id))
      "
      :animationData="lose"
      :loop="false"
      :speed="1"
      :height="animation_height"
      @onComplete="onComplete"
    />
    <Vue3Lottie
      v-else
      :animationData="win"
      :loop="false"
      :speed="1"
      :height="animation_height"
      @onComplete="onComplete"
    />
  </div>
</template>

<script>
import win from '../assets/img/json/win.json'
import lose from '../assets/img/json/lose.json'
import draw from '../assets/img/json/draw.json'
import notice from '../utils/notice'
import find from '../utils/findRouter'
import { ref } from 'vue'
import { useRouterStore, useGobangStore, useUserStore } from '../store'
export default {
  name: "ResultBoard",
  setup () {
    let animation_height = ref(0);

    animation_height.value = window.innerHeight - 100;
    const routerStore = useRouterStore();
    const gobangStore = useGobangStore();
    const userStore = useUserStore();
    const onComplete = () => {
      gobangStore.clearGobangInfo();
      notice("准备跳转", 1500, "success");
      setTimeout(() => {
        routerStore.updateRouterName(find(routerStore.router_name));
      }, 1500);
    }
    return {
      win, lose, draw,
      animation_height, gobangStore, userStore,
      onComplete,
    }
  }
}
</script>

<style scoped>
.result-board {
  position: absolute;
  z-index: 1;

  top: -12vh;
}
</style>