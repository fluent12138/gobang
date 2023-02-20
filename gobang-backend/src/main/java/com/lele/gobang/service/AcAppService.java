package com.lele.gobang.service;

import com.google.gson.JsonObject;

/**
 * @author lele
 * @create 2023-01-13 8:38
 */
public interface AcAppService {

    JsonObject applyCode();

    JsonObject applyCodeForLife();

    JsonObject receiveCode(String code, String state);

    JsonObject receiveCodeForLife(String code, String state);
}
