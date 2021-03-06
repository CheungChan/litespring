package org.litespring.beans;

import java.util.List;

public interface BeanDefination {
    String SCOPE_DEFAULT = "";
    String SCOPE_PROTOTYPE = "prototype";
    String SCOPE_SINGLETON = "singleton";

    boolean isSingleton();

    boolean isPrototype();

    String getScope();

    void setScope(String scope);


    String getBeanClassName();

    List<PropertyValue> getPropertyValues();

    ConstructorArgument getConstructorArgument();

    String getId();
    boolean hasConstructorValues();
}
