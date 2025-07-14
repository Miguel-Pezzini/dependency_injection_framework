package com.miguel.di.example;

import com.miguel.di.Inject;
import com.miguel.di.Singleton;

@Singleton
public class UserService {

    @Inject(UserRepositoryImpl.class)
    private UserRepository userRepository;

    public void doSomething() {
        this.userRepository.doSomething();
    }

    public UserRepository getUserRepository() {
        return this.userRepository;
    }
}
