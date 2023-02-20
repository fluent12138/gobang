<template>
  <n-grid :cols="1" class="random-match">
    <n-grid-item class="random-match-n-grid-item">
      <n-gradient-text
        type="info"
        class="timer"
        v-if="match_btn_info === '匹配中'"
      >
        {{ waiting_time }} s
      </n-gradient-text>
    </n-grid-item>
    <n-grid-item class="random-match-n-grid-item">
      <DoYouKnowCarousel />
    </n-grid-item>
    <n-grid-item class="random-match-n-grid-item">
      <n-spin size="small" :show="show_spin">
        <n-button
          strong
          secondary
          round
          size="large"
          type="info"
          class="match-btn"
          @click="matching"
        >
          {{ match_btn_info }}
        </n-button>
      </n-spin>
      <n-button
        size="tiny"
        secondary
        round
        type="default"
        class="match-false-btn"
        v-if="match_btn_info === '匹配中'"
        @click="match_take_away"
        >取消</n-button
      >
    </n-grid-item>
  </n-grid>
</template>

<script>
import DoYouKnowCarousel from '../components/DoYouKnowCarousel.vue'
import { NButton, NGradientText, NGrid, NGridItem, NSpin } from 'naive-ui'
import { ref } from 'vue'
import { useGobangStore, useUserStore } from '../store'
import WsEvent from '../utils/WsEvent.js'
export default {
  name: "RandomMatch",
  components: {
    NButton, NGradientText, NGrid, NGridItem, NSpin,
    DoYouKnowCarousel
  },
  setup () {
    const gobangStore = useGobangStore();
    const userStore = useUserStore();
    const ws_event = new WsEvent();
    let show_spin = ref(false);
    let timer = null;
    let waiting_time = ref(0);
    let match_btn_info = ref("开始匹配");
    const matching = () => {
      show_spin.value = !show_spin.value;
      set_waiting_time();
      send_match_message();
      if (match_btn_info.value === "开始匹配") {
        match_btn_info.value = "匹配中";
      }
    }
    const match_take_away = () => {
      back_waiting_time();
      send_stopmatch_message();
      show_spin.value = !show_spin.value;
      match_btn_info.value = "开始匹配";
    }

    const set_waiting_time = () => {
      timer = setInterval(() => {
        waiting_time.value++;
      }, 1000)
      userStore.updateTimer(timer);
    }
    const back_waiting_time = () => {
      waiting_time.value = 0;
      clearInterval(timer);
    }
    const send_match_message = () => {
      const message = getSendMsg(ws_event.MATCH);
      gobangStore.socket.send(message);
    }

    const send_stopmatch_message = () => {
      const message = getSendMsg(ws_event.STOPMATCH);
      gobangStore.socket.send(message);
    }

    const getSendMsg = (event) => {
      let msg = null;
      switch (event) {
        case ws_event.MATCH:
          msg = getMatchMsg();
          break;
        case ws_event.STOPMATCH:
          msg = getStopMatchMsg();
          break;
        case ws_event.PUT:
          msg = getPutMsg();
          break;
        default:
          break;
      }
      return msg;
    }
    const getMatchMsg = () => {
      return JSON.stringify({
        event: "match",
      })
    }
    const getStopMatchMsg = () => {
      return JSON.stringify({
        event: "stopmatch",
      })
    }
    const getPutMsg = () => {
      return JSON.stringify({
        event: "put",
      })
    }
    return {
      show_spin,
      match_btn_info,
      waiting_time,
      matching,
      match_take_away,
    }
  }
}
</script>

<style scoped>
@import "../assets/font/font.css";
.random-match {
  box-sizing: border-box;
  margin-top: 3vh;
  height: 27vh;
  border-radius: 4vh;
  background-color: rgba(153, 223, 229, 0.3);
}
.random-match-n-grid-item {
  padding-top: 1vh;
  text-align: center;
  font-family: SmileySans;
}
.timer {
  font-size: 20px;
}
.match-notice-msg {
  font-size: 18px;
}
.match-btn:hover {
  transform: scale(1.2);
  transition: 200ms;
}
.match-false-btn {
  margin-top: 10px;
}
</style>