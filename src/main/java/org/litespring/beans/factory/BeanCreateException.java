package org.litespring.beans.factory;

import org.litespring.beans.BeansException;

public class BeanCreateException extends BeansException {
    private String beanName;

    public BeanCreateException(String msg) {
        super(msg);
    }

    public BeanCreateException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BeanCreateException(String beanName, String msg) {
        super("Error create bean with name " + beanName + ":" + msg);
        this.beanName = beanName;
    }

    public BeanCreateException(String beanName, String msg, Throwable cause) {
        this(beanName, msg);
        initCause(cause);
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
