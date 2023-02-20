package com.lele.gobang.controller.life;

import com.google.gson.JsonObject;
import com.lele.gobang.common.ErrorCode;
import com.lele.gobang.common.R;
import com.lele.gobang.common.ResultUtils;
import com.lele.gobang.model.domain.LifeUser;
import com.lele.gobang.service.AcAppService;
import com.lele.gobang.service.LifeUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * acapp life 调用api
 *
 * @author lele
 * @create 2023-01-18 11:44
 */
@RestController
@RequestMapping("/life/")
@RequiredArgsConstructor
@Slf4j
public class LifeController {
    private final AcAppService acAppService;
    private final LifeUserService lifeUserService;

    @GetMapping("/acwing/acapp/apply_code/")
    public String applyCode() {
        JsonObject resp = acAppService.applyCodeForLife();
        return resp.toString();
    }

    @GetMapping("/acwing/acapp/receive_code/")
    public String receiveCode(@RequestParam Map<String, String> data) {
        String code = data.get("code");
        String state = data.get("state");
        JsonObject resp = acAppService.receiveCodeForLife(code, state);
        return resp.toString();
    }

    @PostMapping("save")
    public R save(@RequestBody LifeUser lifeUser) {
        log.info("life user : {}", lifeUser);
        if (lifeUser == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean save = lifeUserService.updateById(lifeUser);
        return ResultUtils.success(save);
    }
}
