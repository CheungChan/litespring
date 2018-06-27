package org.litespring.test.v1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.BeanDefination;
import org.litespring.beans.factory.BeanCreateException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinationReader;
import org.litespring.core.io.ClasspathResource;
import org.litespring.core.io.Resource;
import org.litespring.service.v1.PetStoreService;

import static org.junit.Assert.*;

public class BeanFactoryTest {
    private XmlBeanDefinationReader reader;
    private DefaultBeanFactory factory;

    @Before
    public void setUp() {
        factory = new DefaultBeanFactory();
        reader = new XmlBeanDefinationReader(factory);
    }

    @Test
    public void testGetBean() {
        Resource resource = new ClasspathResource("petstore-v1.xml");
        reader.loadBeanDefination(resource);
        BeanDefination bd = factory.getBeanDefination("petStore");

        assertTrue(bd.isSingleton());
        assertFalse(bd.isPrototype());
        assertEquals(BeanDefination.SCOPE_DEFAULT, bd.getScope());

        assertEquals("org.litespring.service.v1.PetStoreService", bd.getBeanClassName());

        PetStoreService petStore = (PetStoreService) factory.getBean("petStore");

        assertNotNull(petStore);

        PetStoreService petStore1 = (PetStoreService) factory.getBean("petStore");
        assertTrue(petStore.equals(petStore1));
    }

    @Test
    public void testInvalidBean() {
        Resource resource = new ClasspathResource("petstore-v1.xml");
        reader.loadBeanDefination(resource);
        try {
            factory.getBean("invalidBean");
        } catch (BeanCreateException e) {
            return;
        }
        Assert.fail("expect BeanCreateException");
    }

    @Test
    public void testInvalidXML() {
        try {
            Resource resource = new ClasspathResource("xxx.xml");
            reader.loadBeanDefination(resource);
        } catch (BeanDefinitionStoreException e) {
            return;
        }
        Assert.fail("expect BeanDefinitionStoreException");
    }
}
