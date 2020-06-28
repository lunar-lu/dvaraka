package org.lunarlu.dvaraka.web;


import io.vertx.core.Vertx;
import jakarta.ws.rs.ApplicationPath;

/**
 * @author jiaoteng
 */
@ApplicationPath(TestLauncher.APPLICATION_PATH)
public class TestLauncher {
    public static final String APPLICATION_PATH = "/test";

    public static void main(String[] args) {
        HttpServerLauncher.start(TestLauncher.class, Vertx.vertx());
        System.out.println("web server started");
    }
}
