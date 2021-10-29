# IO 기반 입출력 및 네트워킹

자바에서 데이터는 스트림(Stream)을 통해 입출력된다. 스트림은 단일 방향으로 연속적으로 흘러가는 것을 말하는데, 물이 높은 곳에서 낮은 곳으로 흐르듯이 데이터는 출발지에서 나와 도착지로 들어간다는 개념이다.

## 입력 스트림과 출력 스트림

프로그램이 출발지 또는 도착지냐에 따라서 스트림의 종류가 결정된다. 프로그램이 데이터를 입력받을 때에는 입력 스트림(InputStream) 이라고 부른다. 입력 스트림의 출발지는 키보드, 파일, 니트워크상의 프로그램이 될 수 있고, 출력 스트림의 도착지는 모니터, 파일, 네트워크상의 프로그램이 될 수 있다.

**프로그램을 기준으로 데이터가 들어오면 입력 스트림, 데이터가 나가면 출력 스트림이다.**
프로그램이 네트워크상의 다른 프로그램과 데이터 교환을 하기 위해서는 양쪽 모두 입력 스트림과 출력 스트림이 따로 필요하다. 스트림의 틀성이 단방향이므로 하나의 스트림으로 입력과 출력을 모두 할 수 없기 때문이다.

자바의 기본 데이터 입출력(IO) API는 [java.io 패키지](https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/io/package-summary.html)에서 제공한다.

다음은 주요 입출력 스트림 클래스이다.

![자바 주요 입출력 클래스](./image/java_chapter17_1.png)

출력 : https://velog.io/@ljs0429777/Java-IO

스트림 클래스는 크게 **바이트(byte) 기반 스트림, 문자(character) 기반 스트림** 두 종류로 구분된다.
바이트 기반 스트림은 그림, 멀티미디어, 문자 등 모든 종류의 데이터를 받고 보낼 수 있고, 문자 기반 스트림은 오로지 문자만 받고 보내는 작업에 특화되어 있다.
바이트 기반 스트림, 문자 기반 스트림은 최상위 클래스에 따라서 사용 클래스가 다르다.

InputStream은 바이트 기반 입력 스트림의 최상위 클래스이다. OutputStream은 바이트 기반 출력 스트림의 최상위 클래스이다. 이 클래스들을 각각 상속받는 하위 클래스는 접미사로 InputStream 또는 OutputStream이 붙는다. 

Reader는 문자 기반 입력 스트림의 최상위 클래스이고, Writer는 문자 기반 출력 스트림의 최상위 클래스이다. 이 클래스들을 각각 상속받는 하위 클래스는 접미사로 Reader 또는 Writer가 붙는다. 예를 들어 그림, 멀티미디어, 텍스트 등의 파일을 바이트 단위로 읽어들일 때에는 FileInputStream을 사용하고, 바이트 단위로 저장할 때에는 FileOutputStream을 사용한다. 텍스트 파일의 경우 문자 단위로 읽어들일 때에는 FileReader를 사용, 문자 단위로 저장할 때에는 FileWriter를 사용한다.

## InputStream

[InputStream](https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/io/InputStream.html)은 바이트 기반 입력 스트림의 최상위 클래스로 추상 클래스이다. 모든 바이트 기반 **입력** 스트림은 이 클래스를 상속받아서 만들어진다.

InputStream 클래스의 주요 메소드를 알아보자.

### read() 메소드

read() 메소드는 입력 스트림으로부터 1바이트를 읽고 4바이트 int 타입으로 리턴한다. 따라서 리턴된 4바이트 중 끝의 1바이트에만 데이터가 들어 있다. 예를 들어 입력 스트림에서 5개의 바이트가 들어온다면 read() 메소드로 1바이트씩 5번 읽을 수 있다.

더 이상 입력 스트림으로부터 바이트를 읽을 수 없다면 read() 메소드는 -1을 리턴한다. 이것을 이용하면 읽을 수 있는 마지막 바이트까지 루프를 돌며 한 바이트씩 읽을 수 있다.

