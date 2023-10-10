package vip.mango2.mangocore.Entity;

import lombok.Data;

import java.util.List;

@Data
public class TClass {

    private String name;

    private int grade;

    private List<TStudent> students;
}
