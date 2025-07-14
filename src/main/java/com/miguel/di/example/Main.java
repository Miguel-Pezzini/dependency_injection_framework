package com.miguel.di.example;

import com.miguel.di.BeanFactory;

public class Main {
    public static void main(String[] args) {
        UserService userService = BeanFactory.INSTANCE.getInstanceOf(UserService.class);
        userService.doSomething();
    }
}