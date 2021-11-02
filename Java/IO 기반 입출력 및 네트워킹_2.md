# IO 기반 입출력 및 네트워킹_2

## 네트워크 기초

- 네트워크(network): 여러 대의 컴퓨터를 통신 회선으로 연결한 것.

### 서버와 클라이언트

- 서버(server): 서비스를 제공하는 프로그램. 연결을 수락하여 서비스를 제공(response).
- 클라이언트(client): 서비스를 받는 프로그램. 서비스를 받기 위해 연결을 요청(request).

인터넷에서 두 프로그램이 통신하기 위해 연결을 요청하고 수락하는 역할이 필요하다.

### IP 주소와 포트(Port)

- IP(Internet Protocol): 컴퓨터의 고유한 주소. 네트워크 어댑터(랜(Lan) 카드)마다 할당되는데, 한 개의 컴퓨터에 두 개의 네트워크 어댑터가 장착되어 있다면 두 개의 IP 주소를 할당할 수 있다. 

IP 주소는 부호 없는 0~255 사이의 정수이다. 연결할 상대방 컴퓨터의 IP 주소를 모른다면 프로그램들은 통신을 할 수 없다. 프로그램은 DNS(Domain Name System)를 통해 연결할 컴퓨터의 IP 주소를 찾는다. 자사 서비스를 제공하는 대부분의 서버는 도메인 이름을 가지고 있다. 일반적으로 DNS에 도메인 이름으로 IP를 등록해 놓는다.

웹 브라우저에서 사용자가 도메인 이름을 입력하면 DNS에서 검색하여 IP를 얻은 다음 해당 IP를 가진 서버로 연결한다.

한 대의 컴퓨터에는 다양한 서버 프로그램들이 실행될 수 있다. 예를 들면 Web Server, DBMS, FTP Server 등이 하나의 IP 주소를 갖는 컴퓨터에서 동시에 실행될 수 있다. 이 경우 클라이언트는 어떤 서버와 통신해야 할지 결정해야 한다. IP는 컴퓨터 네트워크 어댑터까지만 갈 수 있는 정보이기 때문에 컴퓨터 내에서 실행하는 서버를 선택하려면 추가적인 정보가 필요하다. 이 정보가 포트(port) 번호이다.

서버는 시작할 때 고정적인 포트 번호를 가지고 실행한다. 이것을 포트 바인딩(port binding)이라고 한다. 기본적으로 Web Server는 80번과 바인딩하고, FTP Server는 21번과 바인딩한다. 클라이언트가 Web Server에 연결하려면 80번으로 연결 요청을 해야 하고, FTP Server에 연결하려면 21번으로 연결 요청을 해야 한다.

클라이언트도  서버에서 보낸 정보를 받기 위해 포트 번호가 필요하다. 서버와 같이 고정적인 포트 번호가 아니라 운영체제가 자동으로 부여하는 동적 포트 번호를 사용한다. 포트 번호의 전체 범위는 0~65535이며, 세 가지 범위로 구분된다.

| 구분명 | 범위 | 설명 |
| --- | --- | --- |
| Well Know Port Numbers | 0~1023 | 국제인터넷주소관리기구(ICANN)가 특정 애플리케이션용으로 미리 예약한 포트 |
| Registered Port Numbers | 1024~49151 | 회사에서 등록해서 사용할 수 있는 포트 |
| Dynamic Or Private Port Numbers | 49152~65535 | 운영체제가 부여하는 동적 포트 또는 개인적인 목적으로 사용할 수 있는 포트 |

### InetAddress로 IP주소 얻기

