# Mini DI Framework

A lightweight **Dependency Injection** framework in Java, built for educational purposes. It uses the **Reflection API** to instantiate objects, inject dependencies automatically, and support the **Singleton** pattern.

---

## ✨ Features

- Field-based dependency injection using `@Inject`
- Singleton support with `@Singleton`
- Optional implementation binding for interfaces via `@Inject(SomeImpl.class)`
- Automatic constructor-based instantiation and field injection
- Centralized bean management via a singleton `BeanFactory`

---

## 🚀 Example Usage
📄 File: UserRepository.java
```java
public interface UserRepository {
    void doSomething();
}
```
📄 File: UserRepositoryImpl.java
```java
@Singleton
public class UserRepositoryImpl implements UserRepository {
    public void doSomething() {
        System.out.println("User repository implementation");
    }
}
```
📄 File: UserService.java
```java
@Singleton
public class UserService {

    @Inject(UserRepositoryImpl.class)
    private UserRepository userRepository;

    public void doSomething() {
        userRepository.doSomething();
    }
}
```
📄 File: Main.java
```java

public class Main {
    public static void main(String[] args) {
        UserService userService = BeanFactory.INSTANCE.getInstanceOf(UserService.class);
        userService.doSomething(); // Expects the text "User repository implementation"
    }
}
```

---

## 📄 Explanation

The ```BeanFactory``` class provides the ```getInstanceOf``` method, which receives the class we want to instantiate and, optionally, the arguments for its constructor (in the example, it's ```null```, so no arguments are needed).

First, the method checks if the given class (e.g., ```UserService```) is annotated with ```@Singleton```. If it is, the factory checks whether an instance already exists in the internal ```ConcurrentHashMap```. If it does, that instance is returned. Otherwise, a new instance is created by calling the ```instantiateBeanClass``` method.

The ```instantiateBeanClass``` method is responsible for the actual instantiation process. It starts by converting the constructor arguments into a ```Class<?>[]``` array to retrieve the correct constructor using ```getConstructor(...)```.

After locating the constructor, it calls ```newInstance(arguments)``` to create the bean. Then, the framework proceeds with dependency injection.

It scans all the declared fields in the class and filters those annotated with ```@Inject```. For each of these fields, it checks whether the annotation specifies a concrete implementation class (useful when injecting into an interface) or uses the default type (the field's own class).

It then recursively calls ```getInstanceOf``` to create the dependency, making sure it respects the ```@Singleton``` annotation if present.

Finally, it makes the field accessible (in case it's ```private```) and sets the resolved instance into the field using reflection.

---

## ✅ Conclusion

This implementation demonstrates the power of Java's Reflection API, which allows us to dynamically inspect and manipulate classes at runtime. Through reflection, we are able to:

- Access declared fields using ```getDeclaredFields()```

- Read annotations applied to classes and fields with ```isAnnotationPresent(...)``` and ```getAnnotation(...)```

- Retrieve constructors using ```getConstructor(...)``` and instantiate objects via ```newInstance(...)```

- Set private fields accessible with ```setAccessible(true)```

- Inject dependencies dynamically, even if the fields are ```private``` or declared as interfaces

This level of introspection and control makes it possible to build lightweight dependency injection frameworks like this one, without relying on external libraries. Reflection enables a flexible and powerful foundation for dynamic object creation, configuration, and wiring — essential concepts behind larger frameworks like Spring.

---

## 👨‍💻 Author

This project was developed by **Miguel Pezzini Kühr** as a learning exercise to explore and apply Java's Reflection API and build a simple dependency injection framework from scratch.

Parts of the `BeanFactory` implementation were inspired by examples from the official Java blog:  
🔗 [dev.java](https://dev.java)

Feel free to explore, fork, or contribute to this project!
