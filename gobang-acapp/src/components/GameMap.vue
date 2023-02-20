<template>
  <div ref="parent" :class="gameMapStyle">
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
import { useGobangStore, useUserStore } from '../store'
export default {
  setup () {
    let parent = ref(null);
    let canvas = ref(null);
    let gameMapStyle = ref("");
    const gobangStore = useGobangStore();
    const userStore = useUserStore();

    onMounted(() => {
      if (userStore.screenH) {
        gameMapStyle.value = "gameMap-t";
      } else {
        gameMapStyle.value = "gameMap-l";
      }
      let gameObject = new GameMap(canvas.value.getContext('2d'), parent.value, gobangStore);
      gobangStore.updateGameObject(gameObject);
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
  height: 50vh;
  display: flex;
  justify-content: center;
  align-items: center;
}
.gameMap-l {
  width: 50vh;
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