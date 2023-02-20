import { get, post } from '../utils/request';

export default class Home {
  static async craeteNoPwd () {
    return get('/home/create');
  }

  static async create (password) {
    return post('/home/create', null, { password });
  }

  static async remove () {
    return post('/home/remove');
  }

  static async joinNoPwd (homeId) {
    return get('/home/remove', { homeId });
  }

  static async join (homeId, password) {
    return post('/home/join', null, {
      homeId,
      password,
    });
  }

  static async page (page) {
    return get('/home/page', { page });
  }

  static async search (homeId) {
    return get('/home/search', { homeId });
  }
}
