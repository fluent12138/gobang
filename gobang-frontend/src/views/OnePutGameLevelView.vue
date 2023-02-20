<template>
  <Back />
  <Vue3Lottie
    v-if="animation_start_game"
    class="animation-strat-game"
    :animationData="countDown"
    :loop="false"
    :speed="1.1"
    @onComplete="start_count_down()"
  />
  <div v-show="!animation_start_game">
    <n-space justify="space-around" class="level-info">
      <n-gradient-text type="info"
        >第{{ onePutStore.levelId }}关</n-gradient-text
      >
      <n-gradient-text v-if="!onePutStore.over" type="info"
        >last: {{ count_down }}s</n-gradient-text
      >
      <n-gradient-text v-else type="info"
        >last: {{ last_count_down }}s</n-gradient-text
      >
    </n-space>
    <PlayGround class="playground" />
    <InfoDisplay :user="userInfo" />
  </div>
</template>

<script>
import Back from '../components/Back.vue'
import back from '../utils/routerBack'
import PlayGround from '../components/PlayGround.vue'
import InfoDisplay from '../components/InfoDisplay.vue'
import OnePut from '../api/oneput'
import notice from '../utils/notice'
import countDown from '../assets/img/json/countDown.json'
import { useOnePutStore } from '../store'
import { NGradientText, NSpace } from 'naive-ui'
import { computed, onMounted, onUnmounted, ref } from 'vue'
export default {
  components: {
    Back, PlayGround, InfoDisplay,
    NGradientText, NSpace
  },
  setup () {
    let onePutStore = useOnePutStore();
    let count_down = ref(computed(() => onePutStore.passTime));
    let animation_start_game = ref(true);
    let last_count_down = ref(0);
    let userInfo = ref({});
    let timer = null;
    onMounted(() => {
      onePutStore.updateOver(false);
      onePutStore.updatePassTime(10);

      let res = OnePut.getSelfInfo();
      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          convert(data.data);
        } else {
          let notice_msg = data.message;
          if (data.description !== '') {
            notice_msg = data.description;
          }
          notice(notice_msg, 2e3, "error");
        }
      }).catch((resp) => {
        console.log(resp);
        notice("系统繁忙", 2e3, "error");
      })
    })
    onUnmounted(() => {
      clearInterval(timer);
    })
    const start_count_down = () => {
      animation_start_game.value = false;
      timer = setInterval(() => {
        if (!onePutStore.over && count_down.value > 0) {
          last_count_down.value = count_down.value;
        } else {
          if (count_down.value > 0) {
            last_count_down.value = count_down.value;
          } else {
            last_count_down.value = 0;
          }
          clearInterval(timer);
          if (onePutStore.over) return false;
          onePutStore.updateOver(true);
          notice("timeout!", 2000, "warning");

          setTimeout(() => {
            notice("正在跳转..", 1500, "success");
            setTimeout(() => {
              back();
            }, 1000);
          }, 2000);
        }
        // 更新倒计时
        onePutStore.updatePassTime(parseFloat(onePutStore.passTime - 0.09).toFixed(2));
      }, 90)
    }
    const convert = (data) => {
      userInfo.value = {
        ...data,
        rate: data.passCount + " 关 - " + data.passTime + "s",
      }
    }
    return {
      countDown,
      onePutStore, count_down, start_count_down,
      animation_start_game,
      userInfo, last_count_down
    }
  }
}
</script>

<style scoped>
@import "../assets/font/font.css";
.level-info {
  margin-top: 3vh;
  font-size: 24px;
  user-select: none;
  font-family: SmileySans;
}
.animation-strat-game {
  position: absolute;
  z-index: 0;
}
</style>