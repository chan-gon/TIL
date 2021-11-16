# BiFunction 인터페이스

BiFunction은 두 개의 인자(Type T, Type U)를 받고 하나의 객체(Type R)를 리턴하는 함수형 인터페이스이다.

- T : 함수에 적용될 첫 번째 인수 타입
- U : 함수에 적용될 두 번째 인수 타입
- T : 함수의 결과 타입

<pre>
<code>
@FunctionalInterface
public interface BiFunction<T, U, R> {

      R apply(T t, U u);

}
</code>
</pre>

BiFunction은 하나의 추상 메서드와 하나의 디폴트 메서드를 가지고 있다.

| 메서드 이름 | 종류 | 설명 |
| --- | --- | --- |
| apply(T t, U u) | Abstract Methods | 주어진 인수에 적용되는 메서드 |
| andThen(Function<? super R,? extends V> after) | Default Methods | Returns a composed function that first applies this function to its input, and then applies the after function to the result. |

다음 코드는 두 개의 Integer 타입 인자와 Integer, Double, List 타입 리턴 객체를 반환하는 코드이다.

<pre>
<code>
public class Main {
    public static void main(String[] args) {

        BiFunction<Integer, Integer, Integer> biFunc = (a, b) -> a + b;
        Integer result = biFunc.apply(2, 3);
        System.out.println(result); // 5

        BiFunction<Integer, Integer, Double> biFunc2 = (a, b) -> Math.pow(a, b);
        Double result2 = biFunc2.apply(2, 3);
        System.out.println(result2); // 8.0

        BiFunction<Integer, Integer, List<Integer>> biFunc3 = (a, b) -> Arrays.asList(a + b);
        List<Integer> result3 = biFunc3.apply(2, 3);
        System.out.println(result3); // [5]

    }
}

결과)
5
8.0
[5]
</code>
</pre>

다음 코드는 두 개의 Integer 타입 인자를 받아서 Double 타입으로 반환하는 BiFunction<T, U, R> 인터페이스를 구현하고,
andThen() 메서드로 Function<T, R> 메서드와 연결하여 Double 타입을 String 타입으로 변환하는 코드이다.

<pre>
<code>
public class Main {
    public static void main(String[] args) {

        BiFunction<Integer, Integer, Double> biFunc1 = (a, b) -> Math.pow(a, b);

        Function<Double, String> biFunc2 = (input) -> "결과: " + String.valueOf(input);

        String result = biFunc1.andThen(biFunc2).apply(2,3);

        System.out.println(result);

    }
}

결과)
8.0
</code>
</pre>

다음 코드는 위의 코드를 BiFunction과 Function을 인수로 하는 메서드로 변환하고 연결한 코드이다.

<pre>
<code>
public class Main {
    public static void main(String[] args) {

        String result = powToString(2, 3,
            (a, b) -> Math.pow(a, b),
            (r) -> "결과: " + String.valueOf(r));

        System.out.println(result);

    }

    public static <R> R powToString(Integer a, Integer b,
                                    BiFunction<Integer, Integer, Double> biFunc1,
                                    Function<Double, R> biFunc2) {
      
      return biFunc1.andThen(biFunc2).apply(a, b);
    }

}

결과)
8.0
</code>
</pre>

# 출처
* [Mkyong](https://mkyong.com/java8/java-8-bifunction-examples/)