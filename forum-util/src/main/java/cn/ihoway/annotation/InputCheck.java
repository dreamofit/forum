package cn.ihoway.annotation;

import java.lang.annotation.*;

/**
 * 输入类检查
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InputCheck {
    boolean check() default false; //开启检查，默认关
}
