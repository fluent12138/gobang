<template>
  <div :class="display" @click="enterGame">
    <div>第{{ info.levelId }}关</div>
  </div>
</template>

<script>
import { useRouterStore, useOnePutStore } from '../store'
import notice from '../utils/notice'
import OnePut from '../api/oneput'
export default {
  name: "GameLevel",
  props: {
    info: {
      type: Object,
      required: true,
    }
  },
  setup (props) {
    const routerStore = useRouterStore();
    const onePutStore = useOnePutStore();
    let display = props.info.isPass ? "gobang-level gobang-level-finished" : "gobang-level";
    const enterGame = () => {
      if (props.info.isPass) {
        notice("你已经通关啦", 1500, "success");
        return;
      }
      onePutStore.updateLevelId(props.info.levelId);
      getOnePutMap(props.info.levelId);
    }

    const getOnePutMap = (levelId) => {
      let res = OnePut.getOnePutMap(levelId);
      res.then(resp => {
        let data = resp.data;
        console.log(data);
        if (data.code === 0) {
          onePutStore.updateOnePutGameMap(data.data);
          routerStore.updateLastRouterName("one_put_game");
          setTimeout(() => {
            routerStore.updateRouterName("one_put_game_level");
          }, 200);
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
    }

    return {
      enterGame, display
    }
  }
}
</script>

<style scoped>
@import "../assets/font/font.css";
.gobang-level {
  height: 6vh;
  width: 10vh;
  border-radius: 3vh;
  background-color: lightblue;

  text-align: center;
  line-height: 6vh;
  font-size: 16px;
  font-family: SmileySans;
}
.gobang-level-finished {
  background-color: #bfefaa;
}

.gobang-level:hover {
  cursor: pointer;
  transform: scale(1.2);
  transition: 200ms;
}
</style>