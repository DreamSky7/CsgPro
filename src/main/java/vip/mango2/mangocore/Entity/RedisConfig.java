package vip.mango2.mangocore.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RedisConfig {

    private String host;

    private Integer port;

    private String password;
}
