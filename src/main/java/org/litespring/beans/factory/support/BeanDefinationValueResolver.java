package org.litespring.beans.factory.support;

import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;

public class BeanDefinationValueResolver {
    private DefaultBeanFactory beanFactory;

    public BeanDefinationValueResolver(DefaultBeanFactory factory) {
        this.beanFactory = factory;
    }

    public Object resolveValueIfNeccessay(Object value, PropertyValue pv) {

        if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference ref = (RuntimeBeanReference) value;
            String refName = ref.getBeanName();
            Object bean = beanFactory.getBean(refName);
            pv.setConvertedValue(bean);
            return bean;
        } else if (value instanceof TypedStringValue) {
            TypedStringValue val = (TypedStringValue) value;
            Object v = val.getValue();
            pv.setConvertedValue(v);
            return v;
        } else {
            // TODO
            throw new RuntimeException("the value" + value + "has not implemented");
        }
    }

}