자바는 IP 주소를 [java.net.InetAddress](https://docs.oracle.com/javase/7/docs/api/java/net/InetAddress.html) 객체로 표현한다. InetAddress는 로컬 컴퓨터의 IP주소 뿐만 아니라 도메인 이름을 DNS에서 검색한 후 IP 주소를 가져오는 기능을 제공한다. 로컬 컴퓨터의 IP 주소를 얻고 싶다면 getLocalHost() 메소드를 사용한다.

<pre>
<code>
InetAddress ia = InetAddress.getLocalHost();
</code>
</pre>

외부 컴퓨터의 도메인 이름을 알고 있다면 다음 두 개의 메소드를 사용하여 InetAddress 객체를 얻는다.

<pre>
<code>
InetAddress ia = InetAddress.getByName(String host);
InetAddress[] iaArr = InetAddress.getAllByName(String host);
</code>
</pre>

getByName() 메소드는 매개값으로 준 도메인 이름으로 DNS에서 단 하나의 IP 주소와 InetAddress를 생성한다.

연결 클라이언트가 많은 회사의 경우 서버의 과부화를 막기 위해 하나의 도메인 이름에 여러 개의 컴퓨터 IP를 등록해서 운영하는 경우가 있기 때문에 이 경우 getAllByName() 메소드를 사용하면 모든 IP 주소를 리턴한다.(getAllByName() 메소드는 해당 도메인 이름에 등록된 모든 IP 주소를 리턴한다.)

위 메소드를 통해 얻은 InetAddress 객체에서 IP 주소를 얻으려면 getHostAddress() 메소드를 호출한다. 리턴값은 문자열로된 IP 주소이다.

<pre>
<code>
String ip = InetAddress.getHostAddress();
</code>
</pre>

아래 코드는 로컬 컴퓨터의 IP와 구글 코리아(www.google.co.kr)의 IP 정보를 추출한다.

<pre>
<code>
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {
        try{
            InetAddress local = InetAddress.getLocalHost();
            System.out.println("My IP Address = " + local.getHostAddress());

            InetAddress[] iaArr = InetAddress.getAllByName("www.google.co.kr");
            for(InetAddress remote : iaArr){
                System.out.println("IP of www.google.co.kr = " + remote.getHostAddress());
            }
        }catch(UnknownHostException e){
            e.printStackTrace();
        }
    }
}

결과)
My IP Address = 192.168.0.XXX
IP of www.google.co.kr = 172.217.31.163
</code>
</pre>

### TCP 네트워킹

TCP(Transmission Control Protocol)는 연결 지향적 프로토콜이다. 연결 지향 프로토콜은 클라이언트와 서버가 연결된 상태에서 데이터를 주고받는 프로토콜이다. 클라이언트가 여녈 요청을 하고, 서버가 연결을 수락하면 통신 선로가 고정되고, 모든 데이터는 고정된 통신 선로를 통해서 순차적으로 전달된다. 

TCP는 데이터를 정확하고 안정적으로 전달하지만, 데이터를 보내기 전에 반드시 연결이 형성되어야 하고, 고정된 통신 선로가 최단선이 아닐 경우 상대적으로 UDP(User Datagram Protocol)보다 데이터 전송 속도가 느릴 수 있다. 

