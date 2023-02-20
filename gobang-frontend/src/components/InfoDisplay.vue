<template>
  <n-space justify="space-evenly" class="info-display">
    <NAvatar
      class="avatar"
      :size="40"
      :src="user.avatar"
      @click="updateAvatar"
    />
    <n-modal
      v-model:show="avatarModal"
      preset="card"
      title="更新头像"
      style="width: 360px"
      size="huge"
      :bordered="false"
    >
      <n-space justify="center" align="center">
        <n-avatar round :size="120" :src="user.avatar" />
        <n-upload
          action="http://localhost:10020/api/user/avatar/upload"
          with-credentials
          :on-finish="uploadAvatarFinish"
        >
          <n-button strong secondary round type="default">更新头像</n-button>
        </n-upload>
      </n-space>
    </n-modal>
    <n-gradient-text class="rate" :size="24" type="info" v-if="user.username">
      {{ user.username }}
    </n-gradient-text>
    <n-gradient-text class="rate" :size="24" type="warning">
      {{ rating }}{{ user.rate }}
    </n-gradient-text>
    <n-gradient-text :size="24" v-if="user.home">
      <span v-if="user.username !== userStore.user.username">
        {{ homeStore.opponent_ready }}
      </span>
      <span v-else>{{ homeStore.selfReady }}</span>
      <n-button
        v-if="user.username === userStore.user.username"
        strong
        secondary
        round
        type="info"
        size="tiny"
        @click="onReady"
      >
        {{ readyBtn }}
      </n-button>
    </n-gradient-text>
  </n-space>
</template>

<script>
import { NGradientText, NAvatar, NSpace, NButton, NModal, NUpload } from 'naive-ui'
import { onMounted, ref } from 'vue'
import { useRouterStore, useHomeStore, useGobangStore, useUserStore } from '../store'
import User from '../api/user'
import notice from '../utils/notice'
export default {
  name: "InfoDisplay",
  props: {
    user: {
      type: Object,
    },
  },
  components: {
    NGradientText, NAvatar, NSpace, NButton, NModal, NUpload
  },
  setup (props) {
    let avatarModal = ref(false);
    let rating = ref("rate: ");
    let userinfo = ref(props.user);
    let readyBtn = ref("准备");
    const [routerStore, homeStore, gobangStore, userStore] = [useRouterStore(), useHomeStore(), useGobangStore(), useUserStore()];
    onMounted(() => {
      // 利用username区分是menu页面还是pk页面
      if (routerStore.router_name === "one_put_game_level") {
        rating.value = "";
      }
      if (!userinfo.value.username) {
        let res = User.getinfo();
        res.then(resp => {
          let data = resp.data;
          if (data.code === 0) {
            userinfo.value = {
              avatar: data.data.avatar,
              rate: data.data.rate,
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
    });
    const onReady = () => {
      if (readyBtn.value === "准备") {
        homeStore.updateSelfReady("已准备");
        readyBtn.value = "取消准备";
        sendReady();
      } else {
        homeStore.updateSelfReady("未准备");
        readyBtn.value = "准备";
        sendCancelReady();
      }
    }

    const sendReady = () => {
      gobangStore.socket.send(
        JSON.stringify({
          "event": "ready",
          "ready": 1,
        })
      );
    }

    const sendCancelReady = () => {
      gobangStore.socket.send(
        JSON.stringify({
          "event": "ready",
          "ready": 0,
        })
      );
    }

    const updateAvatar = () => {
      if (!props.user.username) {
        avatarModal.value = true;
      }
    }

    const uploadAvatarFinish = () => {
      let res = User.getinfo();
      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          notice("更新头像成功", 1.5e3, "success");
          userStore.updateAvatar(data.data.avatar);
          props.user.avatar = data.data.avatar;
          routerStore.updateRouterName("menu");
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
    return {
      rating, readyBtn, homeStore, userStore,
      onReady, updateAvatar, avatarModal, uploadAvatarFinish
    }
  }
}
</script>

<style scoped>
@import "../assets/font/font.css";
.info-display {
  user-select: none;
  font-family: SmileySans;
}
.avatar:hover {
  cursor: pointer;
  -webkit-animation: rotate-center 0.6s ease-in-out both;
  animation: rotate-center 0.6s ease-in-out both;
}
.rate:hover {
  cursor: pointer;
  -webkit-animation: jello-horizontal 0.9s both;
  animation: jello-horizontal 0.9s both;
}
@-webkit-keyframes rotate-center {
  0% {
    -webkit-transform: rotate(0);
    transform: rotate(0);
  }
  100% {
    -webkit-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@keyframes rotate-center {
  0% {
    -webkit-transform: rotate(0);
    transform: rotate(0);
  }
  100% {
    -webkit-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}

@-webkit-keyframes jello-horizontal {
  0% {
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }
  30% {
    -webkit-transform: scale3d(1.25, 0.75, 1);
    transform: scale3d(1.25, 0.75, 1);
  }
  40% {
    -webkit-transform: scale3d(0.75, 1.25, 1);
    transform: scale3d(0.75, 1.25, 1);
  }
  50% {
    -webkit-transform: scale3d(1.15, 0.85, 1);
    transform: scale3d(1.15, 0.85, 1);
  }
  65% {
    -webkit-transform: scale3d(0.95, 1.05, 1);
    transform: scale3d(0.95, 1.05, 1);
  }
  75% {
    -webkit-transform: scale3d(1.05, 0.95, 1);
    transform: scale3d(1.05, 0.95, 1);
  }
  100% {
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }
}
@keyframes jello-horizontal {
  0% {
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }
  30% {
    -webkit-transform: scale3d(1.25, 0.75, 1);
    transform: scale3d(1.25, 0.75, 1);
  }
  40% {
    -webkit-transform: scale3d(0.75, 1.25, 1);
    transform: scale3d(0.75, 1.25, 1);
  }
  50% {
    -webkit-transform: scale3d(1.15, 0.85, 1);
    transform: scale3d(1.15, 0.85, 1);
  }
  65% {
    -webkit-transform: scale3d(0.95, 1.05, 1);
    transform: scale3d(0.95, 1.05, 1);
  }
  75% {
    -webkit-transform: scale3d(1.05, 0.95, 1);
    transform: scale3d(1.05, 0.95, 1);
  }
  100% {
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }
}
</style>