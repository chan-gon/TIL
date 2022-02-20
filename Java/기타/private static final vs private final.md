# private static final vs private final

- static final : 해당 변수를 한 번만 생성. 'one constant variable per class_클래스 당 하나의 상수 변수'. 
- final : 매 객체마다 해당 변수 생성. 'one constant variable per instance_인스턴스 당 하나의 상수 변수'.

## private static final

static 키워드로 생성된 변수는 해당 변수가 있는 클래스 로드 중에 초기화되며, 애플리케이션 실행 내내 메모리에 머물러 있다. 따라서 초기화 과정 없이 해당 변수, 메소드에 접근할 수 있다.

non-static(static 키워드 없이 생성된) 변수는 새로운 객체를 생성할 때마다 초기화된다.

자바 코드 컨벤션에 따르면 static final 변수 이름은 대문자(uppercase)로 작성되어야 한다.

아래 코드는 static final 키워드로 임의의 난수를 생성하는 변수를 생성했다. 그리고 해당 변수를 가진 클래스 인스턴스를 세 개 생성하여 static final 키워드가 붙은 변수인 경우 어떤 값을 출력하는지 알아보았다.
출력 결과 서로 다른 인스턴스를 생성하여 다른 값이 나올 것이라 기대했지만, static final 키워드로 변수를 생성했기 때문에 해당 변수가 한 번만 생성되었고, 최초로 생성된 값을 계속 불러온다는 것을 알 수 있었다.

<pre>
<code>
public class ConstTest {

    private static final double NUMBER = Math.random();

    public void TestClass() {
        System.out.println(NUMBER);
    }

    public static void main(String[] args) {

        ConstTest c1 = new ConstTest();
        c1.TestClass();

        ConstTest c2 = new ConstTest();
        c2.TestClass();

        ConstTest c3 = new ConstTest();
        c3.TestClass();
    }
 
}

결과)
0.4881718709697359
0.4881718709697359
0.4881718709697359
</code>
</pre>

반대로 변수의 static 키워드를 제외하고 실행하면 해당 변수는 새로운 객체를 생성할 때마다 초기화되기 때문에 
서로 다른 값이 생성되는 것을 확인할 수 있었다.

<pre>
<code>
public class ConstTest {

    private final double NUMBER = Math.random();

    public void TestClass() {
        System.out.println(NUMBER);
    }

    public static void main(String[] args) {

        ConstTest c1 = new ConstTest();
        c1.TestClass();

        ConstTest c2 = new ConstTest();
        c2.TestClass();

        ConstTest c3 = new ConstTest();
        c3.TestClass();
    }
 
}

결과)
0.9380501985444206
0.07243411825169088
0.4107689500978309
</code>
</pre>

## private final

예약어 final은 클래스, 메소드, 인스턴스 변수와 클래스 변수에 선언되어 자주 사용된다.

## 클래스를 final 예약어로 생성하는 경우
String 클래스와 같이 클래스 내부가 외부의 변경으로부터 보호되어야 하는 경우. 즉 더 이상 확장해서는 안 되는 클래스, 누군가 해당 클래스를 상속 받아서 내용을 변경해서는 안 되는 클래스를 선언할 때 final로 선언하면 된다.

## 메소드를 final로 선언하는 경우
메소드를 final로 선언하면 Overriding 할 수 없다. 그 이유는 클래스를 final로 선언하는 이유와 동일하다. 외부의 변경으로부터 보호하는 목적 때문이다.  

## 인스턴스 변수나 클래스 변수를 final로 선언하는 경우

인스턴스 변수나 클래스 변수를 final로 선언하는 기본적인 이유는 클래스, 메소드를 final로 선언하는 이유와 동일하다. **초기 설정을 변경하지 않도록** 하기 위함이다.  
여기서 말하는 **초기 설정** 이라는 것은 **초기화**를 의미한다. 즉 final로 인스턴스 변수나 클래스 변수를 선언하는 동시에 초기화 작업이 필요하다.  

