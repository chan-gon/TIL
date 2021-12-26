# Strategy 패턴

Strategy 패턴은 행위를 클래스로 캡슐화하여 동적으로 원하는 행위를 자유롭게 바꿀 수 있는 패턴이다.  
목적에 따라 필요한 전략(Strategy)으로 교체하여 목적을 달성하도록 해주는 패턴이다.  
*전략 : 어떤 목적을 달성하기 위해 일을 수행하는 방식, 비즈니스 규칙, 문제를 해결하는 알고리즘*  

- 특정한 계열의 알고리즘들을 정의하고(Define a family of algorithms)
- 각 알고리즘을 캡슐화하며(Encapsulate each algorithm)
- 이 알고리즘들을 해당 계열 안에서 상호 교체가 가능하게 만든다.(Make the algorithms interchangeable within that family)

*(출처: 위키백과)*

Strategy 패턴을 표현한 UML 클래스는 아래와 같다.

![UML 클래스](./image/strategy_01.png)

- Strategy : 인터페이스나 추상 클래스로 외부에서 동일한 방식으로 알고리즘을 호출하는 방법을 명시
- ConcreteStrategy : Strategy 패턴에서 명시한 알고리즘을 실제로 구현한 클래스
- Context : Strategy 패턴을 이용하는 역할을 수행.필요에 따라 동적으로 구체적인 전략을 바꿀 수 있도록 setter 메서드(‘집약 관계’)를 제공한다.

# 예시

당신은 수제 햄버거 가게를 열었다. 주요 메뉴는 치즈 버거, 치킨 버거, 피시 버거가 있고, 패티 위에 뿌려질 소스는 머스타드와 타르타르가 제공된다.  
일단 Strategy 패턴은 신경쓰지 않고 구현해본다.  

```
public abstract class Hamburger {
    private String name;

    public Hamburger(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public abstract void patty();
    public abstract void sauce();
    
}

public class ChickenBurger extends Hamburger{

    public ChickenBurger(String name) {
        super(name);
    }

    @Override
    public void patty() {
        System.out.println("치킨");
        
    }

    @Override
    public void sauce() {
        System.out.println("머스타드");
        
    }
    
}

public class FishBurger extends Hamburger{

    public FishBurger(String name) {
        super(name);
    }

    @Override
    public void patty() {
        System.out.println("생선 필렛");
        
    }

    @Override
    public void sauce() {
        System.out.println("타르타르");
        
    }
    
}

public class Client {
    public static void main(String[] args) {
        Hamburger order1 = new ChickenBurger("chicken burger");
        Hamburger order2 = new FishBurger("fish burger");

        System.out.println("새로운 주문 = " + chicken.getName());
        order1.patty();
        order1.sauce();

        System.out.println("새로운 주문 = " + fish.getName());
        order2.patty();
        order2.sauce();
        
    }
    
}

결과)
새로운 주문 = chicken burger
치킨
머스타드
새로운 주문 = fish burger
생선 필렛
타르타르
```

위 코드의 문제점은 다음과 같다.
- 유동적인 변화에 취약하다.
  - 만약 고객의 요구로 치킨 버거의 소스가 타르타르로 변경되었다면?
  - 새로운 종류의 패티나 소스가 추가된다면?
- 새로운 기능을 변경하려면 기존 코드의 내용을 수정해야 하므로 확장에 열려있고 수정에 대해서는 닫혀 있어야 한다는 개방-폐쇄 원칙에 위배된다.

문제 해결의 실마리는 다음과 같다.
- 변화되는 부분을 찾아낸 후 이를 클래스로 캡슐화한다.

햄버거 만들기에 사용되는 **패티 종류 선택** 그리고 **소스 종류 선택**의 변화가 주요한 문제이다. 이를 캡슐화한다.  

```
public abstract class Hamburger {
    private String name;
    private PattyStrategy pattyStrategy;
    private SauceStrategy sauceStrategy;

    public Hamburger(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void patty() {
        pattyStrategy.patty();
    }
    public void sauce() {
        sauceStrategy.sauce();
    }

    public void setPattyStrategy(PattyStrategy pattyStrategy) {
        this.pattyStrategy = pattyStrategy;
    }

    public void setSauceStrategy(SauceStrategy sauceStrategy) {
        this.sauceStrategy = sauceStrategy;
    }
    
}

public class ChickenBurger extends Hamburger{

    public ChickenBurger(String name) {
        super(name);
    }
   
}

public class FishBurger extends Hamburger{

    public FishBurger(String name) {
        super(name);
    }

}
```

```
public interface PattyStrategy {
    public void patty();
}

public class ChickenPatty implements PattyStrategy {

    @Override
    public void patty() {
        System.out.println("치킨 패티");
        
    }
    
}

public class FishPatty implements PattyStrategy{

    @Override
    public void patty() {
        System.out.println("생선 패티");
        
    }
    
}
```

```
public interface SauceStrategy {
    public void sauce();
}

public class MustardSauce implements SauceStrategy{

    @Override
    public void sauce() {
        System.out.println("머스타드 소스");
        
    }
    
}

public class TarTarSauce implements SauceStrategy{

    @Override
    public void sauce() {
        System.out.println("타르타르 소스");
        
    }
    
}
```

```
public class Client {
    public static void main(String[] args) {
        Hamburger order1 = new ChickenBurger("치킨 버거");
        Hamburger order2 = new FishBurger("피시 버거");

        System.out.println("새로운 주문 = " + order1.getName());
        System.out.println("소스는 타르타르 소스로 주세요.");
        order1.setPattyStrategy(new ChickenPatty());
        order1.setSauceStrategy(new TarTarSauce());
        order1.patty();
        order1.sauce();

        System.out.println();

        System.out.println("새로운 주문 = " + order2.getName());
        System.out.println("소스는 타르타르 말고 머스터드로 주세요.");
        order2.setPattyStrategy(new FishPatty());
        order2.setSauceStrategy(new MustardSauce());
        order2.patty();
        order2.sauce();
        
    }
    
}

결과)
새로운 주문 = 치킨 버거
소스는 타르타르 소스로 주세요.
치킨 패티
타르타르 소스

새로운 주문 = 피시 버거
소스는 타르타르 말고 머스터드로 주세요.
생선 패티
머스타드 소스
```

- 패티와 소스를 정의하는 클래스를 PattyStrategy, SauceStrategy 인터페이스로 캡슐화했다.
- 유연한 변경에 대해 기존 코드가 영향을 받지 않는다. 따라서 개방-폐쇄 원칙을 만족한다.
  - 원하는대로 패티와 소스를 선택하는 것을 구현하기 위해 Hamburger 클래스에 setPattyStrategy, setSauceStrategy와 같은 setter 메소드를 구현했다.

# 참고
* [Heee's Development Blog](https://gmlwjd9405.github.io/2018/07/06/strategy-pattern.html)