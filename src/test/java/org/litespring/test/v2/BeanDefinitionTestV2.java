package org.litespring.test.v2;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.BeanDefination;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinationReader;
import org.litespring.core.io.ClasspathResource;

import java.util.List;

public class BeanDefinitionTestV2 {

    @Test
    public void TestGetBeanDefination() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinationReader reader = new XmlBeanDefinationReader(factory);
        reader.loadBeanDefination(new ClasspathResource("petstore-v2.xml"));

        BeanDefination db = factory.getBeanDefination("petStore");
        List<PropertyValue> pvs = db.getPropertyValues();
        Assert.assertTrue(pvs.size() == 4);

        {
            PropertyValue pv = this.getPropertyValue("accountDao", pvs);
            Assert.assertNotNull(pv);
            Assert.assertTrue(pv.getValue() instanceof RuntimeBeanReference);
        }

        {
            PropertyValue pv = this.getPropertyValue("itemDao", pvs);
            Assert.assertNotNull(pv);
            Assert.assertTrue(pv.getValue() instanceof RuntimeBeanReference);
        }
    }

    private PropertyValue getPropertyValue(String name, List<PropertyValue> pvs) {
        for (PropertyValue pv : pvs) {
            if (pv.getName().equals(name)) {
                return pv;
            }
        }
        return null;
    }
}
