package org.litespring.beans.factory.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefination;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.BeanDefinationRegisty;
import org.litespring.beans.factory.support.GenericBeanDefination;
import org.litespring.core.io.Resource;
import org.litespring.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Logger;

public class XmlBeanDefinationReader {
    private static final String ID_ATTRIBUTE = "id";
    private static final String CLASS_ATTRIBUTE = "class";
    private static final String SCOPE_ATTRIBUTE = "scope";
    private static final String PROPERTY_ELEMENT = "property";
    private static final String REF_ATTRIBUTE = "ref";
    private static final String VALUE_ATTRIBUTE = "value";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String CONSTRUCTOR_ARGS_ELEMENT = "constructor-args";
    private static final String TYPE_ATTRIBUTE_ATTRIBUTE = "type";
    private BeanDefinationRegisty registy;
    private final Log logger = LogFactory.getLog(getClass());

    public XmlBeanDefinationReader(BeanDefinationRegisty registy) {
        this.registy = registy;
    }

    public void loadBeanDefination(Resource resource) {
        InputStream is = null;
        try {
            is = resource.getInputStream();
            SAXReader reader = new SAXReader();
            Document doc = reader.read(is);

            Element root = doc.getRootElement(); // <beans>
            Iterator iter = root.elementIterator();
            while (iter.hasNext()) {
                Element ele = (Element) iter.next();
                String id = ele.attributeValue(ID_ATTRIBUTE);
                String beanClassName = ele.attributeValue(CLASS_ATTRIBUTE);
                BeanDefination bd = new GenericBeanDefination(id, beanClassName);
                String scope = ele.attributeValue(SCOPE_ATTRIBUTE);
                if (scope != null) {
                    bd.setScope(scope);
                }
                this.parseConstructorArguments(ele, bd);
                this.parsePropertyElement(ele, bd);
                this.registy.registerBeanDefination(id, bd);
            }
        } catch (DocumentException e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document", e);
        } catch (IOException e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void parsePropertyElement(Element beanEle, BeanDefination bd) {
        Iterator iter = beanEle.elementIterator(PROPERTY_ELEMENT);
        while (iter.hasNext()) {
            Element propEle = (Element) iter.next();
            String propertyName = propEle.attributeValue(NAME_ATTRIBUTE);
            if (!StringUtils.hasLength(propertyName)) {
                logger.fatal("Tag 'property' must have a 'name' attribute");
                return;
            }

            Object val = parsePropertyValue(propEle, bd, propertyName);
            PropertyValue pv = new PropertyValue(propertyName, val);
            bd.getPropertyValues().add(pv);
        }
    }

    public Object parsePropertyValue(Element ele, BeanDefination bd, String propertyName) {
        String elementName = (propertyName != null) ? "<property> element for property '" + propertyName +
                "'" : "<constructor-arg> element";
        boolean hasRefAttribute = (ele.attributeValue(REF_ATTRIBUTE) != null);
        boolean hasValueAttribute = (ele.attributeValue(VALUE_ATTRIBUTE) != null);
        if (hasRefAttribute) {
            String refName = ele.attributeValue(REF_ATTRIBUTE);
            if (!StringUtils.hasText(refName)) {
                logger.fatal(elementName + " contains empty 'ref' attribute");
            }
            RuntimeBeanReference ref = new RuntimeBeanReference(refName);
            return ref;
        } else if (hasValueAttribute) {
            TypedStringValue value = new TypedStringValue(ele.attributeValue(VALUE_ATTRIBUTE));
            return value;
        } else {
            throw new RuntimeException(elementName + "must specify a ref or a value");
        }
    }

    public void parseConstructorArguments(Element beanEle, BeanDefination bd) {
        Iterator iter = beanEle.elementIterator(CONSTRUCTOR_ARGS_ELEMENT);
        while (iter.hasNext()) {
            Element ele = (Element) iter.next();
            parseConstructorArgument(ele, bd);
        }
    }

    public void parseConstructorArgument(Element beanEle, BeanDefination bd) {
        String typeAttr = beanEle.attributeValue(TYPE_ATTRIBUTE_ATTRIBUTE);
        String nameAttr = beanEle.attributeValue(NAME_ATTRIBUTE);
        Object value = parsePropertyValue(beanEle, bd, null);
        ConstructorArgument.ValueHolder valueHolder = new ConstructorArgument.ValueHolder(value);
        if (StringUtils.hasLength(typeAttr)) {
            valueHolder.setType(typeAttr);
        }
        if (StringUtils.hasLength(nameAttr)) {
            valueHolder.setName(nameAttr);
        }
        bd.getConstructorArgument().addArgumentValue(valueHolder);
    }
}
