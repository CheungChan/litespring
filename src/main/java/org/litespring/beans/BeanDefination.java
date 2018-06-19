package org.litespring.beans;

public interface BeanDefination {
    String SCOPE_DEFAULT = "";
    String SCOPE_PROTOTYPE = "prototype";
    String SCOPE_SINGLETON = "singleton";

    boolean isSingleton();

    boolean isPrototype();

    String getScope();

    void setScope(String scope);


    String getBeanClassName();
}
