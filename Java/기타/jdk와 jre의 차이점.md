# jdk와 jre의 차이점

## JDK

JDK(Java Development Kit)는 자바 애플리케이션이나 기타 응용 프로그램 개발에 필요한 개발 환경이다.  
개발자는 사용 환경(Window, Mac, Unix 등)에 따라 요구사항에 맞게 JDK를 설치하여 개발을 하면 된다.  

JDK는 JRE(Java Runtime Environment) 그리고 interpreter, compiler, archiver, debugger와 같은 다양한 개발 도구를 지원한다.  
JDK는 개발 작업만 수행할 수 있는 것이 아니다. JDK가 있으면 개발된 프로그램 실행 또한 가능하다.   

## JRE

JRE는 JVM(Java Virtual Machine)을 구현한 것으로 자바 프로그램을 실행할 수 있는 환경을 제공하는 것에 중점을 두고 있다.  
JRE는 compiler, debugger와 같은 개발 도구를 포함하지 않는다.  

만약 자바로 개발된 특정 프로그램의 실행에만 목적으로 두고 있다면 JRE 설치만으로 충분하다.  

생각해보니 JDK와 JRE의 차이점은 그 이름에 이미 답이 있다. 자바 개발 도구(JDK) 그리고 자바 실행 환경(JRE).  

## 요약

- JDK는 자바 개발 도구이며, 자바로 개발을 하려면 JDK를 설치해야 한다. 개발된 프로그램 실행 또한 가능하다.

- JRE는 자바 실행 환경이며, 자바로 개발된 프로그램 실행이 주요 목적이다.

# 참고
* [GeeksforGeeks](https://www.geeksforgeeks.org/difference-between-jdk-and-jre-in-java/)