package org.lunarlu.dvaraka.web.scan;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lunarlu.commons.Matchers;
import org.lunarlu.dvaraka.web.HttpServerLauncher;
import org.lunarlu.dvaraka.web.TestLauncher;
import org.lunarlu.dvaraka.web.meta.ServerMeta;

/**
 * @author jiaoteng
 */
@RunWith(VertxUnitRunner.class)
public class RequestMappingScannerTest {

    @Before
    public void before() {
        HttpServerLauncher.start(TestLauncher.class, Vertx.vertx());
    }


    @Test
    public void testScan() {
        TestSuite suite = TestSuite.create("the_test_suite");
        suite.test("", context -> {
            Async async = context.async();
            new RequestMappingScanner().scan()
                    .onSuccess(v -> {
                        Matchers.match(
                                ServerMeta.instance()
                                        .getMapping()
                                        .containsKey(ApplicationPathScannerTest.User.class)
                                        &&
                                        ServerMeta.instance()
                                                .getMapping()
                                                .get(ApplicationPathScannerTest.User.class)
                                                .getMethodMapping()
                                                .size() == 2
                        )
                                .then(async::complete)
                                .otherwise(() -> context.fail(""));
                    })
                    .onFailure(context::fail);
        });
        suite.run().awaitSuccess();
        System.out.println(Json.encode(ServerMeta.instance().getMapping()));
    }
}