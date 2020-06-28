package org.lunarlu.dvaraka.web.meta;

import java.util.Map;

/**
 * @author jiaoteng
 */
public class RequestMeta {

    private ClassType classType;
    private Map<String, MethodType> methodMapping;


    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public Map<String, MethodType> getMethodMapping() {
        return methodMapping;
    }

    public void setMethodMapping(Map<String, MethodType> methodMapping) {
        this.methodMapping = methodMapping;
    }

}
