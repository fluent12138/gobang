<template>
  <div ref="parent" class="gameMap">
    <canvas ref="canvas" tabindex="0"></canvas>
  </div>
</template>

<script>
import { OnePutMap } from '../assets/scripts/gobang/OnePutMap.js'
import { onMounted, ref } from 'vue'
import { useOnePutStore } from '../store'
export default {
  setup () {
    let parent = ref(null);
    let canvas = ref(null);
    const oneputStore = useOnePutStore();
    onMounted(() => {
      let levelMap = new OnePutMap(canvas.value.getContext('2d'), parent.value, oneputStore);
      oneputStore.updateLevelMap(levelMap);
    })

    return {
      parent,
      canvas,
    }
  }
}
</script>

<style scoped>
div.gameMap {
  width: 100%;
  height: 50vh;
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