package com.lele.gobang.service.impl;

import com.lele.gobang.consumer.match.MatchingPool;
import com.lele.gobang.model.domain.User;
import com.lele.gobang.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lele
 * @create 2023-01-07 11:47
 */
@Slf4j
@Service
public class MatchServiceImpl extends Thread implements MatchService {
    public final static MatchingPool MATCHING_POOL = new MatchingPool();

    @Override
    public int addPlayer(User user) {
        log.info("add");
        MATCHING_POOL.addPlayer(user.getId(), user.getRate());
        return 0;
    }

    @Override
    public int removePlayer(User user) {
        log.info("remove");
        MATCHING_POOL.removePlayer(user.getId());
        return 0;
    }
}
