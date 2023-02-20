import { defineStore } from 'pinia';

export const useHomeStore = defineStore('home', {
  state: () => ({
    selfReady: "未准备",
    opponent_username: "正在等待他人进入..",
    opponent_photo: "https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/501/defaultAvatar.jpg",
    opponent_rate: "未知",
    opponent_ready: "未准备",
    homeId: null,
    aId: null,
    bId: null,
    home: false, // 是不是在房间内
  }),

  actions: {
    updateHomeInfo (homeInfo) {
      this.opponent_username = homeInfo.opponent_username;
      this.opponent_photo = homeInfo.opponent_photo;
      this.opponent_rate = homeInfo.opponent_rate;
      this.homeId = homeInfo.homeId;
      this.aId = homeInfo.aId;
      this.bId = homeInfo.bId;
      this.home = true;
    },
    updateOpponentReady (ready) {
      this.opponent_ready = ready;
    },
    updateSelfReady (ready) {
      this.selfReady = ready;
    },
    updateOnCreateHome (info) {
      this.aId = info.aId;
      this.homeId = info.homeId;
    },
    clearHomeInfo () {
      this.selfReady = "未准备";
      this.opponent_username = "正在等待他人进入..";
      this.opponent_photo = "https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/501/defaultAvatar.jpg";
      this.opponent_rate = "未知";
      this.opponent_ready = "未准备";
      this.homeId = null;
      this.aId = null;
      this.bId = null;
      this.home = false;
    },
    clearOpponentInfo () {
      this.opponent_username = "正在等待他人进入..";
      this.opponent_photo = "https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/501/defaultAvatar.jpg";
      this.opponent_rate = "未知";
      this.opponent_ready = "未准备";
      this.bId = null;
      this.home = false;
    }
  },
});
