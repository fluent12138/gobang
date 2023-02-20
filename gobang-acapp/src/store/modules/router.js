import { defineStore } from 'pinia';

export const useRouterStore = defineStore('router', {
  state: () => ({
    // router_name: "menu",
    router_name: "login_or_register",
    last_router_name: "menu",
  }),
  actions: {
    updateRouterName (router_name) {
      this.router_name = router_name;
    },
    updateLastRouterName (router_name) {
      this.last_router_name = router_name;
    }
  },
});
