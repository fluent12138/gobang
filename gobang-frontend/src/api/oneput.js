import { get, post } from '../utils/request';

export default class OnePut {
  static async getTopThree () {
    return get('/oneput/topThree');
  }

  static async getRankList () {
    return get('/oneput/rankList');
  }

  static async getOneputList () {
    return get('/oneput/list');
  }

  static async getSelfInfo () {
    return get('/oneput/selfInfo');
  }

  static async checkAns (ans) {
    return post('/oneput/check', ans);
  }

  static async page (page) {
    return get('/oneput/page', { page });
  }

  static async getReputationList () {
    return get('/oneput/reputationList');
  }

  static async getSelfReputation () {
    return get('/oneput/reputation');
  }

  static async getAdminReputation () {
    return get('/admin/oneput/list');
  }

  static async addLevelMap (level) {
    return post('/oneput/add', level);
  }

  static async updateLevelMap (level) {
    return post('/oneput/update', level);
  }

  static async adminUpdateLevel (level) {
    return post('/admin/oneput/update', level);
  }

  static async adminDeleteLevel (level) {
    return post('/admin/oneput/delete', level);
  }

  static async deleteLevel (level) {
    return post('/oneput/delete', level);
  }

  static async getOnePutMap (levelId) {
    return get('/oneput/map', { levelId });
  }
}
