# Test Utilities

## Description
Utilities for injecting and accessing fields in classes and objects. The fields can be private, static or final. One
thing to be aware of regarding final fields is that they can be inlined during compilation. This means that
even though it is possible to change the field it won't have any effect since the previous value has already been
inlined in every place it has been referenced.

```
public class InjectTarget extends SuperInjectTarget {
    private final String finalField = "Final!"; // This field will be inlined during compilation!
}
```

## Maven and gradle usage

### Maven
```
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.paddan</groupId>
  <artifactId>TestUtilities</artifactId>
  <version>1.1</version>
</dependency>
```

### Gradle
```
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    testCompile 'com.github.paddan:TestUtilities:1.1'
}
```

## Usage of Injector

Inject a string into a target object that has a annotated field:
```
Injector.inject("hello", target, Inject.class);
```

Or using the builder to inject a string into a target object that has a annotated field:
```
Injector.inject("hello").into(target).with(Inject.class);
```

Inject a string into a target object using the name of the field:
```
Injector.inject("hello", target, "fieldName");
```

Or using the builder to inject a string into a target object using the name of the field:
```
Injector.inject("hello").into(target).with("fieldName");
```

## Usage of Accessor

Read a private field by its name
```
Accessor.get("privateField", privateClass);
```

Or using the builder to read a private field by its name
```
Accessor.get("privateField").from(privateClass);
```

Read a private field by its annotation and the type
```
Accessor.get(MyFirstAnnotation, String.class, privateClass);
```

Or using the builder to read a private field using by its name
```
Accessor.get(MyFirstAnnotation).ofType(String).from(privateClass);
```

## Usage of Caller
Call private constructor without args
```
Caller.construct(PrivateClass.class);
```

Call private constructor with args
```
Caller.construct(PrivateClass.class, "hello", 10L);
```

Call private method without args
```
Caller.callMethod(privateClass, "methodName");
```

Call private method with args
```
Caller.callMethod(privateClass, "methodName", "first arg", 10, 5L);
```