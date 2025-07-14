package com.miguel.di.example;

import com.miguel.di.Singleton;

@Singleton
public class UserRepositoryImpl implements UserRepository{
    public void doSomething() {
        System.out.println("User repository implementation");
    }
}
