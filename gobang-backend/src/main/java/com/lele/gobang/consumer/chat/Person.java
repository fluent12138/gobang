package com.lele.gobang.consumer.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lele
 * @create 2022-09-19 12:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private Integer id;
    private String content;
    // 0未准备 1已准备
    private Integer ready;
}
