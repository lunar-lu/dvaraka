package org.lunarlu.dvaraka.web.annotation;

import java.lang.annotation.*;

/**
 * @author jiaoteng
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebResource {

    String value() default "";

    String path() default "";

    String[] produces() default "*/*";

    String[] consumes() default "*/*";
}
