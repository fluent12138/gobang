package com.lele.gobang.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.google.gson.JsonObject;
import com.lele.gobang.model.domain.Home;

/**
 * @author 勒勒
 * @description 针对表【home(游戏房间表)】的数据库操作Service
 * @createDate 2023-01-10 15:39:55
 */
public interface HomeService extends IService<Home> {
    /**
     * 创建房间
     */
    Home createHome(String password);

    /**
     * 房主主动删除房间
     */
    int removeHome();

    /**
     * 系统删除房间
     */
    void removeHomeOnSocket(Integer id);

    /**
     * 加入房间
     */
    int joinHome(String homeId, String password);

    /**
     * 搜索房间
     */
    Home searchByHomeId(String homeId);

    /**
     * 房间信息分页展示
     */
    JsonObject page(Integer page);

    /**
     * 成员退出
     */
    void memberQuit(Integer id);

    /**
     * 房主退出房间
     */
    void masterQuit(Integer id);
}
