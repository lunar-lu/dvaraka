package org.lunarlu.dvaraka.web;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import org.lunarlu.commons.Matchers;
import org.lunarlu.dvaraka.web.common.DvarakaException;
import org.lunarlu.dvaraka.web.di.DependencyManagers;
import org.lunarlu.dvaraka.web.meta.ServerMeta;
import org.lunarlu.dvaraka.web.rs.RuntimeDelegateImpl;

import java.util.Objects;

import static jakarta.ws.rs.ext.RuntimeDelegate.JAXRS_RUNTIME_DELEGATE_PROPERTY;

/**
 * web server
 *
 * @author jiaoteng
 */
public class HttpServerLauncher {
    static {
        System.setProperty(JAXRS_RUNTIME_DELEGATE_PROPERTY, RuntimeDelegateImpl.class.getName());
    }

    public static void start(Class<?> baseClass, Vertx vertx) {
        start(baseClass, vertx, new HttpServerOptions());
    }

    public static void start(Class<?> baseClass, Vertx vertx, HttpServerOptions options) {
        try {
            Matchers.of(baseClass)
                    .match(Objects::isNull).thenThrow(new NullPointerException("baseClass cannot be null"))
                    .composeMatch(Objects.isNull(vertx)).thenThrow(new NullPointerException("vertx cannot be null"))
                    .otherwise(
                            ServerMeta.instance()
                                    .setVertx(vertx)
                                    .setBaseClass(baseClass)
                                    .scan()
                    )
                    .result()
                    .compose(f -> DependencyManagers.scanComponents())
                    .compose(v -> Future.<String>future(f -> vertx.deployVerticle(new HttpServerVerticle(options), f)))
                    .onFailure(e -> {
                        throw new DvarakaException(e);
                    })
                    .toCompletionStage()
                    .toCompletableFuture()
                    .get();
        } catch (Exception e) {
            throw new DvarakaException(e);
        }

    }
}
