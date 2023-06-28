package cn.ihoway.annotation;

import java.lang.annotation.*;

/**
 * 输入类内部字段检查
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InsideCheck {
    boolean mustInput() default false; //是否必输字段
    int maxLen() default 0; //字段长度限制
    String format() default "";//字段输入格式限制
    String ranges() default ""; //字符串范围选择，用英文逗号分割，如：post,get
    int minValue() default Integer.MAX_VALUE; //最小值,包含该值
    int maxValue() default Integer.MIN_VALUE;//最大值,包含该值
}
