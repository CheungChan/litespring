package org.litespring.beans.factory.support;

import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;

public class BeanDefinationValueResolver {
    private BeanFactory beanFactory;

    public BeanDefinationValueResolver(BeanFactory factory) {
        this.beanFactory = factory;
    }

    public Object resolveValueIfNeccessay(Object value) {

        if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference ref = (RuntimeBeanReference) value;
            String refName = ref.getBeanName();
            return beanFactory.getBean(refName);
        } else if (value instanceof TypedStringValue) {
            TypedStringValue val = (TypedStringValue) value;
            return val.getValue();
        } else {
            // TODO
            throw new RuntimeException("the value" + value + "has not implemented");
        }
    }

    public Object resolveValueIfNeccessay(Object value, PropertyValue pv) {

        Object beanOrValue = resolveValueIfNeccessay(value);
        pv.setConvertedValue(beanOrValue);
        return beanOrValue;
    }

}
