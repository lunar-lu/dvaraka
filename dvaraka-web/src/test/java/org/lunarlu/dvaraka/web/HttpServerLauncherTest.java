package org.lunarlu.dvaraka.web;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.vertx.core.Vertx;
import org.junit.Test;
import org.lunarlu.dvaraka.web.annotation.GuiceModule;
import org.lunarlu.dvaraka.web.common.DvarakaException;
import org.lunarlu.dvaraka.web.di.DependencyManagers;

import javax.inject.Inject;

/**
 * @author jiaoteng
 */
public class HttpServerLauncherTest {


    @Test
    public void testThrow() {
        try {
            HttpServerLauncher.start(null, Vertx.vertx());
            assert false;
        } catch (DvarakaException e) {
            assert true;
        }

        try {
            HttpServerLauncher.start(HttpServerLauncherTest.class, null);
            assert false;
        } catch (DvarakaException e) {
            assert true;
        }
    }

    @Test
    public void testGuice() {
        HttpServerLauncher.start(TestLauncher.class, Vertx.vertx());
        assert "".equals(
                DependencyManagers.instance()
                        .getInstance(GuiceTester.class)
                        .test()
        );
    }

    public static class GuiceTester {

        private GuiceTestInjector guiceTestInjector;


        @Inject
        public GuiceTester(GuiceTestInjector guiceTestInjector) {
            this.guiceTestInjector = guiceTestInjector;
        }

        public String test() {
            return guiceTestInjector.test();
        }
    }

    public static class GuiceTestInjector {
        public String test() {
            return "";
        }
    }

    @GuiceModule
    public static class guiceTestModule extends AbstractModule {

        @Provides
        public GuiceTestInjector guiceTestInjector() {
            return new GuiceTestInjector();
        }
    }
}