# Test Utilities

## Description
Utilities for injecting and accessing fields in classes and objects. The fields can be private, static or final. One
thing to be aware of regarding final fields is that they can be inlined during compilation. This means that
even though it is possible to change the field it won't have any effect since the previous value has already been
inlined in every place it has been referenced.

```java
public class InjectTarget extends SuperInjectTarget {
    private final String finalField = "Final!"; // This field will be inlined during compilation!
}
```

## Requirements

- Java 21 or higher
- Compatible with Gradle 9.x and Maven

## Maven and Gradle usage

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
  <version>1.6</version>
</dependency>
```

### Gradle
```groovy
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    testImplementation 'com.github.paddan:TestUtilities:1.6'
}
```

## Usage of Injector

### Automatic injection of mocks

Automatically inject objects in all fields that's created by the supplied function. It can be a mock function like Mockito's mock() or any other function that takes a class as argument and returns an object.

Automatically inject mocks into a target object using Mockito:
```java
Map<String, Object> mocks = Injector.autoInject(Mockito::mock, target);
```

Automatically inject mocks into a target object using Spock:
```groovy
def mocks = Injector.autoInject({Mock(it)}, target)
```

### Specific field injection

Inject a string into a target object that has an annotated field:
```java
Injector.inject("hello", target, Inject.class);
```

Or using the builder to inject a string into a target object that has an annotated field:
```java
Injector.inject("hello").into(target).with(Inject.class);
```

Inject a string into a target object using the name of the field:
```java
Injector.inject("hello", target, "fieldName");
```

Or using the builder to inject a string into a target object using the name of the field:
```java
Injector.inject("hello").into(target).with("fieldName");
```

## Usage of Accessor

Read a private field by its name:
```java
Accessor.get("privateField", privateClass);
```

Or using the builder to read a private field by its name:
```java
Accessor.get("privateField").from(privateClass);
```

Read a private field by its annotation and type:
```java
Accessor.get(MyFirstAnnotation.class, String.class, privateClass);
```

Or using the builder to read a private field by its annotation and type:
```java
Accessor.get(MyFirstAnnotation.class).ofType(String.class).from(privateClass);
```

## Usage of Caller

Call private constructor without args:
```java
Caller.construct(PrivateClass.class);
```

Call private constructor with args:
```java
Caller.construct(PrivateClass.class, "hello", 10L);
```

Call private method without args:
```java
Caller.callMethod(privateClass, "methodName");
```

Call private method with args:
```java
Caller.callMethod(privateClass, "methodName", "first arg", 10, 5L);
```

Call private static method:
```java
Caller.callStatic(PrivateClass.class, "staticMethodName", "arg1", "arg2");
```
***
# Disclaimer
Software developed by Patrik Lindefors (PL) is provided 'as is' without warranty of any kind, either expressed or
implied, including, but not limited to, the implied warranties of fitness for a purpose, or the warranty of
non-infringement. 

Without limiting the foregoing, PL makes no warranty that:
1.	the software will meet your requirements
2.	the software will be uninterrupted, timely, secure or error-free
3.	the results that may be obtained from the use of the software will be effective, accurate or reliable
4.	the quality of the software will meet your expectations
5.	any errors in the software will be corrected.

Software and its documentation made by PL:
6.	could include technical or other mistakes, inaccuracies or typographical errors.
7.	may be out of date, and PL makes no commitment to update such materials.

PL assumes no responsibility for errors or omissions in the software or documentation.
In no event shall PL be liable to you or any third parties for any special, punitive, incidental, indirect or
consequential damages of any kind, or any damages whatsoever, including, without limitation, those resulting from
loss of use, data or profits, whether or not PL has been advised of the possibility of such damages, and on any theory
of liability, arising out of or in connection with the use of this software.

The use of the software developed by PL is done at your own discretion and risk and with agreement that you will be
solely responsible for any damage to your computer system or loss of data that results from such activities. No advice
or information, whether oral or written, obtained by you from PL or any associates of PL shall create any warranty for
the software.
***
