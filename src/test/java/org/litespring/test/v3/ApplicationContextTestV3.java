package org.litespring.test.v3;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClasspathXMLApplictionContext;
import org.litespring.service.v3.PetStoreService;

public class ApplicationContextTestV3 {
    @Test
    public void testGetBeanProperty() {
        ApplicationContext ctx = new ClasspathXMLApplictionContext("petstore-v3.xml");
        PetStoreService service = (PetStoreService) ctx.getBean("petStore");

        Assert.assertNotNull(service.getAccountDao());
        Assert.assertNotNull(service.getItemDao());
        Assert.assertEquals(1, service.getVersion());
    }
}
