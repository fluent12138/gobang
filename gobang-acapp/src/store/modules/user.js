import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: {
      id: null,
      username: "",
      avatar: "",
      rate: "",
      pkCount: 0,
      is_login: false,
      role: 0,
    },
    AcWingOS: "AcWingOS",
    screenH: false,
    timer: [],
  }),
  actions: {
    updateUserInfo (user) {
      this.user = user;
    },
    updateSelfRecord (user) {
      this.user.pkCount = user.pkCount;
      this.user.rate = user.rate;
    },
    updateScreen (h) {
      if (h > 800) this.screenH = true;
      else this.screenH = false;
    },
    updateTimer (timer) {
      this.timer.push(timer);
    },
    clearTimer () {
      for (let i = 0; i < this.timer.length; i++) {
        clearInterval(this.timer[i]);
      }
      this.timer = [];
    },
    updateAvatar (avatar) {
      this.user.avatar = avatar;
    }
  },
});
