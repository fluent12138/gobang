<template>
  <Back />
  <n-space vertical align="center" class="oneput-reputations">
    <n-gradient-text type="success" class="oneput-reputations-title">
      贡献者
    </n-gradient-text>
  </n-space>
  <n-space vertical>
    <n-space
      justify="space-between"
      align="center"
      class="oneput-reputations-info"
      v-for="reputation in reputations"
      :key="reputation.username"
    >
      <NAvatar :size="36" round :src="reputation.avatar" />
      <n-h2 class="oneput-reputations-info-username">{{
        reputation.username
      }}</n-h2>
      <n-h2 class="oneput-reputations-info-levelcount"
        >{{ reputation.levelCount }}关</n-h2
      >
    </n-space>
  </n-space>
</template>

<script>
import Back from '../components/Back.vue'
import { NSpace, NGradientText, NAvatar, NH2 } from 'naive-ui'
import { onMounted, ref } from 'vue';
import notice from '../utils/notice'
import OnePut from '../api/oneput'
export default {
  components: {
    Back,
    NSpace, NGradientText, NAvatar, NH2
  },
  setup () {
    let reputations = ref([]);

    onMounted(() => {
      pullReputations();
    })

    const pullReputations = () => {
      let res = OnePut.getReputationList();
      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          reputations.value = data.data;
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
      reputations,
    }
  }
}
</script>

<style scoped>
@import "../assets/font/font.css";
.n-space {
  user-select: none;
  font-family: SmileySans;
}
.oneput-reputations-title {
  margin-top: 2vh;
  margin-bottom: 1vh;
  font-size: 24px;
}
.n-h2 {
  margin-bottom: 0;
}
.oneput-reputations-info {
  border-radius: 1vh;
  background-color: rgba(158, 219, 172, 0.5);
}
.n-avatar {
  margin-top: 5px;
  margin-left: 20px;
}
.n-avatar:hover {
  cursor: pointer;
  -webkit-animation: rotate-center 0.6s ease-in-out both;
  animation: rotate-center 0.6s ease-in-out both;
}
.oneput-reputations-info-username {
  width: 20vh;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: pre;
  text-align: center;
}
.oneput-reputations-info-levelcount {
  margin-right: 20px;
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
</style>