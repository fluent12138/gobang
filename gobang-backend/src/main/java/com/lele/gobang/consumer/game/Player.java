package com.lele.gobang.consumer.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 玩家
 *
 * @author lele
 * @create 2023-01-07 13:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer userId;
    private Integer rate;
    private String color;
    private Boolean isRound;
    private Integer waitingTime = 0;
    private List<Cell> self;

    public Player(Integer userId, Integer rate) {
        this.userId = userId;
        this.rate = rate;
    }

    public Player(Integer userId, String color, Boolean isRound, List<Cell> self) {
        this.userId = userId;
        this.color = color;
        this.isRound = isRound;
        this.self = self;
    }

    public String getSelfString() {
        StringBuilder res = new StringBuilder();
        for (Cell d : self) {
            res.append(d.x + "," + d.y + " ");
        }
        return res.toString();
    }

    public void set_put(int x, int y) {
        this.self.add(new Cell(x, y));
    }
}
