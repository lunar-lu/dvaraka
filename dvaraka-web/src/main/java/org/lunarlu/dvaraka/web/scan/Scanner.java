package org.lunarlu.dvaraka.web.scan;

import io.vertx.core.Future;

/**
 * @author jiaoteng
 */
public interface Scanner {

    Future<Void> scan();
}
