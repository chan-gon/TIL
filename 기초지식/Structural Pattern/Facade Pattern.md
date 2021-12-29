# Facade Pattern

서비스에 접근하는 클라이언트는 서비스의 복잡한 내부 동작 방식을 이해하고 서비스를 사용하지 않는다. 외부의 간단한 인터페이스 조작만으로 서비스를 이용할 수 있다. Facade Pattern은 이것을 실현하는데 집중한다.  
서브 시스템의 복잡한 로직을 캡슐화하여 facade 클래스를 통해 클라이언트와 상호작용 하도록 한다. 이를 통해 클라이언트는 서브 시스템을 신경쓰지 않아도 된다.  

# 예시


첫번째
```
class CPU {
    public void freeze() { ... }
    public void jump(long position) { ... }
    public void execute() { ... }
}

class Memory {
    public void load(long position, byte[] data) { ... }
}

class HardDrive {
    public byte[] read(long lba, int size) { ... }
}

/* Facade */

class ComputerFacade {
    private CPU processor;
    private Memory ram;
    private HardDrive hd;

    public ComputerFacade() {
        this.processor = new CPU();
        this.ram = new Memory();
        this.hd = new HardDrive();
    }

    public void start() {
        processor.freeze();
        ram.load(BOOT_ADDRESS, hd.read(BOOT_SECTOR, SECTOR_SIZE));
        processor.jump(BOOT_ADDRESS);
        processor.execute();
    }
}

/* Client */

class You {
    public static void main(String[] args) {
        ComputerFacade computer = new ComputerFacade();
        computer.start();
    }
}
```

두번째
```
public class Inventory {
public String checkInventory(String OrderId) {
    return "Inventory checked";
}
}

public class Payment {
public String deductPayment(String orderID) {
    return "Payment deducted successfully";
}
}


public class OrderFacade {
private Payment pymt = new Payment();
private Inventory inventry = new Inventory();

public void placeOrder(String orderId) {
    String step1 = inventry.checkInventory(orderId);
    String step2 = pymt.deductPayment(orderId);
    System.out
            .println("Following steps completed:" + step1
                    + " & " + step2);
   }
}

public class Client {
       public static void main(String args[]){
         OrderFacade orderFacade = new OrderFacade();
         orderFacade.placeOrder("OR123456");
         System.out.println("Order processing completed");
       }
  }
```

# 참고
* [design-patterns](https://www.coursera.org/learn/design-patterns)
* [StackOverflow](https://stackoverflow.com/questions/5242429/what-is-the-facade-design-pattern)