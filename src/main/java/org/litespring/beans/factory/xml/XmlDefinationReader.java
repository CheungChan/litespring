package org.litespring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefination;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.BeanDefinationRegisty;
import org.litespring.beans.factory.support.GenericBeanDefination;
import org.litespring.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class XmlDefinationReader {
    private static final String ID_ATTRIBUTE = "id";
    private static final String CLASS_ATTRIBUTE = "class";
    private static final String SCOPE_ATTRIBUTE = "scope";

    private BeanDefinationRegisty registy;

    public XmlDefinationReader(BeanDefinationRegisty registy) {
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
}
