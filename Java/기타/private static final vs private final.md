# private static final vs private final

- static final : 해당 변수를 한 번만 생성. 'one constant variable per class_클래스 당 하나의 상수 변수'. 
- final : 매 객체마다 해당 변수 생성. 'one constant variable per instance_인스턴스 당 하나의 상수 변수'.

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

# 참고
* [stackoverflow](https://stackoverflow.com/questions/1415955/private-final-static-attribute-vs-private-final-attribute)
* [Carry On Programming](https://zorba91.tistory.com/275)
* [장인개발자를 꿈꾸는](https://devbox.tistory.com/entry/Java-static)




