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
  }),
  actions: {
    updateUserInfo (user) {
      this.user = user;
    },
    updateSelfRecord (user) {
      this.user.pkCount = user.pkCount;
      this.user.rate = user.rate;
    },
    updateAvatar (avatar) {
      this.user.avatar = avatar;
    }
  },
});
