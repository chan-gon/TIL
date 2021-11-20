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

... 작성중

# 출처
* [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
