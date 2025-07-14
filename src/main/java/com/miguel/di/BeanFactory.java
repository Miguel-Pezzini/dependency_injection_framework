package com.miguel.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum BeanFactory {
    INSTANCE;

    private final ConcurrentMap<Class<?>, Object> registry = new ConcurrentHashMap<>();

    public <T> T getInstanceOf(Class<T> beanClass, Object... arguments) {
        try {
            if (beanClass.isAnnotationPresent(Singleton.class)) {
                if (registry.containsKey(beanClass)) {
                    return (T) registry.get(beanClass);
                }
                T bean = instantiateBeanClass(beanClass, arguments);
                registry.putIfAbsent(beanClass, bean);
                return (T) registry.get(beanClass);
            } else {
                T bean = instantiateBeanClass(beanClass, arguments);
                return bean;
            }
        } catch (NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T instantiateBeanClass(Class<T> beanClass, Object[] arguments)
            throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {

        Class<?>[] argumentsClasses =
                Arrays.stream(arguments).map(Object::getClass).toArray(Class<?>[]::new);
        Constructor<T> beanConstructor = beanClass.getConstructor(argumentsClasses);

        T bean = beanConstructor.newInstance(arguments);

        Field[] fields = beanClass.getDeclaredFields();

        Field[] injectableFields =
                Arrays.stream(fields)
                        .filter(field -> field.isAnnotationPresent(Inject.class))
                        .toArray(Field[]::new);

        for (Field injectableField : injectableFields) {
            Inject injectAnnotation = injectableField.getAnnotation(Inject.class);
            Class<?> fieldClass = injectableField.getType();

            Class<?> implementationClass = injectAnnotation.value() != Void.class
                    ? injectAnnotation.value()
                    : fieldClass;

            Object fieldValue = BeanFactory.INSTANCE.getInstanceOf(implementationClass);

            injectableField.setAccessible(true);
            injectableField.set(bean, fieldValue);
        }
        return bean;
    }
}
