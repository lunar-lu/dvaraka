package org.lunarlu.dvaraka.web.di;

import io.vertx.core.Future;
import org.apache.commons.lang3.StringUtils;
import org.lunarlu.commons.Matchers;
import org.lunarlu.dvaraka.web.common.DvarakaException;

/**
 * @author jiaoteng
 */
public class DependencyManagers {
    public static final String DEPENDENCY_MANAGER = "org.lunarlu.dvaraka.web.dependency_manager";

    private DependencyManagers() {
    }

    private static final DependencyManager INSTANCE = loadManager();

    private static DependencyManager loadManager() {
        String managerClassName = Matchers.of(System.getProperty(DEPENDENCY_MANAGER))
                .match(StringUtils::isNotBlank)
                .then(str -> str)
                .defaultThen(GuiceDependencyManager.class.getName())
                .result();
        try {
            return (DependencyManager) Class.forName(managerClassName).newInstance();
        } catch (Exception e) {
            throw new DvarakaException(e);
        }
    }

    public static DependencyManager instance() {
        return INSTANCE;
    }

    public static Future<Void> scanComponents() {
        INSTANCE.scan();
        return Future.succeededFuture();
    }
}
