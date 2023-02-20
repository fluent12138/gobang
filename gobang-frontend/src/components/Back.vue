<template>
  <n-icon class="back" size="30" @click="onBack">
    <svg
      xmlns="http://www.w3.org/2000/svg"
      xmlns:xlink="http://www.w3.org/1999/xlink"
      viewBox="0 0 16 16"
    >
      <g fill="none">
        <path
          d="M5.854 2.146a.5.5 0 0 1 0 .708L3.707 5h2.336c1.468 0 2.905 0 4.226.396c1.365.41 2.585 1.234 3.647 2.827a.5.5 0 0 1-.832.554c-.938-1.407-1.968-2.083-3.103-2.423C8.815 6.004 7.517 6 6 6H3.707l2.147 2.146a.5.5 0 1 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 0 1 .708 0zM8 14a2 2 0 1 0 0-4a2 2 0 0 0 0 4zm0-1a1 1 0 1 1 0-2a1 1 0 0 1 0 2z"
          fill="currentColor"
        ></path>
      </g>
    </svg>
  </n-icon>
</template>

<script>
import back from '../utils/routerBack'
import Home from '../api/home'
import { NButton, NIcon } from 'naive-ui'
import { useGobangStore, useUserStore, useHomeStore } from '../store'
export default {
  name: "Back",
  components: {
    NButton, NIcon
  },
  setup () {
    const gobangStore = useGobangStore();
    const userStore = useUserStore();
    const homeStore = useHomeStore();
    const onBack = () => {
      if (homeStore.home) {
        if (userStore.user.id === homeStore.aId) {
          let msg = JSON.stringify({
            "event": "masterQuit",
          })
          gobangStore.socket.send(msg)
        } else {
          homeStore.clearHomeInfo();
          let msg = JSON.stringify({
            "event": "memberQuit",
          })
          gobangStore.socket.send(msg)
        }
      }

      if (homeStore.aId !== null) {
        let res = Home.remove();
        res.then(resp => {
          let data = resp.data;
          if (data.code === 0) {
            homeStore.updateOnCreateHome({
              aId: null,
              homeId: null,
            });
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
      back();
    }
    return {
      onBack,
    }
  }
}
</script>

<style scoped>
.back {
  position: absolute;
  top: 1vh;
  right: 1vh;
  z-index: 2;
}
.back:hover {
  cursor: pointer;
}
</style>