<template>
  <div ref="parent" class="gameMap">
    <canvas ref="canvas" tabindex="0"></canvas>
    <audio
      preload="auto"
      src="https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/501/put (mp3cut.net).mp3"
    ></audio>
  </div>
</template>

<script>
import { GameMap } from '../assets/scripts/gobang/GameMap.js'
import { onMounted, ref } from 'vue'
import { useGobangStore } from '../store'
export default {
  setup () {
    let parent = ref(null);
    let canvas = ref(null);
    const gobangStore = useGobangStore();

    onMounted(() => {
      let gameObject = new GameMap(canvas.value.getContext('2d'), parent.value, gobangStore);
      gobangStore.updateGameObject(gameObject);
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
  height: 100%;
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