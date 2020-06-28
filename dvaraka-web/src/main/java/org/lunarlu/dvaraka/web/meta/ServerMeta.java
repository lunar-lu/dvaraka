package org.lunarlu.dvaraka.web.meta;

import com.google.common.collect.Maps;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.lunarlu.dvaraka.web.common.ValidationUtils;
import org.lunarlu.dvaraka.web.scan.ApplicationPathScanner;
import org.lunarlu.dvaraka.web.scan.RequestMappingScanner;

import java.util.Map;


/**
 * server context
 *
 * @author jiaoteng
 */
public class ServerMeta {

    private static final ServerMeta SERVER_META = new ServerMeta();
    private static final String DEFAULT_APPLICATION_PATH = "/";

    private ServerMeta() {
    }

    public static ServerMeta instance() {
        return SERVER_META;
    }

    private Vertx vertx;
    private Class<?> baseClass = ServerMeta.class;
    private String applicationPath = DEFAULT_APPLICATION_PATH;
    private Map<Class<?>, RequestMeta> mapping = Maps.newConcurrentMap();


    public ServerMeta setBaseClass(Class<?> clazz) {
        this.baseClass = clazz;
        return this;
    }

    public ServerMeta setApplicationPath(String applicationPath) {
        ValidationUtils.checkApplicationPath(applicationPath);
        this.applicationPath = applicationPath;
        return this;
    }

    public ServerMeta setVertx(Vertx vertx) {
        this.vertx = vertx;
        return this;
    }


    public Vertx getVertx() {
        return vertx;
    }

    public Class<?> getBaseClass() {
        return baseClass;
    }

    public String getApplicationPath() {
        return applicationPath;
    }

    public Map<Class<?>, RequestMeta> getMapping() {
        return mapping;
    }

    public Future<Void> scan() {
        return CompositeFuture.all(
                new ApplicationPathScanner().scan(),
                new RequestMappingScanner().scan()
        ).compose(v -> Future.succeededFuture());
    }
}
