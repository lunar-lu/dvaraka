package org.lunarlu.dvaraka.web.di;

/**
 * @author jiaoteng
 */
public interface DependencyManager {

    void scan();

    <T> T getInstance(Class<T> clazz);
}
