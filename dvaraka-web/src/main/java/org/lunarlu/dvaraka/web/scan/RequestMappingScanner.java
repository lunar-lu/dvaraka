package org.lunarlu.dvaraka.web.scan;

import com.google.common.collect.Maps;
import io.vertx.core.Future;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.lunarlu.commons.Matchers;
import org.lunarlu.dvaraka.web.annotation.Sync;
import org.lunarlu.dvaraka.web.annotation.WebResource;
import org.lunarlu.dvaraka.web.common.DvarakaException;
import org.lunarlu.dvaraka.web.common.ValidationUtils;
import org.lunarlu.dvaraka.web.meta.ClassType;
import org.lunarlu.dvaraka.web.meta.MethodType;
import org.lunarlu.dvaraka.web.meta.RequestMeta;
import org.lunarlu.dvaraka.web.meta.ServerMeta;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jiaoteng
 */
public class RequestMappingScanner extends AbsScanner {


    @Override
    public Future<Void> scan() {

        return Future.future(f -> vertx.executeBlocking(promise -> {

            reflections.getTypesAnnotatedWith(WebResource.class)
                    .parallelStream()
                    .forEach(clazz -> ServerMeta.instance().getMapping().put(clazz, requestMapping(clazz)));

            promise.complete();
        }, false, f));
    }

    private RequestMeta requestMapping(Class<?> clazz) {
        RequestMeta mapping = new RequestMeta();
        mapping.setClassType(classType(clazz));
        mapping.setMethodMapping(methodMapping(clazz, mapping.getClassType()));
        return mapping;
    }

    private ClassType classType(Class<?> clazz) {
        ClassType classType = new ClassType();
        WebResource webResource = clazz.getAnnotation(WebResource.class);
        classType.setPath(StringUtils.defaultIfBlank(webResource.path(), webResource.value()));
        classType.setConsumes(webResource.consumes());
        classType.setProduces(webResource.produces());
        Matchers.of(clazz.getAnnotation(Consumes.class))
                .match(Objects::nonNull)
                .then(consumes -> {
                    classType.setConsumes(consumes.value());
                })
                .compose(clazz.getAnnotation(Produces.class))
                .match(Objects::nonNull)
                .then(produces -> {
                    classType.setProduces(produces.value());
                });
        return classType;
    }

    private Map<String, MethodType> methodMapping(Class<?> clazz, ClassType classType) {
        Map<String, MethodType> map = Maps.newConcurrentMap();
        MethodUtils.getMethodsListWithAnnotation(clazz, Path.class, true, true)
                .parallelStream()
                .forEach(method -> {
                    MethodType methodType = new MethodType();
                    methodType.setConsumes(classType.getConsumes());
                    methodType.setProduces(classType.getProduces());
                    Matchers.of(method.getAnnotation(Path.class))
                            .match(Objects::nonNull)
                            .then(path -> {
                                ValidationUtils.checkHttpPath(path.value());
                                methodType.setPath(path.value());
                            })
                            .compose(method.getAnnotation(Produces.class))
                            .match(Objects::nonNull)
                            .then(produces -> {
                                methodType.setProduces(produces.value());
                            })
                            .compose(method.getAnnotation(Consumes.class))
                            .match(Objects::nonNull)
                            .then(consumes -> {
                                methodType.setConsumes(consumes.value());
                            })
                            .compose(method.getAnnotation(Sync.class))
                            .match(Objects::nonNull)
                            .then(() -> methodType.setSync(true));

                    List<HttpMethod> httpMethods = Stream.of(method.getAnnotations())
                            .filter(v -> Objects.nonNull(v.annotationType().getAnnotation(HttpMethod.class)))
                            .map(v -> v.annotationType().getAnnotation(HttpMethod.class))
                            .collect(Collectors.toList());
                    Matchers.of(httpMethods)
                            .match(httpMethods.size() == 1)
                            .then(() -> methodType.setHttpMethod(io.vertx.core.http.HttpMethod.valueOf(httpMethods.get(0).value())))
                            .match(List::isEmpty).thenThrow(new DvarakaException("method:[" + method.getName() + "] please add HttpMethod, such as @GET"))
                            .match(httpMethods.size() > 1).thenThrow(new DvarakaException("HttpMethod only one was needed, but more than one was found: " + httpMethods));
                    map.put(method.toString(), methodType);
                });
        return map;
    }

}
