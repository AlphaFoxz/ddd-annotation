package gitee.com.ericfox.ddd_annotation;

import java.lang.annotation.*;

/**
 * Getter注解demo（实现lombok的生成getter）
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface DddPo {
}
