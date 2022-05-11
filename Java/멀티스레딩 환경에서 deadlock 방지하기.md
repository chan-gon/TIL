# 멀티스레딩 환경에서 deadlock 방지하기

프로세스는 (매우 간단하게 말하면)현재 실행 중인 프로그램을 의미한다. 당장 **Ctrl + Shift + Esc**(Windows 기준)를 누르면 작업 관리자 창에서 현재 실행 중인 프로세스를 확인 할 수 있다.   

스레드는 프로세스 내부의 작업을 수행하는 주체이다.  
프로세스에 하나 이상의 스레드가 존재하며, 두 개 이상의 스레드가 존재하는 경우 멀티 스레드라고 말한다.  
스레드가 작업을 수행하려면 개별 메모리 공간이 필요하다. 그리고 스레드는 프로세스 내부에서 움직이기 때문에 프로세스의 메모리 허용치에 따라 생성 가능한 스레드의 개수가 결정된다. 

데드락(Deadlock)이란 스레드가 실행되지 않고 계속 대기하는 상태를 말한다. 왜 이런 일이 발생할까?  
둘 이상의 스레드가 동시에 동일한 자원에 접근하면 데드락이 발생한다. 

데드락 발생 조건은 다음과 같다.

- 상호 배제 (Mutual Exclusion) : 한 자원에 대해 여러 쓰레드 동시 접근 불가(한 번에 하나의 스레드만 실행)

- 점유와 대기 (Hold and Wait) : 자원을 가지고 있는 상태에서 다른 쓰레드가 사용하고 있는 자원 반납을 기다리는 것

- 비선점 (Non Preemptive) : 다른 쓰레드의 자원을 실행 중간에 강제로 가져올 수 없음

- 환형대기 (Circle Wait) : 각 쓰레드가 순환적으로 다음 쓰레드가 요구하는 자원을 가지고 있는 것

의도적으로 데드락 상황을 구현해보자.

```
package Java;

public class DeadlockTest {
  public static Object Lock1 = new Object();
  public static Object Lock2 = new Object();
  
  public static void main(String args[]) {
     ThreadDemo1 T1 = new ThreadDemo1();
     ThreadDemo2 T2 = new ThreadDemo2();
     T1.start();
     T2.start();
  }
  
  private static class ThreadDemo1 extends Thread {
     public void run() {
        synchronized (Lock1) {
           System.out.println("Thread 1: Holding lock 1...");
           
           try { 
              Thread.sleep(10); 
           } catch (InterruptedException e) {}
               System.out.println("Thread 1: Waiting for lock 2...");
           
           synchronized (Lock2) {
              System.out.println("Thread 1: Holding lock 1 & 2...");
           }
        }
     }
  }
  private static class ThreadDemo2 extends Thread {
     public void run() {
        synchronized (Lock2) {
           System.out.println("Thread 2: Holding lock 2...");
           
           try { 
               Thread.sleep(10); 
               } catch (InterruptedException e) {}
               System.out.println("Thread 2: Waiting for lock 1...");
           
           synchronized (Lock1) {
              System.out.println("Thread 2: Holding lock 1 & 2...");
           }
        }
     }
  } 
}

결과)
Thread 1: Holding lock 1...
Thread 2: Holding lock 2...
Thread 2: Waiting for lock 1...
Thread 1: Waiting for lock 2...
```

java.lang.Thread 클래스의 start() 메소드는 해당 스레드를 실행(execution)한다. 그러면 JVM은 실행하는 스레드의 run() 메소드를 자동 호출한다.   
run() 메소드 오버라이딩 작업을 하지 않았다는 것은 스레드가 **무슨 일을 해야 하는지** 정의하지 않았다는 뜻이다. 그래서 run() 메소드 오버라이딩 없이 start() 메소드만 실행하면 아무 결과도 출력되지 않는다.

start() 메소드가 실행되면 스택 메모리에 해당 스레드의 공간이 생성된다. 그리고 오버라이딩한 run() 메소드를 호출해서 해당 스레드의 스택 메모리 공간에 내용을 저장한다. 즉 start()가 실행되는 순간 해당 스레드의 독립적인 스택 메모리가 생성되고, 메모리 공간 안에 run()과 같은 메소드들이 저장되는 것이다.  

