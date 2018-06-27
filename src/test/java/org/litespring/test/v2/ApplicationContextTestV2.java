package org.litespring.test.v2;

import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClasspathXMLApplictionContext;
import org.litespring.dao.v2.AccountDAO;
import org.litespring.dao.v2.ItemDAO;
import org.litespring.service.v2.PetStoreService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ApplicationContextTestV2 {

    @Test
    public void testGetBeanProperty() {
        ApplicationContext ctx = new ClasspathXMLApplictionContext("petstore-v2.xml");
        PetStoreService service = (PetStoreService) ctx.getBean("petStore");

        assertNotNull(service.getAccountDao());
        assertNotNull(service.getItemDao());
        assertTrue(service.getAccountDao() instanceof AccountDAO);
        assertTrue(service.getItemDao() instanceof ItemDAO);
        assertEquals(service.getOwner(), "liuxin");
        assertEquals(service.getVersion(), 2);
    }
}
