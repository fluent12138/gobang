import { defineStore } from 'pinia';

export const useOnePutStore = defineStore('oneput', {
  state: () => ({
    levelId: null,
    gamemap: null,
    passTime: 10,
    page: 1,
    over: false,
    levelObject: null, // 修改时保存信息
    levelMap: null,
  }),
  actions: {
    updateLevelId (levelId) {
      this.levelId = levelId;
    },
    updateOnePutGameMap (map) {
      this.gamemap = map;
    },
    updatePassTime (time) {
      this.passTime = time;
    },
    updateOver (over) {
      this.over = over;
    },
    updateOnePutPage (page) {
      this.page = page;
    },
    updateLevelMap (map) {
      this.levelMap = map;
    },
    updateLevelObject (levelObj) {
      this.levelObject = levelObj;
    }
  },
});
