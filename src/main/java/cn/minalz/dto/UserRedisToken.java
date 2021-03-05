package cn.minalz.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 存储在Redis中的用户信息
 */
@Data
public class UserRedisToken implements Serializable {

    // 当前登录用户的token
    private String token;
    // 当前用户的创建时间戳 后期可以根据这个时间戳进行踢出用户的拓展功能
    private Long timestamp;

    public UserRedisToken(String token, Long timestamp) {
        this.token = token;
        this.timestamp = timestamp;
    }

}
