import { useRouterStore } from '../store';
import find from './findRouter'
const back = () => {
  const routerStore = useRouterStore();
  let last_router_name = find(routerStore.router_name);
  routerStore.updateRouterName(last_router_name);
}

export default back;
