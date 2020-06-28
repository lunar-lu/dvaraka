package org.lunarlu.dvaraka.web.di;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.lunarlu.dvaraka.web.annotation.GuiceModule;
import org.lunarlu.dvaraka.web.common.DvarakaException;
import org.lunarlu.dvaraka.web.meta.ServerMeta;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.stream.Collectors;

/**
 * @author jiaoteng
 */
public class GuiceDependencyManager implements DependencyManager {

    private static final GuiceDependencyManager DEPENDENCY_MANAGER = new GuiceDependencyManager();

    private final Reflections reflections;

    private Injector injector;

    public GuiceDependencyManager() {
        this.reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackages(ServerMeta.instance().getBaseClass().getPackage().getName())
        );
    }

    @Override
    public void scan() {
        this.injector = Guice.createInjector(
                reflections.getTypesAnnotatedWith(GuiceModule.class)
                        .stream()
                        .map(clazz -> {
                            try {
                                return (AbstractModule) clazz.newInstance();
                            } catch (Exception e) {
                                throw new DvarakaException(e);
                            }
                        })
                        .collect(Collectors.toList())
        );
    }

    @Override
    public <T> T getInstance(Class<T> clazz) {
        return injector.getInstance(clazz);
    }
}
