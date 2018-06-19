package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefination;

public interface BeanDefinationRegisty {
    BeanDefination getBeanDefination(String beanID);

    void registerBeanDefination(String beanID, BeanDefination bd);
}
