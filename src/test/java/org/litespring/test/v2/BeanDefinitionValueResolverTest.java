package org.litespring.test.v2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.BeanDefination;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.BeanDefinationValueResolver;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinationReader;
import org.litespring.core.io.ClasspathResource;
import org.litespring.dao.v2.AccountDAO;

public class BeanDefinitionValueResolverTest {
    private BeanDefinationValueResolver resolver;

    @Before
    public void setUp() {
        DefaultBeanFactory factory;
        factory = new DefaultBeanFactory();
        XmlBeanDefinationReader reader = new XmlBeanDefinationReader(factory);
        reader.loadBeanDefination(new ClasspathResource("petstore-v2.xml"));
        resolver = new BeanDefinationValueResolver(factory);
    }

    @Test
    public void testResolveRuntimeBeanReference() {


        RuntimeBeanReference reference = new RuntimeBeanReference("accountDao");
        Object value = resolver.resolveValueIfNeccessay(reference);
        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof AccountDAO);
    }
    @Test
    public void testResolveTypedStringValue(){
        TypedStringValue stringValue = new TypedStringValue("test");
        Object value = resolver.resolveValueIfNeccessay(stringValue);
        Assert.assertNotNull(value);
        Assert.assertEquals(value,"test");
    }
}
