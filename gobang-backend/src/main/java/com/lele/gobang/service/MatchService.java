package com.lele.gobang.service;

import com.lele.gobang.model.domain.User;

public interface MatchService {
    int addPlayer(User user);

    int removePlayer(User user);
}
