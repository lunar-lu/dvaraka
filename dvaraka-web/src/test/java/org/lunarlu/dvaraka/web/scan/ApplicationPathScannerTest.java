package org.lunarlu.dvaraka.web.scan;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import jakarta.ws.rs.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lunarlu.commons.Matchers;
import org.lunarlu.dvaraka.web.HttpServerLauncher;
import org.lunarlu.dvaraka.web.TestLauncher;
import org.lunarlu.dvaraka.web.annotation.Sync;
import org.lunarlu.dvaraka.web.annotation.WebResource;
import org.lunarlu.dvaraka.web.meta.ServerMeta;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author jiaoteng
 */
@RunWith(VertxUnitRunner.class)
public class ApplicationPathScannerTest {

    @Before
    public void before() {
        HttpServerLauncher.start(TestLauncher.class, Vertx.vertx());
    }

    @Test
    public void testScan() {
        TestSuite suite = TestSuite.create("the_test_suite");
        suite.test("", context -> {
            Async async = context.async();
            new ApplicationPathScanner().scan()
                    .onSuccess(v -> {
                        Matchers.match(
                                TestLauncher.APPLICATION_PATH.equals(ServerMeta.instance().getApplicationPath())
                        )
                                .then(async::complete)
                                .otherwise(() -> context.fail(""));
                    })
                    .onFailure(context::fail);
        });
        suite.run().awaitSuccess();
    }

    @Singleton
    @WebResource("/user")
    @Path("/user")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public static class User {

        private final Parent parent;

        @Inject
        public User(Parent parent) {
            this.parent = parent;
        }

        @GET
        @Path("/async/{id}")
        @Produces("application/json;charset=utf-8")
        @Consumes("application/json;charset=utf-8")
        public Future<User> get(@PathParam("id") String id) {
            parent.doing();
            return Future.succeededFuture();
        }

        @GET
        @Path("/{id}")
        @Sync
        public User getUser(@PathParam("id") String id) {
            return null;
        }

        public static class Parent {
            public void doing() {
                System.out.println("doing");
            }
        }
    }
}