자바는 TCP 네트워킹을 위해 [ServerSocket 클래스](https://docs.oracle.com/javase/7/docs/api/java/net/ServerSocket.html)와 [Socket 클래스](https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html)를 제공한다.

### ServerSocket과 Socket의 용도

TCP 서버의 역할은 다음과 같다.

- 클라이언트가 연결 요청을 해오면 연결을 수락하는 것.
- 연결된 클라이언트와 통신하는 것.

자바에서는 두 역할별로 별도의 클래스를 제공한다. 클라이언트의 연결 요청을 기다리면서 연결 수락을 담당하는 것이 ServerSocket 클래스, 연결된 클라이언트와 통신을 담당하는 것이 Socket 클래스이다. 클라이언트가 연결 요청을 해오면 ServerSocket은 연결을 수락하고 통신용 Socket을 만든다.

서버는 클라이언트가 접속할 포트를 가지고 있어야 하는데, 이 포트를 바인딩(binding) 포트라고 한다. 서버는 고정된 포트 번호에 바인딩해서 실행하므로 ServerSocket을 생성할 때 포트 번호 하나를 지정해야 한다. 

- 서버가 실행되면 클라이언트는 서버의 IP 주소와 바인딩 포트 번호로 Socket을 생성해서 연결 요청을 할 수 있다. 
- ServerSocket은 클라이언트가 연결 요청을 해오명 accept() 메소드로 연결 수락을 하고 통신용 Socket을 생성한다. 
- 그리고 클라이언트와 서버는 각각의 Socket을 이용해서 데이터를 주고받게 된다.

### ServerSocket 생성과 연결 수락

서버를 개발하려면 우선 ServerSocket 객체를 얻어야 한다. 가장 간단한 방법은 생성자에 바인딩 포트를 대입하고 객체를 생성하는 것이다. 
다음은 5001번 포트에 바인딩하는 ServerSocket을 생성한다.

<pre>
<code>
ServerSocket serverSocket = new ServerSocket(5001);
</code>
</pre>

ServerSocket을 얻는 다른 방법은 디폴트 생성자로 객체를 생성하고 포트 바인딩을 위해 bind() 메소드를 호출하는 것이다. bind() 메소드의 매개값은 포트 정보를 가진 InetSocketAddress이다.

<pre>
<code>
ServerSocket serverSocket = new ServerSocket();
serverSocket.bind(new InetSocketAddress(5001));
</code>
</pre>

만약 서버 PC에 멀티 IP가 할당되어 있을 경우, 특정 IP로 접속할 때만 연결 수락을 하고 싶다면 다음과 같이 작성하고 'localhost' 대신 정확한 IP를 주면 된다.

<pre>
<code>
ServerSocket serverSocket = new ServerSocket();
serverSocket.bind(new InetSocketAddress("localhost", 5001));
</code>
</pre>

ServerSocket 생성 시 해당 포트가 이미 다른 프로그램에서 사용 중이라면 BindException이 발생한다. 이 경우 다른 포트로 바인딩 하거나, 다른 프로그램을 종료한다.

포트 바인딩 완료 후 ServerSocket은 클라이언트 연결 수락을 위해 accept() 메소드를 실행해야 한다. accept() 메소드는 클라이언트가 연결 요청하기 전까지 블로킹된다.(스레드가 대기 상태가 된다는 뜻) 따라서 UI를 생성하는 스레드나, 이벤트를 처리하는 스레드에서 accept() 메소드를 호출하지 않도록 한다. 블로킹이 되면 UI 갱신이나 이벤트 처리를 할 수 없기 때문이다. 

클라이언트가 연결 요청을 하면 accept()는 클라이언트와 통신할 Socket을 만들고 리턴한다. 이것이 연결 수락이다. 만약 accept()에서 블로킹되어 있을 때 ServerSocket을 닫기 위해 close() 메소드를 호출하면 SocketException이 발생한다. 그래서 예외 처리가 필요하다.

연결된 클라이언트의 IP와 포트 정보를 얻고 싶다면 Socket의 getRemoteSocketAddress() 메소드를 호출하여 SocketAddress를 얻으면 된다. 실제 리턴되는 것은 InetSocketAddress 객체이므로 다음과 같이 타입 변환할 수 있다.

<pre>
<code>
InetSocketAddress socketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
</code>
</pre>

InetSocketAddress에는 IP와 포트 정보를 리턴하는 다음과 같은 메소드들이 있다.

| 리턴 타입 | 메소드명(매개 변수) | 설명 |
| --- | --- | --- |
| String | getHostName() | 클라이언트 IP 리턴 |
| int | getPort() | 클라이언트 포트 번호 리턴 |
| String | toString() | "IP:포트번호" 형태의 문자열 리턴 |

더 이상 클라이언트 연결 수락이 필요 없다면 ServerSocket의 close() 메소드로 포트를 언바인딩시켜야 한다.(다른 프로그램에서 해당 포트를 재사용할 수 있도록 하기 위해서)

<pre>
<code>
serverSocket.close();
</code>
</pre>

아래 코드는 반복적으로 accept() 메소드를 호출해서 다중 클라이언트 연결을 수락하는 기본적은 코드이다.

<pre>
<code>
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientExample {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try{
            // ServerSocket 생성
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", 5001));
            while(true){
                System.out.println("Waiting to connect");

                // 클라이언트 연결 수락
                Socket socket = serverSocket.accept();
                InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
                System.out.println("Connection accepted = " + isa.getHostName());
            }
        }catch(Exception e){
            if(!serverSocket.isClosed()){
                try{
                    serverSocket.close();
                }catch(IOException e1){}
            }
        }
    }
}
</code>
</pre>

