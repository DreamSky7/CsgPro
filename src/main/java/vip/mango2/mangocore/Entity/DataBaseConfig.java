package vip.mango2.mangocore.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataBaseConfig {
    private String connect;
    private int port;

    private String dataBase;

    private String user;

    private String password;

}
