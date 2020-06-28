package org.lunarlu.dvaraka.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

import javax.inject.Inject;

/**
 * start http server
 *
 * @author jiaoteng
 */
class HttpServerVerticle extends AbstractVerticle {

    private final HttpServerOptions options;

    @Inject
    public HttpServerVerticle(HttpServerOptions options) {
        this.options = options;
    }


    @Override
    public void start(Promise<Void> startPromise) {
        Router router = Router.router(vertx);
        Future.<HttpServer>future(
                f -> vertx.createHttpServer(options)
                        .requestHandler(router)
                        .listen(f)
        )
                .onSuccess(server -> startPromise.complete())
                .onFailure(startPromise::fail);

    }
}
