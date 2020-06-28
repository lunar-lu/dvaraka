package org.lunarlu.dvaraka.web.meta;

/**
 * @author jiaoteng
 */
public class ClassType {
    private String path;
    private String[] produces;
    private String[] consumes;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getProduces() {
        return produces;
    }

    public void setProduces(String[] produces) {
        this.produces = produces;
    }

    public String[] getConsumes() {
        return consumes;
    }

    public void setConsumes(String[] consumes) {
        this.consumes = consumes;
    }
}