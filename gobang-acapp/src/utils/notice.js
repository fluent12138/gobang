import { useRouterStore } from '../store';
const notice = (msg, time, type) => {
  const routerStore = useRouterStore();
  let message = window.$message;
  switch (type) {
    case "success":
      message.success(
        msg,
        { duration: time }
      );
      break;
    case "error":
      if (msg === "未登录" && routerStore.router_name === "login_or_register") break;
      message.error(
        msg,
        { duration: time }
      );
      if (msg === "未登录") {
        routerStore.updateRouterName("login_or_register");
      }
      break;
    case "info":
      message.info(
        msg,
        { duration: time }
      );
      break;
    case "warning":
      message.warning(
        msg,
        { duration: time }
      );
      break;
    case "loading":
      message.loading(
        msg,
        { duration: time }
      );
      break;
    default:
      return;
  }
}

export default notice;
