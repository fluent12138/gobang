import { createPinia } from 'pinia';

const store = createPinia();

export default store;

export * from './modules/router';
export * from './modules/user';
export * from './modules/gobang';
export * from './modules/oneput';
export * from './modules/home';