final 로 선언한 인스턴스 변수나 클래스 변수를 초기화하지 않으면 어떻게 되는지 살펴보자.  
Member.java 클래스를 생성한 후 아래와 같이 인스턴스 변수 age를 final로 생성한다.  

```
public class Member {
    private final int age;
}
```

이렇게 final 변수를 생성하고 초기화 작업을 하지 않으면 컴파일 작업을 하기도 전에 이미 에러가 발생했다고 빨간줄이 표시될 것이다.  
에러 메시지를 살펴보면 final 변수 age가 초기화 되지 않았다고 한다.  

```
[에러 메시지]
The blank final field age may not have been initialized
```

이렇게 초기화를 하면 에러가 사라진다.  

```
[초기화 작업을 통해 에러 해결]
public class Member {
    private final int age = 1;
}
```

## 왜 final 변수는 생성과 동시에 초기화를 해야 할까?

생성자나 메소드를 통해 초기화 작업을 하면 되지 않을까 생각할 수 있다. 그런데 그렇게 하면 변수의 중복이 발생할 가능성이 있다. 이것은 final로 변수를 선언하는 기본적인 이유인 **초기 설정을 변경 없이 유지하는 것**에 위배된다. 생성자 또는 메소드를 통한 초기화 작업이 안 될 것은 없다. 하지만 final로 선언하는 이유를 생각해 보면 어떤 방식이 최선인가에 대한 답은 명확해진다.  

## 매개 변수나 지역 변수를 final로 선언하는 경우는?

```
public void getMessage(final String 매개 변수) {
    final String 지역 변수
}
```

매개 변수나 지역 변수를 final로 선언하는 경우 초기화 작업 없이 사용할 수 있다.  
매개 변수는 이미 초기화가 되어서 넘어 왔고, 지역 변수는 메소드를 선언하는 중괄호 내에서만 참조되므로 다른 곳에서 변경할 일이 없다. 

하지만 다음과 같이 사용하면 안된다.  
애초에 final로 선언된 변수이기 때문에 초기에 할당된 값을 변경하려고 하면 에러가 발생한다.

```
public void getMessage(final String 매개 변수) {
    final String 지역 변수
    지역 변수 = "a";
    지역 변수 = "b"; // 에러 발생!
    매개 변수 = "new message"; // 에러 발생!
}
```

```
[에러 메시지]
The final local variable 지역 변수 may already have been assigned
The final local variable 매개 변수 cannot be assigned. It must be blank and not using a compound assignment.
```

## 참조 자료형을 final로 선언하는 경우는?

참조 자료형은 기본형(Primitive type)을 제외한 모든 타입을 말한다.  
클래스 타입, 인터펭스 타입, 배열 타입, 열거 타입 등이 있다.  

참조 자료형을 final로 선언하면 어떻게 되는지 알아보자.  
참조 자료형을 final로 선언해도 문제는 없다. 중요한 점은, 기본 자료형과 마찬가지로 두 번 이상 값을 할당하거나 새로 생성자를 사용해서 초기화를 할 수 없다. 그렇게 하면 아래와 같은 에러가 발생한다.

```
public class Member {
    private final Service service = new Service();
    
    public void setService() {
        service = new Service(); // 에러 발생!
    }

}
```

```
[에러 메시지]
The final field Member.service cannot be assigned
```

# 참고
* [자바의 신](http://www.yes24.com/Product/Goods/42643850)
* [stackoverflow](https://stackoverflow.com/questions/1415955/private-final-static-attribute-vs-private-final-attribute)
* [Carry On Programming](https://zorba91.tistory.com/275)
* [장인개발자를 꿈꾸는](https://devbox.tistory.com/entry/Java-static)
* [인스턴스 변수, 클래스 변수, 지역 변수](https://itmining.tistory.com/20)