# commons-either

## Usage

```java
Either<String, String> either = Either.ofLeft("left");

boolean isLeft = either.isLeft(); // true
boolean isRight = either.isRight(); // false

String value = either.left() // "left"
String value = either.right() // throw NoSuchElementException
```

## Maven
### Repository
File: <i>pom.xml</i>
```Xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
### Dependency
File: <i>pom.xml</i>
```Xml
<dependency>
    <groupId>com.github.d-william</groupId>
    <artifactId>commons-either</artifactId>
    <version>1.0.0</version>
</dependency>