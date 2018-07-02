package org.litespring.test.v3;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.BeanDefination;
import org.litespring.beans.factory.support.ConstructorResolver;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinationReader;
import org.litespring.core.io.ClasspathResource;
import org.litespring.core.io.Resource;
import org.litespring.service.v3.PetStoreService;

public class ConstructorResolverTest {
    @Test
    public void testConstructorResolver() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinationReader reader = new XmlBeanDefinationReader(factory);
        Resource resource = new ClasspathResource("petstore-v3.xml");
        reader.loadBeanDefination(resource);

        BeanDefination bd = factory.getBeanDefination("petStore");
        ConstructorResolver resolver = new ConstructorResolver(factory);
        PetStoreService petStore = (PetStoreService)resolver.autowireConstructor(bd);

        Assert.assertEquals(1, petStore.getVersion());
        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());

    }
}
