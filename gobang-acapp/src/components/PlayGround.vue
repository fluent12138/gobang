<template>
  <div class="playground">
    <GameMap />
  </div>
</template>

<script>
import GameMap from './GameMap.vue'
// import { useUserStore } from '../store'
// import { onMounted, ref } from 'vue';
export default {
  components: {
    GameMap
  },
  setup () {
    // const userStore = useUserStore();
    // let playground = ref("");

    // // onMounted(() => {
    // //   console.log(userStore.screenH);
    // //   if (userStore.screenH) {
    // //     playground.value = "playground-t";
    // //   } else {
    // //     playground.value = "playground-l";
    // //   }
    // // })

    // return {
    //   playground,
    // }
  }
}
</script>


<style scoped>
.playground-t {
  width: 41.8vh;
  height: 50vh;
}
.playground-l {
  width: 43vh;
  height: 50vh;
}
.playground {
  height: 100%;
  width: 100%;
}
</style>
