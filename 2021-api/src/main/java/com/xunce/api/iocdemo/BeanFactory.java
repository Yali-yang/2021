package com.xunce.api.iocdemo;

import java.util.Map;

/**
 * 对象工厂，我们最核心的一个类，在它初始化的时候，创建了bean注册器，完成了资源的加载。
 *
 * 获取bean的时候，先从单例缓存中取，如果没有取到，就创建并注册一个bean
 */
public class BeanFactory {
    Map<String, BeanDefinition> beanDefinitionMap;

    BeanRegister beanRegister;

    public BeanFactory() {
        beanDefinitionMap = ResourceLoader.getResourceDefinition();
        beanRegister = new BeanRegister();
    }

    public Object getBean(String beanName) {
        Object bean = beanRegister.getSingletonBean(beanName);
        if (bean != null) {
            return bean;
        }

        return createBean(beanName);
    }

    public Object createBean(String beanName){
        try {
            Object instance = beanDefinitionMap.get(beanName).getBeanClass().newInstance();
            beanRegister.registerSingletonBean(beanName, instance);
            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