### Socket 생성과 연결 요청

클라이언트가 서버에 연결 요청을 하려면 java.net.Socket을 이용해야 한다. Socket 객체를 생성함과 동시에 연결 요청을 하려면 생성자의 매개값으로 서버의 IP 주소와 바인딩 포트 번호를 제공하면 된다. 

다음은 로컬 PC의 5001 포트에 연결 요청하는 코드이다.

<pre>
<code>
    try{
        Socket socket = new Socket("localhost", 5001); // 방법 1
        Socket socket = new Socket(new InetSocketAddress("localhost", 5001)); 방법 2
    }catch(UnknownHostException e){
        // IP 표기 방법이 잘못되었을 경우
    }catch(IOException e){
        // 해당 포트의 서버에 연결할 수 없는 경우
    }
</code>
</pre>

외부 서버에 접속하려면 localhost 대신 정확한 IP를 입력하면 된다. IP 대신 도메인 이름만 알고 있다면 도메인 이름을 IP 주소로 번역해야 하므로 InetSocketAddress 객체를 이용하는 방법을 사용해야 한다. Socket 생성과 동시에 연결 요청을 하지 않고 다음과 같이 기본 생성자로 Socket을 생성한 후, connect() 메소드로 연결 요청을 할 수도 있다.

<pre>
<code>
socket = new Socket();
socket.connect(new inetSocketAddress("localhost", 5001));
</code>
</pre>

Socket 생성자 및 connect() 메소드는 서버와 연결이 될 때까지 블로킹된다. 따라서 UI를 생성하는 스레드, 이벤트를 처리하는 스레드에서 Socket 생성자 및 connect()를 호출하지 않도록 한다. 블로킹이 되면 UI 갱신, 이벤트 처리를 할 수 없기 때문이다. 

연결을 끊는 close() 메소드도 IOException이 발생할 수 있기 때문에 예외 처리가 필요하다.

<pre>
<code>
try{
    socket.close();
}catch(IOException e){}
</code>
</pre>

아래 코드는 localhost 5001 포트로 연결을 요청하는 코드이다. connect() 메소드가 정상적으로 리턴하면 연결이 성공한 것이다.

<pre>
<code>
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerExample {
    public static void main(String[] args) {
        Socket socket = null;
        try{
            socket = new Socket();
            System.out.println("Waiting to connect");
            socket.connect(new InetSocketAddress("localhost", 5001));
            System.out.println("Connection accepted");
        }catch(Exception e){
            if(!socket.isClosed()){
                try{
                    socket.close();
                }catch(IOException e1){}
            }
        }
    }
}
</code>
</pre>

클라이언트, 서버간 상호 연결이 원활한지 확인하기 위해 앞서 작성한 ServerExample을 먼저 실행한 후 ClientExample을 실행한다.
결과는 다음과 같다.

<pre>
<code>
// ClientExample
Waiting to connect
Connection accepted

// ServerExample
Waiting to connect
Connection accepted = 127.0.0.1
Waiting to connect
</code>
</pre>

### Socket 데이터 통신

클라이언트가 연결 요청(connect())하고 서버가 연결 수락(accept())했다면 양쪽의 Socket 객체로부터 각각 입력 스트림(InputStream), 출력 스트림(OutputStream)을 얻을 수 있다.

Socket으로부터 InputStream, OutputStream은 다음과 같이 얻을 수 있다.

<pre>
<code>
// 입력 스트림(InputStream)
InputStream is = socket.getInputStream();

// 출력 스트림(OutputStream)
OutputStream os = socket.getOutputStream();
</code>
</pre>

상대방에게 데이터를 보내기 위해서는 보낼 데이터를 byte[] 배열로 생성하고, 이것을 매개값으로 OutputStream의 write() 메소드를 호출한다. 

다음은 UTF-8로 인코딩한 바이트 배열을 얻어내고, write() 메소드로 전송한다.

