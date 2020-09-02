package cn.minalz.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 存储在Redis中的用户信息
 */
@Data
public class UserRedisToken implements Serializable {

    private String token;
    private Long timestamp;

    public UserRedisToken(String token, Long timestamp){
        this.token = token;
        this.timestamp = timestamp;
    }

}