synchronized 키워드 사용의 이유는, 단 하나의 스레드만 실행할 수 있는 상태를 구현하기 위함이다.  

결과를 보면 제대로 실행되지 않고 무한정 대기 상태에 빠진다.  
위 코드에서 데드락이 발생한 이유는 다음과 같다.

- 상호 배제: Lock1, Lock2 객체에 두 스레드가 동시에 접근하고 있다.

- 점유와 대기: ThreadDemo1 에서는 Lock1을 가지고 있는 동시에 Lock2를 원하고, ThreadDemo2는 Lock2를 가지고 있는 동시에 Lock1을 원한다. 
- 환형대기: ThreadDemo1은 ThreadDemo2의 Lock2 객체를 대기하고, ThreadDemo2는 ThreadDemo1의 Lock1 객체를 대기하고 있다.

먼저 ThreadDemo1이 Lock1에 접근하고 이후 Lock2에 접근해야만 ThreadDemo1이 가지고 있던 Lock1을 반환할 수 있다. 그런데 ThreadDemo2가 Lock2를 점유하고 있다. 즉 ThreadDemo1, ThreadDemo2 두 개의 스레드가 하나의 자원에 접근하려고 하기 때문에 무한 대기 상황이 발생한다.

데드락을 해결하려면 데드락이 형성 조건을 충족하지 않도록 구현하면 된다.  
아래 코드는 환형대기 조건을 만족하지 않아서 데드락이 발생하지 않았다.

언뜻 이전 코드와 동일해 보이지만, ThreadDemo2의 코드 동작 순서가 Lock2 -> Lock1에서 Lock1 -> Lock2로 변경되었다.  

이전 코드는 Thread1이 Lock1을 점유하고 잠시 정지했다가 Lock2를 점유해야 한다. 그리고 ThreadDemo2는 Lock2를 점유하고 잠시 정지했다가 Lock1을 점유해야 한다. 즉 Thread1, 2 서로가 서로를 기다리고 있는 환형대기 상태이기 때문에 데드락이 발생했다. 그래서 Thread1, 2가 순차적으로 Lock1, Lock2를 점유할 수 있도록 순서를 조정하면 환형대기 문제를 방지할 수 있기 때문에 데드락이 발생하지 않는다.

```
public class DeadlockTest {
  public static Object Lock1 = new Object();
  public static Object Lock2 = new Object();
  
  public static void main(String args[]) {
     ThreadDemo1 T1 = new ThreadDemo1();
     ThreadDemo2 T2 = new ThreadDemo2();
     T1.start();
     T2.start();
  }
  
  private static class ThreadDemo1 extends Thread {
     public void run() {
        synchronized (Lock1) {
           System.out.println("Thread 1: Holding lock 1...");
           
           try { Thread.sleep(10); }
           catch (InterruptedException e) {}
           System.out.println("Thread 1: Waiting for lock 2...");
           
           synchronized (Lock2) {
              System.out.println("Thread 1: Holding lock 1 & 2...");
           }
        }
     }
  }
  private static class ThreadDemo2 extends Thread {
     public void run() {
        synchronized (Lock1) {
           System.out.println("Thread 2: Holding lock 2...");
           
           try { Thread.sleep(10); }
           catch (InterruptedException e) {}
           System.out.println("Thread 2: Waiting for lock 1...");
           
           synchronized (Lock2) {
              System.out.println("Thread 2: Holding lock 1 & 2...");
           }
        }
     }
  } 
}

결과)
Thread 1: Holding lock 1...
Thread 1: Waiting for lock 2...
Thread 1: Holding lock 1 & 2...
Thread 2: Holding lock 2...
Thread 2: Waiting for lock 1...
Thread 2: Holding lock 1 & 2...
```

# 참고
* [jenkov.com](https://jenkov.com/tutorials/java-concurrency/creating-and-starting-threads.html)
* [Java Virtual Machine Specification](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-2.html#jvms-2.5.2)
* [I'm a developer, not a coder](https://math-coding.tistory.com/175)