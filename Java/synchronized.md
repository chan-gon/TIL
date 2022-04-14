# synchronized

멀티 스레딩 환경에서 여러 프로세스/스레드가 동시에 같은 데이터에 접근할 때 타이밍이나 접근 순서에 따라 결과가 달라질 수 있는 상황을 경합 조건(race condition) 이라고 합니다. 경합 조건 상태에서 애플리케이션을 개발하면 의도하지 않은 결과 출력 또는 버그가 발생할 수 있습니다.

경합 조건을 방지하고 여러 프로세스/스레드를 동시에 실행해도 공유 데이터의 일관성을 유지하는 것을 동기화(synchronization)라고 합니다.

Java에서는 synchronized 키워드를 사용해서 동기화를 구현합니다. 이 키워드를 사용하면 synchronized 블럭이 생성되는데, 이는 한 번에 하나의 스레드만 허용한다는 의미를 가집니다.

먼저 경합 조건이 어떻게 발생하는지 알아봅시다.

```
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class racecondition {

    private int sum = 0;

    public void calculate() {
        setSum(getSum() + 1);
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getSum() {
        return sum;
    }
    
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(3);
        racecondition summation = new racecondition();
        IntStream.range(0, 1000)
                    .forEach(count -> service.submit(summation::calculate));
        service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        System.out.println(summation.getSum());
    }
}
```

메소드를 실행하면 1을 더하는 로직을 구현합니다. 그리고 임의의 스레드 3개를 생성해서 1000번 연산합니다.

main 메소드를 보면 스레드를 생성하고 1000번 1씩 더하는 연산을 수행하도록 구현했습니다. 예상되는 결과는 1000 입니다만, 메소드를 실행하면 1000이 나오지 않고 900 언저리의 숫자만 출력됩니다. 하나의 클래스 인스턴스에 여러 스레드가 접근했기 때문에 발생한 문제입니다.

이제 synchronized 키워드를 이용한 동기화 구현을 진행해봅니다.

synchronized 키워드를 통한 동기화 구현을 다음 세 가지 레벨에서 진행해봅니다.

- Instance methods

- Static methods
- Code blocks

## Monitor

Java에서 synchronized를 통해 동기화를 구현할 때 내부적으로 monitor의 도움으로 동기화 작업을 수행합니다. Java의 모든 객체는 monitor를 가지고 있고, monitor의 주요 관심사는 어떻게 스레드가 객체에 접근하는가입니다. 즉 스레드의 객체 접근을 모니터링하는 것입니다. monitor의 감독 하에 한 번에 하나의 스레드만 lock, unlock 할 수 있습니다. 여기서 말하는 lock/unlock은 객체(또는 리소스) 점유/점유 해제를 의미합니다. 

synchronized 키워드를 사용하면 synchronized 블록 안의 객체가 원래 가지고 있는 monitor를 통해 스레드 또한 monitor 감독하에 놓이게 됩니다. 이를 통해 객체에 lock을 걸어서 다른 스레드가 해당 객체에 접근하지 못하게 합니다. 그리고 synchronized 블록 내부의 실행 구문을 수행하고 나면 unlock이 실행되고, 스레드의 해당 객체에 대한 점유가 해제됩니다. 그러면 이제 다른 스레드가 이 객체에 접근할 수 있는 상황이 됩니다.

## Synchronized Instance Methods

호출되는 주요 메소드에 synchronized 키워드를 추가합니다.  
이제 클래스 인스턴스당 오직 하나의 스레드만 이 메소드를 호출할 수 있습니다.  
결과를 확인하면 의도한 값 1000이 출력되는 것을 확인할 수 있습니다.

```
public synchronized void calculate() {
        setSum(getSum() + 1);
}
```

## Synchronized Static Methods

인스턴스 메소드와 마찬가지로 정적 메소드 또한 synchronized 키워드를 통한 동기화 적용이 가능합니다.  
static 필드, 메소드를 추가한 후 구현 방식을 수정해서 결과를 출력해보면 1000이 반환되는 것을 확인할 수 있습니다.  

```
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class racecondition {

    private int sum = 0;
    public static int staticSum = 0;

    public void calculate() {
        setSum(getSum() + 1);
    }

    public static synchronized void staticCalculate() {
        staticSum = staticSum + 1;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getSum() {
        return sum;
    }
    
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();
        IntStream.range(0, 1000)
                    .forEach(count -> service.submit(racecondition::staticCalculate));
        service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        System.out.println(racecondition.staticSum);
    }
}
```

## Synchronized Blocks Within Methods

호출하는 메소드 전체에 동기화를 적용시키지 않고 특정 부분에만 동기화 기능이 작동하도록 하고 싶다면, synchronized 블록을 사용합니다.

syncBlockedCalculate() 메소드를 추가합니다. 메소드 실행 구문은 synchronized 블록을 생성하고 원하는 객체(this)를 참조하게 합니다. 자바의 모든 객체는 monitor를 가지고 있고, 이 상태에서 동기화를 구현했기 때문에 하나의 스레드만 해당 객체에 접근할 수 있습니다. 

```
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class racecondition {

    private int sum = 0;

    public void calculate() {
        setSum(getSum() + 1);
    }

    public void syncBlockedCalculate() {
        synchronized (this) {
            setSum(getSum() + 1);
        }
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getSum() {
        return sum;
    }
    
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(3);
        racecondition thread = new racecondition();

        IntStream.range(0, 1000)
                    .forEach(count -> service.submit(thread::syncBlockedCalculate));
        service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        System.out.println(thread.getSum());
    }
}
```

만약 메소드가 정적 메소드라면 다음과 같이 작성합니다. 참조 객체를 지정하는 방법이 다릅니다.

```
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class racecondition {

    private int sum = 0;
    public static int staticSum = 0;

    public void calculate() {
        setSum(getSum() + 1);
    }

    public static void syncBlockedCalculate() {
        synchronized (racecondition.class) {
            staticSum = staticSum + 1;
        }
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getSum() {
        return sum;
    }
    
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();

        IntStream.range(0, 1000)
                    .forEach(count -> service.submit(racecondition::syncBlockedCalculate));
        service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        System.out.println(racecondition.staticSum);
        
    }
}
```

## 참고
* [이해가 쉬운 블로그](https://blog.naver.com/myca11/222626313972)
* [Baeldung](https://www.baeldung.com/cs/race-conditions)
* [Java Language Specification](https://docs.oracle.com/javase/specs/jls/se8/html/jls-17.html#jls-17.1)