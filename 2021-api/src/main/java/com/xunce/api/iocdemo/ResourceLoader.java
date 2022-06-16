package com.xunce.api.iocdemo;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 资源加载器，用来完成配置文件中配置的加载
 */
public class ResourceLoader {

    public static Map<String, BeanDefinition> getResourceDefinition(){
        HashMap<String, BeanDefinition> beanDefinitionMap = new HashMap<>(16);
        Properties properties = new Properties();
        try {
            InputStream inputStream = ResourceLoader.class.getResourceAsStream("/bean.properties");
            properties.load(inputStream);

            Iterator<String> iterator = properties.stringPropertyNames().iterator();
            while(iterator.hasNext()){
                String beanName = iterator.next();
                String className = properties.getProperty(beanName);
                BeanDefinition beanDefinition = new BeanDefinition();
                Class clazz = Class.forName(className);
                beanDefinition.setBeanName(beanName);
                beanDefinition.setBeanClass(clazz);
                beanDefinitionMap.put(beanName, beanDefinition);
            }
            inputStream.close();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return beanDefinitionMap;
    }

}
