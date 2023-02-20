package com.lele.gobang;

import cn.xuyanwu.spring.file.storage.EnableFileStorage;
import com.lele.gobang.service.impl.MatchServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableFileStorage
@SpringBootApplication
public class GobangApplication {

    public static void main(String[] args) {
        SpringApplication.run(GobangApplication.class, args);
        MatchServiceImpl.MATCHING_POOL.start();
    }

}
