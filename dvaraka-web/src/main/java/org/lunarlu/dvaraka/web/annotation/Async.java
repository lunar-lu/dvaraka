package org.lunarlu.dvaraka.web.annotation;

import java.lang.annotation.*;

/**
 * @author jiaoteng
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Async {
}
