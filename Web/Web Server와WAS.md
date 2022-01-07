# Web Server와 Web Application Server

## Web Server

웹에서 기능을 수행하는 프로그램. HTTP 프로토콜을 기반으로 웹 클라이언트(브라우저)로부터 요청을 받아 정적인 컨텐츠(.html, .js, .css, .jpeg 등)를 제공한다.  
웹 서버의 역할은 클라이언트로부터 요청을 받는 것, 처리된(서비스) 결과를 클라이언트로 응답하는 것으로 나뉘며, 구체적으로 다음 기능들을 처리한다.  
최근에는 Web Server에서도 내부 애플리케이션을 동작시킬 수 있는 Container를 내장하고 있다.  

- HTTP 요청에 따라 서버에 저장되어 있는 적절한 웹페이즈를 클라이언트에게 전달.
- 요청 파일이 없거나 문제가 발생하면 정해진 코드 값으로 응답한다.
- 클라이언트로부터의 요청에 대한 기본 사용자 인증을 처리한다.
- 서버 프로그램에 대한 요청을 Web Application Server에 수행시키고 그 결과를 응답한다.

*Web Server 예시*
- Apache
- IIS
- nginx

## WAS(Web Application Server)

인터넷 상에서 HTTP를 통해 사용자 컴퓨터나 장치에 애플리케이션을 수행해 주는 미들웨어.  
서버단에서 애플리케이션이 동작할 수 있도록 지원한다. 일반적으로 Container라는 용어로 쓰인다. 초창기에는 CGI, 이후 Servlet, ASP, JSP, PHP 등의 프로그램으로 사용되고 있다.  

*Servlet, ASP, JSP, PHP 등의 웹 언어로 작성된 웹 애플리케이션을 서버단에서 실행 후 실행 결과값을 사용자에게 넘겨주고, 우리가 가진 브라우저가 결과를 해석해서 화면에 표시하는 순서로 동작한다.*  

- 프로그램 실행 환경과 DB 접속 기능 제공
- 여러 개의 트랜잭션을 관리
- 다양한 기능을 수행하는 Business Logic 수행

*WAS 예시*
- WebLogic
- WebSphere
- Jeus
- JBoss
- Tomcat

**(요약)**  
WAS는 웹 서버 기능과 컨테이너 기능으로 구성된다.(**Web Server + Web Container**)  
Web Container 또는 Servlet Container라고도 불린다.(WAS는 Servlet, JSP 구동 환경을 제공)

## Container

Servlet, JSP와 같은 Web Server Application들은 동적 컨텐츠를 생성한느 웹 컴포넌트이다. 이러한 웹 컴포넌트를 저장하는 저장소 역할, 메모리 로딩, 객체 생성 및 초기화 등 서블릿의 생명주기를 관리하고 JSP를 서블릿으로 변환하는 기능을 수행하는 프로그램이 Container이다.  

# 출처
* [Web Server와 Web Application Server](https://blog.daum.net/ticcanis/256)
* [웹 서버와 WAS(Web Application Server)의 정의 ](https://blog.daum.net/hopcount/8658571)