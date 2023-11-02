package vip.mango2.mangocore.Entity.test;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TClass {
    private String name;
    private TStudent student;
    private List<TStudent> listStudent;
}
