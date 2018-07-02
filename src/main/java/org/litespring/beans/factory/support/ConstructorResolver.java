package org.litespring.beans.factory.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.beans.BeanDefination;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.TypeConverter;
import org.litespring.beans.factory.BeanCreateException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.service.v3.PetStoreService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ConstructorResolver {
    protected final Log logger = LogFactory.getLog(getClass());
    private ConfigurableBeanFactory beanFactory;


    public ConstructorResolver(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object autowireConstructor(BeanDefination bd) {
        Constructor constructorToUse = null;
        Object[] argsToUse = null;
        Class<?> beanClass = null;
        try {
            beanClass = this.beanFactory.getBeanClassLoader().loadClass(bd.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new BeanCreateException(bd.getId() + "Instantiation of bean failed,can't creat bean");
        }
        Constructor<?>[] candidates = beanClass.getConstructors();
        BeanDefinationValueResolver resolver = new BeanDefinationValueResolver(beanFactory);
        ConstructorArgument cargs = bd.getConstructorArgument();
        SimpleTypeConverter converter = new SimpleTypeConverter();
        for (int i = 0; i < candidates.length; i++) {
            Class[] paramTypes = candidates[i].getParameterTypes();
            if (paramTypes.length != cargs.getArgumentCount()) {
                continue;
            }
            argsToUse = new Object[paramTypes.length];
            boolean result = this.valueMatchTypes(paramTypes,
                    cargs.getArgumentValues(),
                    argsToUse,
                    resolver,
                    converter);

            if (result) {
                constructorToUse = candidates[i];
                break;
            }

        }
        if (constructorToUse == null) {
            throw new BeanCreateException(bd.getId() + "can't find right constructor");
        }
        try {
            return constructorToUse.newInstance(argsToUse);
        } catch (Exception e) {
            throw new BeanCreateException(bd.getId() + "can't instance constructor");
        }
    }

    private boolean valueMatchTypes(Class[] paramTypes, List<ConstructorArgument.ValueHolder> valueHolders,
                                    Object[] argsToUse,
                                    BeanDefinationValueResolver resolver, TypeConverter converter) {
        for (int i = 0; i < paramTypes.length; i++) {
            ConstructorArgument.ValueHolder valueHolder = valueHolders.get(i);
            // 可能是TypedStringValue也可能是RuntimeeanReference
            Object originValue = valueHolder.getValue();
            try {
                Object resolvedValue = resolver.resolveValueIfNeccessay(originValue);
                Object convertedvalue = converter.convertIfNecessary(resolvedValue, paramTypes[i]);
                argsToUse[i] = convertedvalue;
            } catch (Exception e) {
                logger.error(e);
                return false;
            }

        }
        return true;

    }
}
