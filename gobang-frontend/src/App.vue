<template>
  <n-message-provider>
    <div class="game-body">
      <LoginOrRegisterView v-if="router_name === 'login_or_register'" />
      <MenuView v-if="router_name === 'menu'" />
      <OnePutGameView v-if="router_name === 'one_put_game'" />
      <PlayGroundView v-if="router_name === 'play_ground'" />
      <RandomPkView v-if="router_name === 'random_pk'" />
      <OnePutGameLevelView v-if="router_name === 'one_put_game_level'" />
      <OnePutReputationsView v-if="router_name === 'one_put_reputations'" />
      <OnePutSelfReputationView
        v-if="router_name === 'one_put_self_reputation'"
      />
      <OnePutLevelEditView v-if="router_name === 'one_put_level_edit'" />
      <GobangView v-if="router_name === 'pk'" />
      <HomeView v-if="router_name === 'home'" />
    </div>
  </n-message-provider>
</template>

<script>
import { useRouterStore } from './store'
import { computed, onMounted, onUnmounted } from 'vue'
import { NMessageProvider } from 'naive-ui'
import User from './api/user'
import MenuView from './views/MenuView.vue'
import OnePutGameView from './views/OnePutGameView.vue'
import PlayGroundView from './views/PlayGroundView.vue'
import RandomPkView from './views/RandomPkView.vue'
import GobangView from './views/GobangView.vue'
import LoginOrRegisterView from './views/LoginOrRegisterView.vue'
import OnePutGameLevelView from './views/OnePutGameLevelView.vue'
import HomeView from './views/HomeView.vue'
import OnePutReputationsView from './views/OnePutReputationsView.vue'
import OnePutSelfReputationView from './views/OnePutSelfReputationView.vue'
import OnePutLevelEditView from './views/OnePutLevelEditView.vue'
export default {
  components: {
    MenuView, OnePutGameView, PlayGroundView, RandomPkView, GobangView,
    LoginOrRegisterView, OnePutGameLevelView, HomeView, OnePutReputationsView,
    OnePutSelfReputationView, OnePutLevelEditView,
    NMessageProvider,
  },
  setup () {
    const routerStore = useRouterStore();
    const router_name = computed(() => routerStore.router_name);

    onMounted(() => {
      window.addEventListener("beforeunload", (e) => {
        User.logout();
      });
    })

    return {
      router_name,
    };
  }
}
</script>

<style>
body {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
</style>