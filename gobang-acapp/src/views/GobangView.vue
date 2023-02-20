<template>
  <Back />
  <Vue3Lottie
    v-if="animation_start_game"
    class="animation-strat-game"
    :animationData="pk"
    :loop="false"
    :speed="0.5"
    @onComplete="animation_start_game = false"
  />
  <div v-show="!animation_start_game">
    <ResultBoard v-if="gobangStore.loser !== 'none'" />
    <InfoDisplay class="info-display1" :user="opponentInfo" />
    <GameMap />
    <InfoDisplay class="info-display2" :user="userStore.user" />
    <div class="round-msg">
      <span>self: </span>
      <img :src="getSelf()" alt="" />
      <span> &nbsp;&nbsp;&nbsp; 当前回合: </span>
      <img :src="getRound()" alt="" />
    </div>
  </div>
</template>

<script>
import GameMap from '../components/GameMap.vue'
import Back from '../components/Back.vue'
import InfoDisplay from '../components/InfoDisplay.vue'
import ResultBoard from '../components/ResultBoard.vue'
import pk from '../assets/img/json/pk.json'
import WsEvent from '../utils/WsEvent'
import User from '../api/user'
import { useUserStore, useGobangStore } from '../store'
import { ref, onMounted, onUnmounted } from 'vue';
export default {
  name: "GobangView",
  components: {
    GameMap, Back, InfoDisplay, ResultBoard,
  },
  setup () {
    let animation_start_game = ref(true);
    let socket = null;
    const ws_event = new WsEvent();
    const userStore = useUserStore();
    const gobangStore = useGobangStore();
    const white = "https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/501/白子.png";
    const black = "https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/501/黑子.png";
    let opponentInfo = ref({
      avatar: gobangStore.opponent_photo,
      username: gobangStore.opponent_username,
      rate: gobangStore.opponent_rate,
    });

    onMounted(() => {
      socket = gobangStore.socket;

      socket.ws.onmessage = msg => {
        const data = JSON.parse(msg.data);
        switch (data.event) {
          case ws_event.PUT:
            on_put(data);
            break;
          case ws_event.RESULT:
            on_result(data);
            break;
          default:
            break;
        }
      }
    })

    onUnmounted(() => {
      // 异常退出
      if (gobangStore.a_id) {
        socket.ws.send(JSON.stringify({
          event: ws_event.GAME_QUIT,
        }))
      };
      // 等待后台操作完毕后更新数据
      setTimeout(() => {
        updateUser();
      }, 500);
    })

    const on_put = (data) => {
      const game = gobangStore.gameObejct;
      const round = gobangStore.round;
      const [gobang0, gobang1] = game.gobang;
      game.set_put_position(data.x, data.y);
      game.play_audio();
      gobang0.set_cells(data.cellsA);
      gobang1.set_cells(data.cellsB);
      gobangStore.updateGameMap(data.map);
      gobangStore.updateRound(!round);
    }

    const on_result = (data) => {
      gobangStore.updateLoser(data.loser);
    }

    const updateUser = () => {
      let res = User.getinfo();;
      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          userStore.updateSelfRecord(data.data);
        } else {
          let notice_msg = data.message;
          if (data.description !== '') {
            notice_msg = data.description;
          }
          notice(notice_msg, 2e3, "error");
        }
      }).catch(() => {
        notice("系统繁忙", 2e3, "error");
      })
    }

    const getSelf = () => {
      if (userStore.user.id === gobangStore.a_id) {
        // a => 黑子
        return black;
      } else {
        // b => 白子
        return white;
      }
    }

    const getRound = () => {
      if (gobangStore.round) {
        return getSelf();
      } else {
        return getSelf() === white ? black : white;
      }
    }
    return {
      userStore, gobangStore, pk, animation_start_game, opponentInfo,
      getSelf, getRound,
    }
  }
}
</script>

<style scoped>
@import "../assets/font/font.css";
.animation-strat-game {
  position: absolute;
  z-index: 0;
}
.round-msg {
  width: 20vh;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 3vh;
  border-radius: 1vh;
  background-color: rgba(152, 214, 243, 0.5);
  margin: 0 auto;
  font-family: SmileySans;

  -webkit-animation: heartbeat 1.5s ease-in-out infinite both;
  animation: heartbeat 1.5s ease-in-out infinite both;
}
.round-msg > span {
  line-height: 3vh;
  font-size: 14px;
}
.round-msg > img {
  height: 2vh;
  margin-left: 1vh;
}
@-webkit-keyframes heartbeat {
  from {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-transform-origin: center center;
    transform-origin: center center;
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  10% {
    -webkit-transform: scale(0.91);
    transform: scale(0.91);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  17% {
    -webkit-transform: scale(0.98);
    transform: scale(0.98);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  33% {
    -webkit-transform: scale(0.87);
    transform: scale(0.87);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  45% {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
}
@keyframes heartbeat {
  from {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-transform-origin: center center;
    transform-origin: center center;
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  10% {
    -webkit-transform: scale(0.91);
    transform: scale(0.91);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  17% {
    -webkit-transform: scale(0.98);
    transform: scale(0.98);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  33% {
    -webkit-transform: scale(0.87);
    transform: scale(0.87);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  45% {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
}
</style>
