# TestUtilities
Utilities for injecting and accessing fields in classes and objects. The fields can be private, static or final.

Injects a string into a target object that has a annotated field:
```
inject("hello", String.class, target, Inject.class);
```

Utilities for creating objects from classes with private constructors.
