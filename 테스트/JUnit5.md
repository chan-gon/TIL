# JUnit5

JUnit5는 JUnit Platform, JUnit Jupiter, JUnit Vintage가 함쳐진 형태를 가지고 있다.


## 사용 환경
Java8 이상의 환경을 요구하지만, 이전 JDK 버전으로 작성된 코드 또한 테스트 가능하다.

## 사용 어노테이션

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

... 작성중

# 출처
* [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
