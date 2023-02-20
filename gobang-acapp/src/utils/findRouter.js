import { useRouterStore } from '../store';
const find = (router_name) => {
  const routerStore = useRouterStore();
  let last_router_name = routerStore.last_router_name;
  let to_router_name = "";
  switch (router_name) {
    case "one_put_game":
      to_router_name = "menu";
      break;
    case "play_ground":
      to_router_name = "menu";
      break;
    case "random_pk":
      to_router_name = "menu";
      break;
    case "pk":
      to_router_name = last_router_name;
      break;
    case "one_put_game_level":
      to_router_name = last_router_name;
      break;
    case "one_put_reputations":
      to_router_name = last_router_name;
    case "one_put_self_reputation":
      to_router_name = last_router_name;
    case "one_put_level_edit":
      to_router_name = last_router_name;
    case "home":
      to_router_name = last_router_name;
      break;
    default:
      break;
  }
  return to_router_name;
}

export default find;
