package com.lele.gobang.controller.user.acwing;

import com.google.gson.JsonObject;
import com.lele.gobang.service.AcAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author lele
 * @create 2022-10-09 20:33
 */
@RestController
@RequestMapping("/user/account")
@RequiredArgsConstructor
public class AcAppController {
    private final AcAppService acAppService;

    @GetMapping("/acwing/acapp/apply_code/")
    public String applyCode() {
        JsonObject resp = acAppService.applyCode();
        return resp.toString();
    }

    @GetMapping("/acwing/acapp/receive_code/")
    public String receiveCode(@RequestParam Map<String, String> data) {
        String code = data.get("code");
        String state = data.get("state");
        JsonObject resp = acAppService.receiveCode(code, state);
        return resp.toString();
    }
}