<pre>
<code>
InputStream is = new FileInputStream("D:/inputdata.txt");
int readByte;
while((readByte=is.read()) != -1) { ... }
</code>
</pre>

### read(byte[] b) 메소드

read(byte[] b) 메소드는 입력 스트림으로부터 매개값으로 주어진 바이트 배열의 길이만큼 바이트를 읽고 배열에 저장한다. 실제로 읽은 바이트 수가 배열의 길이보다 작을 경우 읽은 수만큼 리턴한다. 

입력 스트림으로부터 더 이상 읽을 수 없다면 -1을 리턴한다. 이것을 이용하면 읽을 수 있는 마지막 바이트까지 루프를 돌며 읽을 수 있다.

<pre>
<code>
InputStream is = new FileInputStream("D:/inputdata.txt");
int readByteNo;
byte[] readBytes = new byte[100];
while((readByteNo=is.read(readBytes)) != -1) { ... }
</code>
</pre>

### read(byte[] b, int off, int len)

read(byte[] b, int off, int len) 메소드는 입력 스트림으로부터 len개의 바이트만큼 읽고, 매개값으로 주어진 바이트 배열 b[off]부터 len개까지 저장한다. 그리고 읽은 바이트 수인 len개를 리턴한다. 실제로 읽은 바이트 수가 len개보다 작을 경우 읽은 수만큼 리턴한다. 

입력 스트림으로부터 더 이상 읽을 수 없다면 -1을 리턴한다. read(byte[] b)와의 차이점은 한 번에 읽어들이는 바이트 수를 len 매개값으로 조절할 수 있고, 배열에서 저장이 시작되는 인덱스를 지정할 수 있다는 것이다.

### close() 메소드

InputStream을 더 이상 사용하지 않을 경우 close() 메소드를 호출하여 InputStream에서 사용했던 시스템 자원을 풀어준다.

## OutputStream

