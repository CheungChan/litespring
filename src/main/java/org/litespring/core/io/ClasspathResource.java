package org.litespring.core.io;

import org.litespring.util.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClasspathResource implements Resource {
    private final String path;
    private final ClassLoader loader;

    public ClasspathResource(String path) {
        this(path, (ClassLoader) null);
    }

    public ClasspathResource(String path, ClassLoader loader) {
        this.path = path;
        this.loader = (loader == null ? ClassUtils.getDefaultClassLoader() : loader);
    }

    public InputStream getInputStream() throws IOException {
        InputStream in = this.loader.getResourceAsStream(this.path);
        if(in==null){
            throw new FileNotFoundException(path + "cann't be opened");
        }
        return in;
    }

    public String getDescription() {
        return this.path;
    }
}
