<template>
  <div ref="parent" :class="gameMapStyle">
    <canvas ref="canvas" tabindex="0"></canvas>
  </div>
</template>

<script>
import { OnePutMap } from '../assets/scripts/gobang/OnePutMap.js'
import { onMounted, ref } from 'vue'
import { useOnePutStore, useUserStore } from '../store'
export default {
  setup () {
    let parent = ref(null);
    let canvas = ref(null);
    let gameMapStyle = ref("");
    const oneputStore = useOnePutStore();
    const userStore = useUserStore();
    onMounted(() => {
      if (userStore.screenH) {
        gameMapStyle.value = "gameMap-t";
      } else {
        gameMapStyle.value = "gameMap-l";
      }
      let levelMap = new OnePutMap(canvas.value.getContext('2d'), parent.value, oneputStore);
      oneputStore.updateLevelMap(levelMap);
    })

    return {
      parent,
      canvas,
      gameMapStyle
    }
  }
}
</script>

<style scoped>
.gameMap-t {
  width: 41.7vh;
  height: 40vh;
  display: flex;
  justify-content: center;
  align-items: center;
}
.gameMap-l {
  width: 50vh;
  height: 40vh;
  display: flex;
  justify-content: center;
  align-items: center;
}
canvas:hover {
  cursor: pointer;
}
canvas:focus {
  outline: none;
}
</style>