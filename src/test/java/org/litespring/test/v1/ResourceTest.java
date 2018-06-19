package org.litespring.test.v1;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.core.io.ClasspathResource;
import org.litespring.core.io.FileSystemResource;
import org.litespring.core.io.Resource;

import java.io.InputStream;

public class ResourceTest {

    @Test
    public void testClasspathResource() throws Exception {
        Resource resource = new ClasspathResource("petstore-v1.xml");
        InputStream in = null;
        try {
            in = resource.getInputStream();
            Assert.assertNotNull(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    @Test
    public void testFileSystemResource() throws Exception {
        Resource resource = new FileSystemResource("/Users/chenzhang/IdeaProjects/litespring/src/test/resources/petstore-v1.xml");
        InputStream in = null;
        try {
            in = resource.getInputStream();
            Assert.assertNotNull(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
