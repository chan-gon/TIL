# Command 패턴

Command 패턴은 명령을 객체로 캡슐화한다. 명령을 객체의 형태로 캡슐화하여 사용자가 보낸 명령을 나중에 이용할 수 있도록 메소드 이름, 매개변수 등 명령에 필요한 정보를 저장 또는 로깅, 취소할 수 있게 하는 패턴이다.  

# 구조

![Command 패턴 구조](../image/command_01.png)

- Command : 명령을 수행하는 메소드를 정의하는 인터페이스(interface).
- ConcreteCommand : 실행되는 기능을 구현하는 클래스. Receiver에서 정의된 메소드를 활용해 세부 구현을 한다.
- Receiver : ConcreteCommand에서 Command의 인터페이스를 구현할 때 필요한 클래스. ConcreteCommand의 기능을 실행하기 위해 사용되는 수신자 클래스. Client가 요청한 내용에 대해 액션만 취해주면 된다.(어떤 Client인지는 알 필요가 없다)
  - 비즈니스 로직을 작성. 실질적인 동작을 구현한다. 
- Invoker : 기능의 실행을 요청하는 호출자 클래스.
  - Command 객체를 참조하는 필드가 반드시 필요하다. Receiver에게 요청을 곧바로 전달하지 않고 Command 객체를 거쳐서 전달한다. 
- Client : 요청자. Command 객체 생성, Invoker를 통해 Receiver에게 필요한 행위를 전달한다.
  - 내 요청을 누가(Receiver) 구체적으로 어떻게 처리할 지만(ConcreteCommand) 알고 있으면 된다.

# 동작 순서

1. Client에서 Command 객체를 생성한다.
2. Invoker로 Command 객체를 저장한다.
3. Client에서 Invoker를 통해 행동 명령을 전송한다.
4. Receiver가 행위를 한다.

# 예시

방의 불을 키고 끄는 기능을 가지고 있는 리모컨을 구현한다.  
리모컨은 불을 키고 끄기 위해 어떤 로직을 거쳐야 하는지 알 필요가 없다.(캡슐화를 통한 정보은닉)   
단순하게 **버튼을 누르고 결과가 어떻게 되는지만 알면 된다.**

## Command
```
public interface Command {
    void execute();
}
```

## Receiver
```
public class Light {
    void on() {
        System.out.println("Light is on");
    }
    void off() {
        System.out.println("Light is off");
    }
}

```

## ConcreteCommand

```
public class LightOnCommand implements Command{

    Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }
    @Override
    public void execute() {
        light.on();
        
    }
    
}

public class LightOffCommand implements Command{
    Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }
    @Override
    public void execute() {
        light.off();
        
    }
    
}
```

## Invoker

```
public class Invoker {
    Command button;

    public void setCommand(Command command) {
        button = command;
    }

    public void buttonWasPressed() {
        button.execute();
    }
}
```

## Client

```
public class Client {
    public static void main(String[] args) {
        Invoker remote = new Invoker();
        Light light = new Light();
        
        remote.setCommand(new LightOnCommand(light));
        remote.buttonWasPressed();
        remote.setCommand(new LightOffCommand(light));
        remote.buttonWasPressed();
    }
}
```

Invoker에 추가 명령을 등록하거나 수정할 수 있다.  
명령이나 Receiver가 더 추가된다면 기존 코드의 수정 없이 setCommand를 수정하면 된다.

# 참고
* [GeeksforGeeks](https://www.geeksforgeeks.org/command-pattern/?ref=gcse)
* [huisam.tistory.com](https://huisam.tistory.com/entry/CommandPattern)
* [brownbears.tistory.com](https://brownbears.tistory.com/561)