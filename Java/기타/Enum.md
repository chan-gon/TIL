# Enum

어떤 클래스가 상수(constant)로만 만들어져 있는 경우 반드시 class로 선언할 필요는 없다. 이럴 때는 enum 키워드로 열거형 클래스를 생성할 수 있다.  

## 자바 1.5 이전에 사용했던 열거 패턴

Enum은 자바 1.5부터 등장했다. 그 이전에는 상수값을 정수 열거 패턴, 문자열 열거 패턴 등으로 표현했다.

<pre>
<code>
public class Calculator {
    private static final String PLUS = "+";
    private static final String MINUS = "-";
    private static final String MULTIPLY = "*";
    private static final String DIVIDE = "/";
}
</code>
</pre>

열거 패턴의 단점은 클라이언트와 함께 컴파일 되기 때문에 상수의 값이 바뀌면 다시 컴파일 해야 한다는 것이다.
또한 위 코드와 같은 문자열 열거 패턴의 경우 문자열 비교에 따른 성능저하가 발생한다.

## Enum 클래스

클래스 키워드 자리에 enum 키워드를 입력한다.
Enum 클래스는 하나의 객체를 public static final 필드 형태로 제공한다.

<pre>
<code>
public enum Calculator {
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE;
}
</code>
</pre>

만약 열거된 필드에 특정 값을 넣고 싶다면 다음과 같이 작성한다. **PLUS("+")**  
enum 상수에 데이터를 넣으려면 객체 필드를 선언하고 생성자를 통해 받은 데이터를 그 필드에 저장하면 된다.

상수 아래에 operator라는 변수가 final로 선언되어 있다. 이 변수는 Calculator 생성자에서 매개 변수로 넘겨받은 값을 할당할 때 사용된다.

enum 클래스는 생성자를 사용할 수 있지만, 생성자 선언부에 public 이라고 되어 있지 않다. enum 클래스 생성자는 아무것도 명시하지 않는 package-private과 private만 접근 제어자로 사용할 수 있다.

getOperator() 메소드는 enum 클래스의 변수로 선언한 operator 값을 리턴하기 위해 만들었다.

<pre>
<code>
public enum Calculator {
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    private final String operator;

    Calculator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }
}
</code>
</pre>

일반적으로 switch문을 통해 enum 클래스를 사용한다.

<pre>
<code>
public enum Calculator {
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    private final String operator;

    Calculator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public double calculate(int a, int b) {
        switch(operator){
            case "+":
                return a + b;

            case "-":
                return a - b;

            case "*":
                return a * b;

            case "/":
                return a / b;

            default:
                throw new IllegalArgumentException();
        }
    }
}

public class CalculatorTest {
    public static void main(String[] args) {
        System.out.println(Calculator.PLUS.calculate(1, 2)); // 3.0
    }
}
</code>
</pre>

enum 클래스의 메소드는 다음과 같다.

| 메소드 | 내용 |
| --- | --- |
| compareTo(E e) | 매개 변수로 enum 타입과의 순서(ordinal) 차이를 리턴한다. |
| getDeclaringClass() | 클래스 타입의 enum을 리턴한다. |
| name() | 상수의 이름을 리턴한다. |
| ordinal() | 상수의 순서를 리턴한다. |
| valueOf(Class<T> enumType, String name) | static 메소드. 첫 번째 매개 변수로 클래스 타입의 enum을, 두 번째 매개 변수로 상수의 이름을 넘겨준다. |

enum 클래스의 API 문서에 없는 특수한 메소드가 있는데, values() 메소드가 있다. 이 메소드는 enum 클래스에 선언된 모든 상수를 배열로 리턴한다.

<pre>
<code>
public enum Calculator {
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    private final String operator;

    Calculator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }
}

public class CalculatorTest {
    public static void main(String[] args) {
        Calculator cal[] = Calculator.values();
        for(Calculator operator : cal) {
            System.out.println(operator);
        }
    }
}

결과)
PLUS
MINUS
MULTIPLY
DIVIDE
</code>
</pre>

# 참고
* [webdevtechblog](https://webdevtechblog.com/enum%EC%9D%84-%EC%82%AC%EC%9A%A9%ED%95%9C-%EC%83%81%EC%88%98%EA%B0%92-%EA%B4%80%EB%A6%AC-a3e3fb73eae1#)
* [webdul](https://wedul.site/285)
* [자바의 신](http://www.yes24.com/Product/Goods/42643850)
