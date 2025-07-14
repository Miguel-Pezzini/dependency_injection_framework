package com.miguel.di;

import com.miguel.di.example.UserService;
import com.miguel.di.example.UserRepositoryImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BeanFactoryTest {

    @Test
    void shouldInjectConcreteDependency() {
        UserService userService = BeanFactory.INSTANCE.getInstanceOf(UserService.class);
        assertNotNull(userService);
        assertNotNull(userService.getUserRepository());
    }

    @Test
    void shouldReturnSameInstanceForSingletonClass() {
        UserService s1 = BeanFactory.INSTANCE.getInstanceOf(UserService.class);
        UserService s2 = BeanFactory.INSTANCE.getInstanceOf(UserService.class);
        assertSame(s1, s2);
    }

    @Test
    void shouldInjectImplementationIntoInterfaceField() {
        UserService service = BeanFactory.INSTANCE.getInstanceOf(UserService.class);
        assertTrue(service.getUserRepository() instanceof UserRepositoryImpl);
    }

}
