package org.litespring.context.support;

import org.litespring.core.io.ClasspathResource;
import org.litespring.core.io.Resource;

public class ClasspathXMLApplictionContext extends AbstractApplicationContext {


    public ClasspathXMLApplictionContext(String configFile) {
        super(configFile);

    }

    protected Resource getResourceByPath(String path) {
        return new ClasspathResource(path, this.getBeanClassLoader());
    }

}
