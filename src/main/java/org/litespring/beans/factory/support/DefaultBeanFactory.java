package org.litespring.beans.factory.support;

import org.apache.commons.beanutils.BeanUtils;
import org.litespring.beans.BeanDefination;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.TypeConverter;
import org.litespring.beans.factory.BeanCreateException;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory extends DefaultSingletonBeanRegistry
        implements ConfigurableBeanFactory, BeanDefinationRegisty {

    private final Map<String, BeanDefination> beanDefinationMap = new ConcurrentHashMap<String, BeanDefination>();
    private ClassLoader beanClassLoader;

    public BeanDefination getBeanDefination(String beanId) {
        return this.beanDefinationMap.get(beanId);
    }

    public void registerBeanDefination(String beanID, BeanDefination bd) {
        this.beanDefinationMap.put(beanID, bd);
    }

    public Object getBean(String beanId) {
        BeanDefination bd = getBeanDefination(beanId);
        if (bd == null) {
            throw new BeanCreateException("Bean Definition does not exist.");
        }
        if (bd.isSingleton()) {
            Object bean = this.getSingleton(beanId);
            if (bean == null) {
                bean = createBean(bd);
                this.registrySingleton(beanId, bean);
            }
            return bean;
        }
        return createBean(bd);

    }

    private Object createBean(BeanDefination bd) {
        // 创建实例
        Object bean = instantiateBean(bd);
        // 设置属性
//        populateBean(bd, bean);
        populateBeanUseBeanUtils(bd, bean);
        return bean;
    }

    private Object instantiateBean(BeanDefination bd) {
        ClassLoader cl = this.getBeanClassLoader();
        String beanClassName = bd.getBeanClassName();
        try {
            Class<?> clz = cl.loadClass(beanClassName);
            return clz.newInstance();
        } catch (Exception e) {
            throw new BeanCreateException("create bean for " + beanClassName + "failed");
        }
    }

    private void populateBean(BeanDefination bd, Object bean) {
        List<PropertyValue> pvs = bd.getPropertyValues();
        if (pvs == null || pvs.isEmpty()) {
            return;
        }
        BeanDefinationValueResolver resolver = new BeanDefinationValueResolver(this);
        TypeConverter converter = new SimpleTypeConverter();
        try {
            for (PropertyValue pv : pvs) {
                String propertyName = pv.getName();
                Object originalValue = pv.getValue();
                Object resolvedValue = resolver.resolveValueIfNeccessay(originalValue, pv);
                // 接下来调用bean的set方法设置属性 可以使用jdk提供的Introspector获取BeanInfo
                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {
                    if (pd.getName().equals(propertyName)) {
                        Object convertedValue = converter.convertIfNecessary(resolvedValue, pd.getPropertyType());
                        pd.getWriteMethod().invoke(bean, convertedValue);
                        break;
                    }
                }

            }
        } catch (Exception ex) {
            throw new BeanCreateException("Failed to obtain BeanInfo for class [" + bean.getClass() + "]");
        }

    }

    private void populateBeanUseBeanUtils(BeanDefination bd, Object bean) {
        List<PropertyValue> pvs = bd.getPropertyValues();
        if (pvs == null || pvs.isEmpty()) {
            return;
        }
        BeanDefinationValueResolver resolver = new BeanDefinationValueResolver(this);
        try {
            for (PropertyValue pv : pvs) {
                String propertyName = pv.getName();
                Object originalValue = pv.getValue();
                Object resolvedValue = resolver.resolveValueIfNeccessay(originalValue, pv);
                // 使用commons.beanutils里面的setPropertyValue来设置bean的属性, 不再需要自己写类型转换
                BeanUtils.setProperty(bean, propertyName, resolvedValue);
            }
        } catch (Exception ex) {
            throw new BeanCreateException("Failed to obtain BeanInfo for class [" + bean.getClass() + "]");
        }
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader == null ? ClassUtils.getDefaultClassLoader() : this.beanClassLoader;
    }
}
