<template>
  <div class="random-pk-game">
    <Back />
    <GameInfo class="game-info" :gameInfo="gameInfo" :mode="'pk'" />
    <RankListCarousel class="ranklist-carousel" :mode="'pk'" />
    <SelfRecord />
    <RandomMatch />
  </div>
</template>

<script>
import Back from '../components/Back.vue'
import GameInfo from '../components/GameInfo.vue'
import RankListCarousel from '../components/RankLIstCarousel.vue'
import RandomMatch from '../components/RandomMatch.vue'
import SelfRecord from '../components/SelfRecord.vue'
import Ws from '../utils/Ws'
import WsEvent from '../utils/WsEvent'
import { useUserStore, useGobangStore, useRouterStore } from '../store'
import { useMessage } from 'naive-ui'
import { onMounted } from 'vue'
export default {
  name: "RandomPkView",
  components: {
    Back, GameInfo, RankListCarousel, RandomMatch, SelfRecord,
    useMessage,
  },
  setup () {
    const [routerStore, userStore, gobangStore] = [useRouterStore(), useUserStore(), useGobangStore()];
    const gameInfo = {
      user_avatar: userStore.user.avatar,
      game_id: "1",
    }
    const userid = parseInt(userStore.user.id);
    const socketUrl = `ws://localhost:10020/api/websocket/${userid}`;
    const ws_event = new WsEvent();
    let socket = null;
    onMounted(() => {
      window.$message = useMessage();
      socket = new Ws(socketUrl);
      gobangStore.updateSocket(socket);

      socket.ws.onmessage = msg => {
        const data = JSON.parse(msg.data);
        switch (data.event) {
          case ws_event.PLAYING:
            on_playing(data);
            break;
          default:
            break;
        }
      }
    })

    const on_playing = (data) => {
      // 改变路由
      routerStore.updateLastRouterName(routerStore.router_name);
      routerStore.updateRouterName("pk");
      let round = (userStore.user.id === data.game.a_id);
      let gameInfo = {
        opponent_photo: data.opponent_photo,
        opponent_rate: data.opponent_rate,
        opponent_username: data.opponent_username,
        a_id: data.game.a_id,
        b_id: data.game.b_id,
        map: data.game.map,
        round,
      };
      gobangStore.updateGameStartInfo(gameInfo);
    }
    return {
      gameInfo
    }
  }
}
</script>

<style scoped>
.random-pk-game {
  user-select: none;
}
.game-info {
  margin-bottom: 5vh;
}
.ranklist-carousel {
  margin-bottom: 5vh;
}
</style>