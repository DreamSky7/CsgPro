package vip.mango2.mangocore.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TClass {

    private String name;

    private int grade;

    private List<TStudent> students;
}