<pre>
<code>
String data = "Sending Data";
byte[] byteArr = data.getBytes("UTF-8");
OutputStream outputStream = socket.getOutputStream();
outputStream.write(byteArr);
outputStream.flush();
</code>
</pre>

상대방이 보낸 데이터를 받으려면 받은 데이터를 저장할 byte[] 배열을 생성하고, 이것을 매개값으로 해서 InputStream의 read() 메소드를 호출한다. read() 메소드는 읽은 데이터를 byte[] 배열에 저장하고 읽은 바이트 수를 리턴한다. 

다음은 데이터를 읽고 UTF-8로 디코딩한 문자열을 얻는다.

<pre>
<code>
byte[] byteArr = new byte[100];
InputStream inputStream = socket.getInputStream();
int readByteCount = inputStream.read(byteArr);
String data = new String(byteArr, 0, readByteCount, "UTF-8");
</code>
</pre>

아래 코드는 이전에 작성한 ClientExample 코드를 수정한 코드이다.
ServerExample과 연결 성공 후 클라이언트가 먼저 "Hello Server"를 서버로 보내고, 서버가 이 데이터를 받아 "Hello Client"를 클라이언트로 보내면 클라이언트가 이 데이터를 받는다.

<pre>
<code>
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientExample {
    public static void main(String[] args) {
        Socket socket = null;
        try{
            socket = new Socket();
            System.out.println("Waiting to connect");
            socket.connect(new InetSocketAddress("localhost", 5001));
            System.out.println("Connection accepted");

            byte[] bytes = null;
            String message = null;

            OutputStream os = socket.getOutputStream();
            message = "Hello Server";
            bytes = message.getBytes("UTF-8");
            os.write(bytes);
            os.flush();
            System.out.println("Data has been sent");

            InputStream is = socket.getInputStream();
            bytes = new byte[100];
            int readByteCount = is.read(bytes);
            message = new String(bytes, 0, readByteCount, "UTF-8");
            System.out.println("Data from Server has been arrived = " + message);

            os.close();
            is.close();
        }catch(Exception e){
            if(!socket.isClosed()){
                try{
                    socket.close();
                }catch(IOException e1){}
            }
        }
    }
}

package Java;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerExample {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try{
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", 5001));
            while(true){
                System.out.println("Waiting to connect");
                Socket socket = serverSocket.accept();
                InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
                System.out.println("Connection accepted = " + isa.getHostName());

                byte[] bytes = null;
                String message = null;

                InputStream is = socket.getInputStream();
                bytes = new byte[100];
                int readByteCount = is.read(bytes);
                message = new String(bytes, 0, readByteCount, "UTF-8");
                System.out.println("Data from Client has been arrived = " + message);

                OutputStream os = socket.getOutputStream();
                message = "Hello Client";
                bytes = message.getBytes("UTF-8");
                os.write(bytes);
                os.flush();
                System.out.println("Data has been sent");

                is.close();
                os.close();
                socket.close();
            }
        }catch(Exception e){
            if(!serverSocket.isClosed()){
                try{
                    serverSocket.close();
                }catch(IOException e1){}
            }
        }
    }
}
</code>
</pre>

먼저 ServerExample 실행 후 ClientExample을 실행한다.
결과는 아래와 같다.

<pre>
<code>
// ClientExample
Waiting to connect
Connection accepted
Data has been sent

// ServerExample
Waiting to connect
Connection accepted = 127.0.0.1
Data from Client has been arrived = Hello Server
Data has been sent
Waiting to connect
</code>
</pre>

### 스레드 병렬 처리

ServerSocket과 Socket은 동기(블로킹) 방식으로 구동된다. 서버 연결을 요청하거나 수락할 때는 해당 작업이 완료될 때까지 블로킹된다. 데이터를 보내거나 받을 때도 해당 작업이 완료될 때까지 블로킹된다. 

만약 서버를 실행시키는 main 스레드가 직접 입출력 작업을 담당하면 입출력이 완료될 때까지 다른 작업을 할 수 없는 상태가 된다. 서버 애플리케이션은 지속적으로 클라이언트의 연결 수락 기능을 수행해야 하는데, 입출력에서 블로킹되면 이 작업을 할 수 없게 된다. 따라서 accept(), connect(), read(), write()는 별도의 작업 스레드를 생성해서 병렬적으로 처리하는 것이 좋다.

