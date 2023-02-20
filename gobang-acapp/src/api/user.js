import { post, get } from '../utils/request';

export default class User {
  /**
   * 登录
   * @param {String} username 用户名
   * @param {String} password 密码
   * @returns
   */
  static async login (username, password) {
    return post('/user/doLogin', {
      username,
      password,
    });
  }
  static async register (username, password, checkPassword) {
    return post('/user/doRegister', {
      username,
      password,
      checkPassword,
    });
  }

  static async logout (info) {
    return await post('/user/logout', null, { info });
  }

  static async getinfo () {
    return post('/user/info');
  }

  static async getPkTopThree () {
    return get('/user/pkTopThree');
  }

  static async getPkRankList (id) {
    return get('/user/pkRankList', { id });
  }

  static async getOnlineCount () {
    return get('/user/onlineCount');
  }

  static async thirdApplyCode () {
    return get('/user/account/acwing/acapp/apply_code/');
  }
}
