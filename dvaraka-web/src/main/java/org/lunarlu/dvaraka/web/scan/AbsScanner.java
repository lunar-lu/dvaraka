package org.lunarlu.dvaraka.web.scan;

import com.google.inject.Inject;
import io.vertx.core.Vertx;
import org.lunarlu.dvaraka.web.meta.ServerMeta;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

/**
 * @author jiaoteng
 */
public abstract class AbsScanner implements Scanner{

    protected final Reflections reflections;
    protected final Vertx vertx;
    private final Class<?> baseClass;

    @Inject
    public AbsScanner() {
        this.baseClass = ServerMeta.instance().getBaseClass();
        this.reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackages(baseClass.getPackage().getName())
        );
        this.vertx = ServerMeta.instance().getVertx();
    }
}