수천 개의 클라이언트가 동시에 연결되면 서버 과부하로 성능이 저하될 수 있기 때문에 스레드풀(Executor Service)을 사용하는 것이 좋다.
스페드풀은 스레드 수를 제한해서 사용하기 때문에 갑작스러운 클라이언트의 폭증은 작업 큐의 작업량만 증가시킬 뿐 스레드 수는 변함이 없기 때문에 서버 성능은 완만히 저하된다.

## UDP 네트워킹

UDP(User Datagram Protocol)는 비연결 지향적 프로토콜이다. 비연결 지향적이란 데이터를 주고받을 때 연결 절차를 거치지 않고 발신자가 일방적으로 데이터를 발신하는 방식이다. 연결 과정이 없기 때문에 TCP 보다는 빠른 전송이 가능하지만 데이터 전달의 신뢰성은 떨어진다. 

UDP는 발신자가 데이터 패킷을 순차적으로 보내더라도 이 패킷들은 서로 다른 통신 선로를 통해 전달될 수 있따. 먼저 보낸 패킷이 느린 선로를 통해 전송될 경우 나중에 보낸 패킷보다 늦게 도착할 수 있다. 또한 일부 패킷은 잘못된 선로로 전송되어 잃어버릴 수도 있다.

일반적으로 데이터 전달의 신뢰성보다 속도가 중요한 프로그램에서 UDP를 사용하고, 데이터 전달의 신뢰성이 중요한 경우 TCP를 사용한다.
자바는 UDP 프로그래밍을 위해 [DatagramSocket 클래스](https://docs.oracle.com/javase/7/docs/api/java/net/DatagramSocket.html)와 [DatagramPacket 클래스](https://docs.oracle.com/javase/7/docs/api/java/net/DatagramPacket.html)를 제공한다. 

- DatagramSocket : 발신점과 수신점에 해당하는 클래스.
- DatagramPacket : 주고받는 패킷 클래스.

### 발신자 구현

우선 DatagramSocket 객체를 생성한다.

<pre>
<code>
DatagramSocket datagramSocket = new DatagramSocket();
</code>
</pre>

보내고자 하는 데이터를 byte[] 배열로 생성한다. 문자열인 경우 UTF-8로 인코딩해서 byte[] 배열을 얻는다.

<pre>
<code>
byte[] byteArr = data.getBytes("UTF-8");
</code>
</pre>

이제 데이터와 수신자 정보를 담고 있는 DatagramPacket을 생성한다. 
생성자의 첫 번째 매개값은 보낼 데이터인 byte[] 배열이고, 두 번째 매개값은 byte[] 배열에서 보내고자 하는 항목 수이다. 전체 항목을 보내려면 length 값으로 대입한다. 세 번째 매개값은 수신자 IP와 포트 정보를 가지고 있는 SocketAddress이다. SocketAddress는 추상 클래스이므로 하위 클래스인 InetSocketAddress 객체를 생성해서 대입하면 된다. 

아래 코드는 로컬 PC인 5001번을 수신자로 하는 DatagramPacket을 생성하는 코드이다.

<pre>
<code>
byte[] byteArr = data.getBytes("UTF-8");
DatagramPacket packet = new DatagramPacket(
    byteArr, byteArr.length, new InetSocketAddress("localhost", 5001)
);
</code>
</pre>

DatagramPacket 생성 후 이것을 매개값으로 해서 DatagramSocket의 send() 메소드를 호출하면 수신자에게 데이터가 전달된다.

<pre>
<code>
datagramSocket.send(packet);
</code>
</pre>

더 이상 보낼 데이터가 없을 경우 DatagramSocket을 닫기 위해 close() 메소드를 호출한다.

<pre>
<code>
datagramSocket.close();
</code>
</pre>

아래 코드는 발신자 프로그램의 전체 코드이다.

<pre>
<code>
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UdpSendExample {
    public static void main(String[] args) throws Exception{
        DatagramSocket datagramSocket = new DatagramSocket();

        System.out.println("발신 시작");

        for(int i = 1; i < 3; i++){
            String data = "message" + i;
            byte[] byteArr = data.getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(byteArr, byteArr.length, new InetSocketAddress("loclahost", 5001));

            datagramSocket.send(packet);
            System.out.println("바이트 수 = " + byteArr.length + "bytes");
        }

        System.out.println("발신 종료");

        datagramSocket.close();        
    }
}
</code>
</pre>

### 수신자 구현

수신자로 사용할 DatagramSocket 객체는 바인딩할 포트 번호를 매개값으로 저장하고 생성해야 한다.

<pre>
<code>
DatagramSocket datagramSocket = new DatagramSocket(5001);
</code>
</pre>

DatagramSocket 생성 후 receive() 메소드로 패킷을 읽을 준비를 한다. 해당 메소드는 패킷을 받을 때까지 블로킹 되고, 패킷이 도착하면 매개값으로 주어진 DatagrramPacket에 패킷 내용을 저장한다.

<pre>
<code>
datagramSocket.receive(datagramPacket);
</code>
</pre>

이제 패킷의 내용을 저장할 DatagramPacket 객체를 생성한다.
첫 번째 매개값은 읽은 패킷 데이터를 저장할 바이트 배열이다.
두 번째 매개값은 읽을 수 있는 최대 바이트 수로 첫 번째 바이트 배열의 크기와 같거나 작아야 한다. 일반적으로 첫 번째 바이트 배열의 크기를 준다.

<pre>
<code>
DatagramPacket datagramPacket = new DatagramPacket(new byte[100], 100);
</code>
</pre>

receive() 메소드로 패킷을 읽었다면 DatagramPacket의 getData() 메소드로 데이터가 저장된 바이트 배열을 얻어낼 수 있다. 그리고 getLength()를 호출해서 읽은 바이트 수를 얻을 수 있다. 받은 데이터가 인코딩된 문자열이라면 디코딩해서 문자열을 얻으면 된다.

<pre>
<code>
String data = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
</code>
</pre>

수신자가 패킷을 받은 후 발신자에게 응답 패킷을 보내고 싶다면 발신자의 IP와 포트를 알아야 한다. DatagramPacket의 getSocketAddress()를 호출하면 발신자의 SocketAddress 객체를 얻어 낼수 있어 발신자에게 응답 패킷을 보낼 때 send() 메소드에서 이용할 수 있다.

<pre>
<code>
SocketAddress socketAddress = packet.getSocketAddress();
</code>
</pre>

수신자는 항상 데이터를 받을 준비를 해야 한다. 따라서 작업 스레드를 생성해서 receive() 메소드를 반복적으로 호출해야 한다. 작업 스레드를 종료하려면 receive() 메소드가 블로킹되어 있는 상태에서 DatagramSocket의 close()를 호출한다. 이 경우 receive()에서 SocketException이 발생하고, 예외 처리 코드에서 작업 스레드를 종료시키면 된다.

아래 코드는 수신자 프로그램의 전체 코드이다. 실행 후 10초가 지나면 수신자를 종료한다.

<pre>
<code>
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpReceiveExample extends Thread{
    public static void main(String[] args) throws Exception{
        DatagramSocket datagramSocket = new DatagramSocket(5001);

        Thread thread = new Thread(){
            @Override
            public void run(){
                System.out.println("수신 시작");
                try{
                    while(true){
                        DatagramPacket packet = new DatagramPacket(new byte[100], 100);
                        datagramSocket.receive(packet);

                        String data = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
                        System.out.println("message " + packet.getSocketAddress() + " = " + data);
                    }
                }catch(Exception e){
                    System.out.println("수신 종료");
                }
            }
        };
        thread.start();

        Thread.sleep(10000);
        datagramSocket.close();
    }
}
</code>
</pre>

# 출처
* [이것이 자바다](http://www.kyobobook.co.kr/product/detailViewKor.laf?ejkGb=KOR&mallGb=KOR&barcode=9788968481475&orderClick=LAG&Kc=)