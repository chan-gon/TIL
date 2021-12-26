# Factory Method 패턴

객체 생성을 위한 인터페이스를 정의하고, 어떤 클래스의 인스턴스를 생성할지에 대해서는 자식 클래스가 결정하도록 위임하는 패턴.  
(객체 생성 처리를 서브 클래스로 분리하여 처리하도록 캡슐화하는 패턴)  

![Factory Method UML](../image/factorymethod_01.png)

# 예시

회사의 고용 상태에 따른 결과값을 출력하는 코드이다.  
고용 상태값은 '인턴', '정직원', '계약직' 세 가지가 있다.  

```
public abstract class EmploymentStatus {
    abstract String getStatus();
}

public class Intern extends EmploymentStatus {
    @Override
    String getStatus() {
        return "인턴";
    }    
}

public class FullTime extends EmploymentStatus{
    @Override
    String getStatus() {
        return "정직원";
    }
}

public class Contract extends EmploymentStatus{
    @Override
    String getStatus() {
        return "계약직";
    }
}
```

### Factory Method 구현
### 상단 UML 다이어그램에서 Creator 클래스에 해당

객체 생성을 위한 로직을 정의한다.
```
public abstract class EmployeeFactory {
    abstract EmploymentStatus createStatus(String status);
}
```

### 상단 UML 다이어그램에서 ConcreteCreator 클래스에 해당

어떤 상황에서 어떤 인스턴스를 생성할지 구체적으로 정의하는 클래스.  
부모 클래스가 아니라 자식 클래스(서브 클래스)가 인스턴스 생성을 결정한다.
```
public class ConcreteEmployeeFactory extends EmployeeFactory {

    @Override
    EmploymentStatus createStatus(String status) {
        if (status.equals("인턴")) {
            return new Intern();
        }
        if (status.equals("정직원")) {
            return new FullTime();
        }
        if (status.equals("계약직")) {
            return new Contract();
        }
        return null;
    }
    
}
```

# 실행

```
public class Main {
    public static void main(String[] args) {
        EmployeeFactory factory = new ConcreteEmployeeFactory();

        EmploymentStatus status1 = factory.createStatus("인턴");
        System.out.println(status1.getStatus());

        EmploymentStatus status2 = factory.createStatus("정직원");
        System.out.println(status2.getStatus());

        EmploymentStatus status3 = factory.createStatus("계약직");
        System.out.println(status3.getStatus());
    }
}

결과)
인턴
정직원
계약직
```

Factory Method는 클래스 간의 결합도를 낮추기 위해 사용된다.  
직접 객체를 사용하지 않고 서브 클래스에 위임하여 객체간의 간섭을 줄인다. 따라서 한 객체의 변화가 다른 객체에 영향을 주지 않는 구조가 된다.

# 참고
* [design-patterns](https://www.coursera.org/learn/design-patterns)
* [밤둘레](https://bamdule.tistory.com/157)