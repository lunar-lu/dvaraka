package org.lunarlu.dvaraka.web.scan;

import io.vertx.core.Future;
import jakarta.ws.rs.ApplicationPath;
import org.lunarlu.commons.Matchers;
import org.lunarlu.dvaraka.web.meta.ServerMeta;

import java.util.Set;

/**
 * @author jiaoteng
 */
public class ApplicationPathScanner extends AbsScanner {

    public Future<Void> scan() {
        return Future.future(f -> vertx.executeBlocking(
                promise -> {
                    Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ApplicationPath.class);
                    Matchers.of(classes)
                            .match(classes.size() > 1)
                            .then(() -> promise.fail("ApplicationPath only one was needed, but more than one was found: " + classes))
                            .match(Set::isEmpty).non()
                            .defaultThen(() ->
                                    classes.stream()
                                            .map(v -> v.getAnnotation(ApplicationPath.class))
                                            .findFirst()
                                            .ifPresent(path -> ServerMeta.instance().setApplicationPath(path.value()))
                            );
                    promise.complete();
                }, false, f)
        );
    }

}
