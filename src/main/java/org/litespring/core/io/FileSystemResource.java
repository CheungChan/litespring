package org.litespring.core.io;

import org.litespring.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileSystemResource implements Resource {
    private final File file;
    private final String path;

    public FileSystemResource(String path) {
        Assert.notNull(path, "path must not be null");
        this.file = new File(path);
        this.path = path;
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    public String getDescription() {
        return "file (" + this.file.getAbsolutePath() + ")";
    }

    public String getPath() {
        return path;
    }
}
