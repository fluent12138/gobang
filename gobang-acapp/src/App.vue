<template>
  <n-message-provider>
    <div class="game-body" id="appx">
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
import { useRouterStore, useUserStore, useGobangStore } from './store'
import { computed, onMounted } from 'vue'
import { NMessageProvider } from 'naive-ui'
import notice from './utils/notice'
import User from './api/user'
import MenuView from './views/MenuView.vue'
import OnePutGameView from './views/OnePutGameView.vue'
import PlayGroundView from './views/PlayGroundView.vue'
import RandomPkView from './views/RandomPkView.vue'
import GobangView from './views/GobangView.vue'
import LoginOrRegisterView from './views/LoginOrRegisterView.vue'
import OnePutGameLevelView from './views/OnePutGameLevelView.vue'
import OnePutReputationsView from './views/OnePutReputationsView.vue'
import OnePutSelfReputationView from './views/OnePutSelfReputationView.vue'
import OnePutLevelEditView from './views/OnePutLevelEditView.vue'
import HomeView from './views/HomeView.vue'
export default {
  components: {
    MenuView, OnePutGameView, PlayGroundView, RandomPkView, GobangView,
    LoginOrRegisterView, OnePutGameLevelView, HomeView, OnePutReputationsView,
    OnePutSelfReputationView, OnePutLevelEditView,
    NMessageProvider,
  },
  setup () {
    const routerStore = useRouterStore();
    const userStore = useUserStore();
    const gobangStore = useGobangStore();
    const router_name = computed(() => routerStore.router_name);

    onMounted(() => {
      window.addEventListener("beforeunload", () => {
        User.logout();
      });
      const vw = window.innerWidth, vh = window.innerHeight;
      userStore.updateScreen(vh);
      if (userStore.AcWingOS === "AcWingOS") return false;
      let res = User.thirdApplyCode();
      userStore.AcWingOS.api.window.on_close(() => {
        userStore.clearTimer();
        User.logout();
        if (gobangStore.socket) {
          gobangStore.socket.close();
        }
      })
      res.then(resp => {
        if (resp.data.result === "success") {
          if (userStore.screenH) {
            userStore.AcWingOS.api.window.resize(42 * vh / vw, 69);
          } else {
            userStore.AcWingOS.api.window.resize(50 * vh / vw, 75);
          }

          userStore.AcWingOS.api.oauth2.authorize(
            resp.data.appid,
            resp.data.redirect_uri,
            resp.data.scope,
            resp.data.state,
            resp => {
              resp = JSON.parse(resp);
              if (resp.result === "success") {
                userStore.updateUserInfo(resp.user);
                // 存储token
                localStorage.setItem('tokenValue', resp.token.tokenValue);
                setTimeout(() => {
                  routerStore.updateRouterName("menu");
                }, 1500);
              } else {
                userStore.AcWingOS.api.window.close();
              }
            }
          );
        } else {
          userStore.AcWingOS.api.window.close();
        }
      }).catch((resp) => {
        console.log(resp);
        notice("系统繁忙", 2e3, "error");
      })
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
.game-body {
  width: 100%;
  height: 100%;
}
</style>