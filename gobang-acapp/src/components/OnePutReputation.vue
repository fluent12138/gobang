<template>
  <n-space justify="space-around">
    <n-button
      strong
      secondary
      round
      type="primary"
      @click="toOnePutReputationsView"
    >
      贡献者
    </n-button>
    <n-icon size="30" color="#209DF5" @click="toOnePutLevelEditView">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        xmlns:xlink="http://www.w3.org/1999/xlink"
        viewBox="0 0 24 24"
      >
        <path
          d="M22 4c0-1.1-.9-2-2-2H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h14l4 4V4zm-2 13.17L18.83 16H4V4h16v13.17zM13 5h-2v4H7v2h4v4h2v-4h4V9h-4z"
          fill="currentColor"
        ></path>
      </svg>
    </n-icon>
    <n-button
      strong
      secondary
      round
      type="primary"
      @click="toOnePutSelfReputationView"
    >
      我的贡献
    </n-button>
  </n-space>
</template>

<script>
import { NSpace, NButton, NIcon } from 'naive-ui'
import { useRouterStore } from '../store'
export default {
  name: "OnePutReputation",
  components: {
    NSpace, NButton, NIcon
  },
  setup () {
    const routerStore = new useRouterStore();

    const toOnePutReputationsView = () => {
      routerStore.updateLastRouterName("one_put_game");
      setTimeout(() => {
        routerStore.updateRouterName("one_put_reputations");
      }, 200);
    }

    const toOnePutSelfReputationView = () => {
      routerStore.updateLastRouterName("one_put_game");
      setTimeout(() => {
        routerStore.updateRouterName("one_put_self_reputation");
      }, 200);
    }

    const toOnePutLevelEditView = () => {
      routerStore.updateLastRouterName("one_put_game");
      setTimeout(() => {
        routerStore.updateRouterName("one_put_level_edit");
      }, 200);
    }
    return {
      toOnePutSelfReputationView, toOnePutReputationsView, toOnePutLevelEditView,
    }
  }
}
</script>

<style scoped>
.n-icon:hover {
  cursor: pointer;
}
.n-icon {
  margin-top: 2px;
}
</style>