package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefination;
import org.litespring.beans.factory.BeanCreateException;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.util.ClassUtils;

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
        ClassLoader cl = this.getBeanClassLoader();
        String beanClassName = bd.getBeanClassName();
        try {
            Class<?> clz = cl.loadClass(beanClassName);
            return clz.newInstance();
        } catch (Exception e) {
            throw new BeanCreateException("create bean for " + beanClassName + "failed");
        }
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader == null ? ClassUtils.getDefaultClassLoader() : this.beanClassLoader;
    }
}