[OutputStream](https://docs.oracle.com/javase/7/docs/api/java/io/OutputStream.html)은 바이트 기반 출력 스트림의 최상위 클래스로 추상 클래스이다. 모든 바이트 기반 **출력** 스트림은 이 클래스를 상속받아서 만들어진다.

OutputStream 클래스의 주요 메소드를 알아보자.

### write(int b)

매개 변수로 주어진 int 값에서 끝에 있는 1바이트만 출력 스트림으로 보낸다. 매개 변수가 int 타입이므로 4바이트 모두를 보내는 것으로 오해할 수 있으니 주의하자.

<pre>
<code>
OutputStream os = new FileOutputStream("D:/outputdata.txt");
byte[] data = "ABC".getBytes();
for(int i = 0; i < data.length; i++){
    os.write(data[i]); // "A", "B", "C" 를 하나씩 출력
}
</code>
</pre>

### write(byte[] b)

write(byte[] b)는 매개값으로 주어진 바이트 배열의 모든 바이트를 출력 스트림으로 만든다.

<pre>
<code>
OutputStream os = new FileOutputStream("D:/outputdata.txt");
byte[] data = "ABC".getBytes();
os.write(data[i]); // "ABC" 모두 출력
</code>
</pre>

### write(byte[] b, int off, int len)

write(byte[] b, int off, int len)은 b[off]부터 len개의 바이트를 출력 스트림으로 보낸다.

<pre>
<code>
OutputStream os = new FileOutputStream("D:/outputdata.txt");
byte[] data = "ABC".getBytes();
os.write(data, 1, 2); // "BC" 만 출력
</code>
</pre>

### flush와 close() 메소드

출력 스트림은 내부에 작은 버퍼(buffer)가 있다. 데이터가 출력되기 전에 버퍼에 쌓여있다가 순서대로 출력된다.

- flush() : 버퍼에 잔류하고 있는 데이터를 모두 출력시키고 버퍼를 비우는 역할을 한다. 프로그램에서 더 이상 출력할 데이터가 없다면 flush() 메소드를 마지막으로 호출하여 버퍼에 잔류하는 모든 데이터가 출력되도록 해야 한다.
- close() : OutputStream을 더 이상 사용하지 않을 경우 close() 메소드로 OutputStream에서 사용했던 시스템 자원을 풀어준다.

<pre>
<code>
OutputStream os = new FileOutputStream("D:/outputdata.txt");
byte[] data = "ABC".getBytes();
os.write(data);
os.flush();
os.close();
</code>
</pre>

## Reader

[Reader](https://docs.oracle.com/javase/7/docs/api/java/io/Reader.html)는 문자 기반 입력 스트림의 최상위 클래스로 추상 클래스이다. 모든 문자 기반 **입력** 스트림은 이 클래스를 상속받아서 만들어진다. 

### read() 메소드

입력 스트림으로부터 한 개의 문자(2바이트)를 읽고 4바이트 int 타입으로 리턴한다. 따라서 리터된 4바이트 중 끝에 있는 2바이트에 문자 데이터가 들어 있다. 

read() 메소드가 리턴한 int 값을 char 타입으로 변환하면 읽은 문자를 얻을 수 있다.

> char charData = (char) read();

더 이상 입력 스트림으로부터 문자를 읽을 수 없다면 read() 메소드는 -1을 리턴한다. 이것을 이용하면 읽을 수 있는 마지막 문자까지 루프를 돌며 한 문자씩 읽을 수 있다.

<pre>
<code>
Reader reader = new FileReader("D:/data.txt");
int readData;
while((readData = reader.read()) != -1){
    char charData = (char) readData;
}
</code>
</pre>

### read(char[] cbuf)

read(char[] cbuf) 메소드는 입력 스트림으로부터 매개값으로 주어진 문자 배열의 길이만큼 문자를 읽고 배열에 저장한다. 그리고 읽은 문자 수를 리턴한다. 실제 읽은 문자 수가 배열의 길이보다 작을 경우 읽은 수만큼만 리턴한다. 

더 이상 입력 스트림으로부터 문자를 읽을 수 없다면 -1을 리턴한다. 이것을 이용하면 읽을 수 있는 마지막 문자까지 루프를 돌며 문자를 읽을 수 있다.

<pre>
<code>
Reader reader = new FileReader("D:/data.txt");
int readData;
char[] cbuf = new char[2];
while((readData = reader.read()) != -1) { ... }
</code>
</pre>

입력 스트림으로부터 100개의 문자가 들어온다면 read() 메소드는 100번을 루핑해야 읽어야 한다. 하지만 read(char[] cbuf) 메소드는 한 번 읽을 때 주어진 배열 길이만큼 읽기 때문에 루핑 횟수가 현저히 줄어든다. 그러므로 많은 양의 문자를 읽을 때는 read(char[] cbuf) 메소드를 사용하는 것이 좋다.

### read(char[] cbuf, int off, int len)

read(char[] cbuf, int off, int len) 메소드는 입력 스트림으로부터 len 개의 문자만큼 읽고 매개값으로 주어진 문자 배열 cbuf[off]부터 len개까지 저장한다. 그리고 읽은 문자 수인 len개를 리턴한다. 실제로 읽은 문자 수가 len개보다 작을 경우 읽은 수만큼 리턴한다. 

더 이상 입력 스트림으로부터 문자를 읽을 수 없다면 -1을 리턴한다. read(char[] cbuf)와의 차이점은 한 번에 읽어들이는 문자 수를 len 매개값으로 조절할 수 있고, 배열에서 저장이 시작되는 인덱스를 지정할 수 있다는 것이다. 만약 off를 0, len을 배열의 길이로 준다면 read(char[] cbuf)와 동일하다.

### close()

Reader를 더 이상 사용하지 않을 경우 close() 메소드를 호출해서 Reader에서 사용했던 시스템 자원을 풀어준다.

# 출처
* [이것이 자바다](http://www.kyobobook.co.kr/product/detailViewKor.laf?ejkGb=KOR&mallGb=KOR&barcode=9788968481475&orderClick=LAG&Kc=)