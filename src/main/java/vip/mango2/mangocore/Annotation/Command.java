package vip.mango2.mangocore.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

    /* 指令名称 */
    String name();

    /* 简写指令 */
    String[] alias() default {};

    /* 指令描述 */
    String description() default "";

    /* 指令权限 */
    String permission() default "";

    /* 指令使用方式 */
    String usage() default "";

}
