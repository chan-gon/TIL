# JUnit5

JUnit5는 JUnit Platform, JUnit Jupiter, JUnit Vintage가 함쳐진 형태를 가지고 있다.


## 사용 환경
---
Java8 이상의 환경을 요구하지만, 이전 JDK 버전으로 작성된 코드 또한 테스트 가능하다.

## 사용 어노테이션
---
| 어노테이션 | 설명 |
| --- | --- |
| @Test | 해당 메소드가 테스트 메소드임을 명시 |
| @ParameterizedTest | 복수의 인자를 가지는 parameterized test임을 명시 |
| @RepeatedTest | 특정 횟수만큼 반복되는 테스트를 수행하는 repeated test임을 명시 |
| @TestFactory | 런타임에 발생한 변경 사항을 반영할 수 있는 동적인 dynamic tests임을 명시 |
| @TestTemplate | 여러번 호출될 수 있는 테스트 케이스 템플릿임을 명시 |
| @TestClassOrder | 테스트 클래스 실행 순서 설정 |
| @TestMethodOrder | 테스트 메소드 실행 순서 설정 |
| @TestInstance | 테스트 인스턴스 생명주기 설정 |
| @DisplayName | 테스트 클래스 또는 메소드 테스트 이름 선언 |
| @BeforeEach | 해당 어노테이션이 붙은 메소드는 각 테스트가 실행되기 이전에 수행되어야 한다. JUnit4에서는 @Before로 사용되었다. |
| @AfterEach | 해당 어노테이션이 붙은 메소드는 각 테스트가 실행된 이후에 수행되어야 한다. JUnit4에서는 @After로 사용되었다. |
| @BeforeAll | 해당 어노테이션이 붙은 메소드는 모든 테스트가 실행되기 이전에 수행되어야 한다. JUnit4에서는 @BeforeClass로 사용되었다. |
| @AfterAll | 해당 어노테이션이 붙은 메소드는 모든 테스트가 실행된 이후에 수행되어야 한다. JUnit4에서는 @AfterClass로 사용되었다. |
| @Disabled | 해당 테스트를 사용하지 않는다는 것을 명시. JUnit4에서는 @Ignore로 사용되었다. |

## 테스트 클래스와 테스트 메소드
---
### 테스트 클래스
- 테스트 클래스는 정적(static) 멤버 클래스 또는 하나 이상의 테스트 메소드를 가진 중첩 클래스이다.
- 테스트 클래스는 추상 클래스로 존재할 수 없다. 무조건 하나의 생성자를 가지고 있다.

### 테스트 메소드
- @Test, @RepeatedTest, @ParameterizedTest, @TestFactory, @TestTemplate 어노테이션을 가진 메소드를 말한다.

### 라이프사이클 메소드
- @BeforeAll, @AfterAll, @BeforeEach, @AfterEach 어노테이션을 가진, 명확한 실행 시점이 있는 메소드를 말한다.

#### Note
테스트 클래스, 테스트 메소드, 라이프사이클 메소드의 접근 제어자가 **public**일 필요는 없지만, **private**이 되어서는 안된다.
일반적으로 반드시 작성해야 하는 상황이 아니라면 테스트 클래스, 테스트 메소드, 라이프사이클 메소드의 public 접근 제어자는 생략한다.

## Display Names
---
테스트 클래스와 메소드는 @DisplayName 어노테이션을 통해 사용자가 원하는 이름으로 설정할 수 있다.
이름은 공백(띄어쓰기), 특수문자, 이모지(emojis) 또한 표현할 수 있다.

사용법은 다음과 같다.
```
@DisplayName("이름")
```

```
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("A special test case")
class DisplayNameDemo {

    @Test
    @DisplayName("Custom test name containing spaces")
    void testWithDisplayNameContainingSpaces() {
    }

    @Test
    @DisplayName("╯°□°）╯")
    void testWithDisplayNameContainingSpecialCharacters() {
    }

    @Test
    @DisplayName("😱")
    void testWithDisplayNameContainingEmoji() {
    }

}
```
##  Display Name Generators
---
@DisplayNameGeneration 테스트 화면에 표시되는 테스트 이름을 설정한다. 
만약 @DisplayName 어노테이션이 사용중이라면 @DisplayName 설정사항을 우선으로 따른다.

@DisplayNameGeneration의 설정 요소들은 다음과 같다.

| 이름 | 설명 |
| --- | --- |
| Standard | 기존 이름 사용 |
| Simple | 매개변수가 없는 메소드의 괄호 제거 |
| ReplaceUnderscores | 언더스코어(_)를 공백으로 변환 |
| IndicativeSentences | 테스트 이름과 테스트를 감싸고 있는 클래스 이름을 포함하는 하나의 문장을 생성한다. |

## Operating Sysytem Conditions
---
@EnabledOnOs 또는 @DisabledOnOs 어노테이션을 통해 특정 OS에 컨테이너, 메소드의 실행/중지 설정을 할 수 있다.

```
@Test
@EnabledOnOs(MAC)
void onlyOnMacOs() {
    // ...
}

@TestOnMac
void testOnMac() {
    // ...
}

@Test
@EnabledOnOs({ LINUX, MAC })
void onLinuxOrMac() {
    // ...
}

@Test
@DisabledOnOs(WINDOWS)
void notOnWindows() {
    // ...
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Test
@EnabledOnOs(MAC)
@interface TestOnMac {
}
```

## Method Order
---
메소드 실행 순서가 중요한 경우 @Order(순서) 어노테이션을 통해 순서를 설정할 수 있다.

```
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class OrderedTestsDemo {

    @Test
    @Order(1)
    void nullValues() {
        // perform assertions against null values
    }

    @Test
    @Order(2)
    void emptyValues() {
        // perform assertions against empty values
    }

    @Test
    @Order(3)
    void validValues() {
        // perform assertions against valid values
    }

}
```

## Repeated Tests
---
@RepeatedTest 어노테이션을 통해 해당 메소드의 반복 횟수를 설정할 수 있다.

```
@RepeatedTest(반복 횟수)
```
```
@RepeatedTest(10)
void repeatedTest() {
    // ...
}
```
## Parameterized Tests
---
Parameterized Tests는 다른 인수(arguments)로 여러번 테스트를 수행할 수 있도록 해준다. 
@Test 어노테이션 대신 @ParameterizedTest 어노테이션을 사용하며, 반드시 하나 이상의 인수를 지정해 주어야 한다.

```
@ParameterizedTest
@ValueSource(strings = { "racecar", "radar", "able was I ere I saw elba" })
void palindromes(String candidate) {
    assertTrue(StringUtils.isPalindrome(candidate));
}
```

위 코드의 테스트를 실행하면 @ValueSource 어노테이션에 설정된 "racecar", "radar", "able was I ere I saw elba"를 테스트 메소드 palindromes의 candidate 인수의 값으로 사용한다. 
StringUtils.isPalindrome(candidate)을 통해 회문(palindrome_앞에서 읽으나 뒤에서 읽으나 똑같은 문자열)인지 아닌지의 여부를 확인한다.

... 작성중

# 출처
* [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
