<template>
  <div class="gobang-index-info">
    <div class="gobang-index-info-header">
      <InfoDisplay :user="user" />
    </div>
    <n-space justify="center" class="gobang-index-info-welcome">
      <n-gradient-text :size="28" type="success">
        Welcome {{ userStore.user.username }}~
      </n-gradient-text>
    </n-space>
    <n-space vertical class="gobang-index-info-menu">
      <n-space vertical align="center">
        <div
          class="gobang-index-info-menu-item"
          @click="updateRouterName('one_put_game')"
        >
          神之一手
        </div>
        <div
          class="gobang-index-info-menu-item"
          @click="updateRouterName('random_pk')"
        >
          随机对战
        </div>
        <div
          class="gobang-index-info-menu-item"
          @click="updateRouterName('play_ground')"
        >
          自定义对局
        </div>
      </n-space>

      <n-space class="system-info" justify="end">
        <n-statistic label="当前在线人数" tabular-nums>
          <n-number-animation
            ref="numberAnimationInstRef"
            :from="0"
            :to="onlineCount"
          />
          <template #suffix>
            人
            <n-button
              size="tiny"
              strong
              secondary
              round
              type="default"
              @click="refreshOnlineCount"
              >刷新一下</n-button
            >
          </template>
        </n-statistic>
      </n-space>
    </n-space>
  </div>
</template>

<script>
import InfoDisplay from '../components/InfoDisplay.vue'
import notice from '../utils/notice'
import User from '../api/user'
import { useRouterStore, useUserStore, useGobangStore } from '../store';
import { NGradientText, NButton, NSpace, NStatistic, NNumberAnimation } from 'naive-ui'
import { onMounted, ref } from 'vue'
export default {
  name: 'MenuView',
  components: {
    NGradientText, NStatistic, NNumberAnimation,
    NButton,
    NSpace,
    InfoDisplay,
  },
  setup () {
    let onlineCount = ref(0);
    const [routerStore, userStore, gobangStore] = [useRouterStore(), useUserStore(), useGobangStore()];
    const updateRouterName = router_name => routerStore.updateRouterName(router_name);
    const user = ref({
      avatar: userStore.user.avatar,
      rate: userStore.user.rate,
    });

    onMounted(() => {
      if (gobangStore.socket) {
        gobangStore.socket.close();
      }
      updateUser();
      updateOnlineCount();
    })

    const updateUser = () => {
      let res = User.getinfo();;
      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          userStore.updateSelfRecord(data.data);
          user.value = {
            avatar: userStore.user.avatar,
            rate: userStore.user.rate,
          }
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
    const updateOnlineCount = () => {
      let res = User.getOnlineCount();
      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          onlineCount.value = data.data;
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

    const refreshOnlineCount = () => {
      updateOnlineCount();
      notice("刷新成功", 1500, "success")
    }
    return {
      updateRouterName, refreshOnlineCount,
      user,
      userStore,
      onlineCount,
    };
  }
}
</script>

<style scoped>
@import "../assets/font/font.css";
.gobang-index-info {
  user-select: none;
  font-family: SmileySans;
}
.gobang-index-info-header {
  height: 5vh;
  padding-top: 3vh;
}
.gobang-index-info-welcome {
  font-family: SmileySans;
  margin-top: 5vh;
  -webkit-animation: tracking-in-contract-bck-top 1s
    cubic-bezier(0.215, 0.61, 0.355, 1) both;
  animation: tracking-in-contract-bck-top 1s cubic-bezier(0.215, 0.61, 0.355, 1)
    both;
}
.gobang-index-info-menu {
  margin-top: 6vh;
}
.gobang-index-info-menu-item {
  height: 6vh;
  width: 20vh;
  font-size: 24px;
  text-align: center;
  margin-top: 2vh;
  line-height: 6vh;
  border: 1px solid black;
  border-radius: 1vh;
}

.gobang-index-info-menu-item:hover {
  transform: scale(1.2);
  transition: 200ms;
  cursor: pointer;
}

.system-info {
  margin-top: 10vh;
}
@-webkit-keyframes tracking-in-contract-bck-top {
  0% {
    letter-spacing: 1em;
    -webkit-transform: translateZ(400px) translateY(-300px);
    transform: translateZ(400px) translateY(-300px);
    opacity: 0;
  }
  40% {
    opacity: 0.6;
  }
  100% {
    -webkit-transform: translateZ(0) translateY(0);
    transform: translateZ(0) translateY(0);
    opacity: 1;
  }
}
@keyframes tracking-in-contract-bck-top {
  0% {
    letter-spacing: 1em;
    -webkit-transform: translateZ(400px) translateY(-300px);
    transform: translateZ(400px) translateY(-300px);
    opacity: 0;
  }
  40% {
    opacity: 0.6;
  }
  100% {
    -webkit-transform: translateZ(0) translateY(0);
    transform: translateZ(0) translateY(0);
    opacity: 1;
  }
}
</style>