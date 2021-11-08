# NIO 기반 입출력 및 네트워킹

IO와 NIO의 차이점은 다음과 같다.

| 구분 | IO | NIO |
| --- | --- | --- |
| 입출력 방식 | 스트림 방식 | 채널 방식 |
| 버퍼 방식 | non-buffer | buffer |
| 비동기 방식 | 지원 안 함 | 지원 |
| 블로킹 / non-blocking 방식 | 블로킹 방식만 지원 | 블로킹 / non-blocking 방식 모두 지원 |

NIO는 불특정 다수의 클라이언트 연결 또는 멀티 파일들을 non-blocking이나 비동기로 처리할 수 있기 때문에 과도한 스레드 생성을 피하고 스레드를 효과적으로 재사용한다는 장점이 있다. 또한 운영체제의 버퍼를 이용한 입출력이 가능하기 때문에 입출력 성능 향상 효과도 있다.
NIO는 연결 클라이언트 수가 많고, 하나의 입출력 처리 작업이 오래 걸리지 않는 경우에 사용하는 것이 좋다.

반면 연결 클라이언트 수가 적고, 전송되는 데이터가 대용량이면서 순차적으로 처리해야 하는 경우 IO로 서버를 구현하는 것이 좋다.

## 파일과 디렉토리

NIO는 파일의 속성 정보를 제공해주는 클래스와 인터페이스를 [java.nio.file](https://docs.oracle.com/javase/8/docs/api/java/nio/file/package-summary.html), [java.nio.file.attribute](https://docs.oracle.com/javase/8/docs/api/java/nio/file/attribute/package-summary.html) 패키지에서 제공한다.

### 경로 정의(Path)

[java.nio.file.Path 인터페이스](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Path.html)는 IO의 java.io.File 클래스와 대응된다. NIO의 API에서 파일의 경로를 지정하기 위해 사용한다.

Path 구현 객체를 얻으려면 get() 메소드를 호출하고, 매개값으로 파일의 경로를 지정한다. 매개값은 문자열 또는 URI 객체로 지정할 있다.

<pre>
<code>
Path path = Paths.get("D:/Hello/test1.txt");
Path path = Paths.get(URI uri);
</code>
</pre>

문자열로 파일 경로를 지정할 때 전체 경로를 한꺼번에 지정해도 되고, 상위/하위 디렉토리로 구분해서 지정해도 된다.

<pre>
<code>
Path path = Paths.get("D:/Hello", "test1.txt");
</code>
</pre>

파일 경로는 절대 경로와 상대 경로 모두 사용할 수 있다.
만약 현재 경로가 "D:\Hello"일 경우 "D:\Hello\Test\test1.txt"는 다음과 같이 지정이 가능하다.

<pre>
<code>
Path path = Paths.get("Test/test1.txt");
Path path = Paths.get("./Test/test1.txt");
</code>
</pre>

만약 현재 경로가 "D:\Hello\Test1"이라면 "D:\Hello\Test2\test2.txt"는 다음과 같이 지정이 가능하다.

<pre>
<code>
Path path = Paths.get("../Test2/test2.txt");
</code>
</pre>

Path 인터페이스에는 파일 경로에서 얻을 수 있는 여러 정보를 제공하는 메소드가 있다. 우선 파일에 대한 Path 객체를 얻고, 파일명, 부모 디렉토리명, 중첩 경로 수, 경로상의 모든 디렉토리를 출력하는 코드를 작성해보자.

<pre>
<code>
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) throws Exception{
        Path path = Paths.get("D:/Hello/test1.txt");
        System.out.println(path.getFileName());
        System.out.println(path.getParent().getFileName());
        System.out.println(path.getNameCount());

        System.out.println();

        for(int i = 0; i < path.getNameCount(); i++){
            System.out.println(path.getName(i));
        }

        System.out.println();

        Iterator<Path> iterator = path.iterator();
        while(iterator.hasNext()){
            Path temp = iterator.next();
            System.out.println(temp.getFileName());
        }
    }
}

결과)
test1.txt
Hello
2

Hello
test1.txt

Hello
test1.txt
</code>
</pre>

### 파일 시스템 정보(FileSystem)

운영체제의 파일 시스템은 [FileSystem 인터페이스](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/nio/file/FileSystem.html)를 통해 접근할 수 있다. 구현 객체는 정적 메소드인 getDefault()로 얻을 수 있다.

<pre>
<code>
FileSystem fileSystem = FileSystems.getDefault();
</code>
</pre>

[FileStore](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/nio/file/FileStore.html)는 드라이버를 표현한 객체다.

아래 코드는 파일 시스템 정보를 얻는다.

<pre>
<code>
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception{
        FileSystem fileSystem = FileSystems.getDefault();
        for(FileStore store : fileSystem.getFileStores()){
            System.out.println(store.name()); // 드라이버명
            System.out.println(store.type()); // 파일 시스템 종류
            System.out.println(store.getTotalSpace()); // 드라이버 전체 크기
            System.out.println(store.getUnallocatedSpace()); // 할당되지 않은 공간 크기
            System.out.println(store.getUsableSpace()); // 사용 가능한 공간 크기
        }

        System.out.println();
        System.out.println("파일 구분자 = " + fileSystem.getSeparator());
        System.out.println();

        for(Path path : fileSystem.getRootDirectories()){ // 루트 디렉토리 정보를 가진 Path 객체들을 리턴
            System.out.println(path.toString());
        }

    }
}

결과)

NTFS
999240384512
762538573824
762538573824

NTFS
127403028480
126705188864
126705188864

파일 구분자 = \

C:\
D:\
E:\
F:\
G:\
</code>
</pre>

### 파일 속성 읽기 및 파일, 일게토리 생성/삭제

[java.nio.file.Files 클래스](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/nio/file/Files.html)는 파일과 디렉토리의 생성 및 삭제 그리고 이들의 속성을 읽는 메소드를 제공한다. 속성이란 파일이나 디렉토리가 숨김인지, 디렉토리인지, 크기가 어떻게 되는지, 소유자가 누구인지에 대한 정보이다.

### WatchService

[WatchService](https://docs.oracle.com/javase/7/docs/api/java/nio/file/WatchService.html)는 디렉토리 내부에서 파일 생성, 삭제, 수정 등의 내용 변화를 감시하는데 사용된다. WatchService를 생성하려면 FileSystem의 newWatchService() 메소드를 호출한다.

<pre>
<code>
WatchService watchService = FileSystems.getDefault().newWatchService();
</code>
</pre>

WatchService 생성 후 감시가 필요한 디렉토리의 Path 객체에서 register() 메소드로 WatchService를 등록한다. 어떤 변화(생성, 삭제, 수정)를 감시할 것인지를 StandardWatchEventKinds 상수로 지정할 수 있다.

<pre>
<code>
path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_MODIFY,
                            StandardWatchEventKinds.ENTRY_DELETE);
</code>
</pre>

디렉토리(Path)에 WatchService를 등록하면 디렉토리 내부에서 변경이 발생될 경우 WatchEvent가 발생, WatchService는 해당 이벤트 정보를 가진 WatchKey를 생성하여 Queue에 넣는다. 프로그램은 무한 루프를 돌면서 WatchService의 take() 메소드를 호출하여 WatchKey가 Queue에 들어올 때까지 대기하고 있다가 WatchKey가 큐에 들어오면 WatchKey를 얻어 처리하면 된다.

<pre>
<code>
while(true){
    WatchKey watchKey = watchService.take(); // Queue에 WatchKey가 들어올 때까지 대기
}
</code>
</pre>

WatchKey를 얻고나서 pollEvents() 메소드를 호출하여 WatchEvent 리스트를 얻는다. 여러개의 파일이 동시에 변경될 수 있기 때문에 List<WatchEvent<?>>로 리턴한다. WatchEvent는 파일당 하나씩 발생한다.

<pre>
<code>
List<WatchEvent<?>> list = watchKey.pollEvents();
</code>
</pre>

프로그램은 WatchEvent 리스트에서 WatchEvent를 하나씩 꺼내서 이벤트의 종류와 Path 객체를 얻어낸 다음 목적에 맞게 처리한다.

<pre>
<code>
for(WatchEvent watchEvent : list){
    // 이벤트 종류 얻기
    Kind kind = watchEvent.kind();

    // 감지된 Path 얻기
    Path path = (Path)watchEvent.context();

    // 이벤트 종류별로 처리
    if(kind == StandardWatchEventKinds.ENTRY_CREATE){
        // 생성 시 처리 코드
    }
    else if(kind == StandardWatchEventKinds.ENTRY_MODIFY){
        // 수정 시 처리 코드
    }
    else if(kind == StandardWatchEventKinds.ENTRY_DELETE){
        // 삭제 시 처리 코드
    }
    else if(kind == StandardWatchEventKinds.OVERFLOW){
        
    }
}
</code>
</pre>

OVERFLOW 이벤트는 운영체제에서 이벤트가 소실 또느 버려진 경우에 발생하므로 별도의 처리 코드가 필요 없다. 따라서 CREATE, MODIFY, DELETE 이벤트만 처리하면 된다. 새로운 WatchEvent가 발생하면 Queue에 다시 들어가기 때문에 한 번 사용된 WatchKey는 reset() 메소드로 초기화해야 한다. 

초기화에 성공하면 reset() 메소드는 true를 리턴하지만, 감시하는 디렉토리가 삭제되었거나 키가 더 이상 유효하지 않을 경우 false를 리턴한다. WatchKey가 더 이상 유효하지 않게 되면 무한 루프를 빠져나와 WatchService의 close() 메소드를 호출하고 종료하면 된다.

<pre>
<code>
while(true){
    WatchKey watchKey = watchService.take();
    List<WatchEvent<?>> list = watchKey.pollEvents();
    for(WatchEvent watchEvent : list){
        ...
    }
    boolean valid = watchKey.reset();
    if(!valid) { break; }
}
watchService.close();
</code>
</pre>

## 버퍼

NIO에서는 데이터 입출력을 위해 항상 버퍼를 사용해야 한다. 버퍼는 읽고 쓰기가 가능한 메모리 배열이다.

### Buffer 종류

Buffer는 저장되는 데이터 타입에 따라 ByteBuffer, CharBuffer, IntBuffer 등으로 분류된다.
또한 어떤 메모리를 사용하느냐에 따라 Direct, NonDirect 버퍼로 분류된다.

| 구분 | NonDIrect 버퍼 | Direct 버퍼 |
| --- | --- | --- |
| 사용하는 메모리 공간 | JVM의 힙 메모리 | 운영체제의 메모리 |
| 버퍼 생성 시간 | 버퍼 생성이 빠르다 | 버퍼 생성이 느리다 |
| 버퍼의 크기 | 작다 | 크다(큰 데이터를 처리할 떄 유리) |
| 입출력 성능 | 낮다 | 높다(입출력이 빈번할 때 유리) |

아래 코드는 NonDirect 버퍼와 Direct 버퍼의 성능 테스트 결과를 출력한다. 
파일을 100번 복사하는데 걸린 시간을 측정한 결과 입출력이 빈번한 환경에서는 Direct 버퍼의 성능이 빠르다는 것을 확인할 수 있다.

<pre>
<code>
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class Main{
    public static void main(String[] args) throws Exception {
        Path from = Paths.get("D:/Hello/test1.txt");
        Path to1 = Paths.get("D:/Hello/test2.txt");
        Path to2 = Paths.get("D:/Hello/test3.txt");

        long size = Files.size(from);

        FileChannel fileChannel_from = FileChannel.open(from);
        FileChannel fileChannel_to1 = FileChannel.open(to1, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE));
        FileChannel fileChannel_to2 = FileChannel.open(to2, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE));

        ByteBuffer nonDirectBuffer = ByteBuffer.allocate((int)size);
        ByteBuffer direBuffer = ByteBuffer.allocateDirect((int)size);

        long start, end;

        start = System.nanoTime();
        for(int i = 0; i < 100; i++){
            fileChannel_from.read(nonDirectBuffer);
            nonDirectBuffer.flip();
            fileChannel_to1.write(nonDirectBuffer);
            nonDirectBuffer.clear();
        }
        end = System.nanoTime();
        System.out.println("NonDirect = " + (end-start) + "ns");

        fileChannel_from.position(0);
        start = System.nanoTime();
        for(int i = 0; i < 100; i++){
            fileChannel_from.read(direBuffer);
            direBuffer.flip();
            fileChannel_to2.write(direBuffer);
            direBuffer.clear();
        }
        end = System.nanoTime();
        System.out.println("Direct = " + (end-start) + "ns");

        fileChannel_from.close();
        fileChannel_to1.close();
        fileChannel_to2.close();
    }
}

결과)
NonDirect = 2610300ns
Direct = 1258501ns
</code>
</pre>

### Buffer 생성

NonDirect 버퍼는 Buffer 클래스의 allocate()와 wrap() 메소드를 호출한다.
Direct 버퍼는 ByteBuffer의 allocateDirect() 메소드를 호출한다.

### allocate() 메소드

allocate() 메소드는 JVM 힙 메모리에 NonDirect 버퍼를 생성한다. 해당 메소드로 다양한 데이터 타입의 Buffer를 생성할 수 있다.

다음은 최대 100개의 바이트를 저장하는 ByteBuffer를 생성하고, 최대 100개의 문자를 저장하는 CharBuffer를 생성하는 코드이다.

<pre>
<code>
ByteBuffer byteBuffer = ByteBuffer.allocate(100);
CharBuffer charBuffer = CharBuffer.allocate(100);
</code>
</pre>

### wrap() 메소드

각 타입별 Buffer 클래스는 모두 wrap() 메소드를 가지고 있다. 이 메소드는 이미 생성되어 있는 자바 배열은 래핑(wrapping)해서 Buffer 객체를 생성한다.
자바 배열은 JVM 힙 메모리에 생성되므로 wrap()은 NonDirect 버퍼를 생성한다. 

다음은 길이가 100인 byte[]를 이용해서 ByteBuffer를 생성하고, 길이가 100인 char[]를 이용해서 CharBuffer를 생성한다.

<pre>
<code>
byte[] byteArr = new byte[100];
ByteBuffer byteBufer = ByteBuffer.wrap(byteArr);

char[] charArr = new char[100];
CharBuffer charBuffer = CharBuffer.wrap(charArr);
</code>
</pre>

배열의 모든 데이터가 아닌 일부 데이터만 가지고 Buffer 객체를 생성할 수도 있다. 시작 인덱스와 길이를 추가로 지정하면 된다.
다음은 인덱스 0부터 50개만 버퍼로 생성한다.

<pre>
<code>
byte[] byteArr = new byte[100];
ByteBuffer byteBuffer = ByteBuffer.wrap(byteArr, 0, 50);

char[] charArr = new char[100];
CharBuffer charBuffer = CharBuffer.wrap(charArr, 0, 50);
</code>
</pre>

CharBuffer는 CharSequence 타입의 매개값을 갖는 wrap() 메소드도 제공한다. String이 CharSequence 인터페이스를 구현했기 때문에 매개값으로 문자열을 제공해서 다음과 같이 CharBuffer를 생성할 수도 있다.

<pre>
<code>
CharBuffer charBuffer = CharBuffer.wrap("NIO 입출력은 Buffer를 이용한다.");
</code>
</pre>

### allocateDirect() 메소드

ByteBuffer의 allocateDirect() 메소드는 운영체제(JVM 힙 메모리 바깥쪽)가 관리하는 메모리에 Direct 버퍼를 생성한다. 이 메소드는 Buffer 클래스에는 없고 ByteBuffer에서만 제공된다. 

각 타입별로 Direct 버퍼를 생성하고 싶다면 먼저 ByteBuffer의 allocateDirect() 메소드로 버퍼를 생성한 다음 타입별 asCharBuffer(), asIntBuffer()와 같이 Buffer를 얻는 메소드를 추가한다. 

다음은 100개의 바이트를 저장하는 다이렉트 ByteBuffer와 50개의 문자를 저장하는 다이렉트 CharBuffer, 25개의 정수를 저장하는 다이렉트 IntBuffer를 생성한다.
char는 2바이트 크기를 가지고, int는 4바이트 크기를 가지기 때문에 초기 다이렉트 ByteBuffer 생성 크기에 따라 저장 용량이 결정된다.

<pre>
<code>
ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);

CharBuffer charBurrer = ByteBuffer.allocateDirect(100).asCharBuffer();

IntBuffer intBuffer = ByteBuffer.allocateDirect(100).asIntBuffer();
</code>
</pre>

<pre>
<code>
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

public class Main {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);
        System.out.println("capacity = " + byteBuffer.capacity() + " byte");

        CharBuffer charBuffer = ByteBuffer.allocateDirect(100).asCharBuffer();
        System.out.println("capacity = " + charBuffer.capacity() + " 문자");

        IntBuffer intBuffer = ByteBuffer.allocateDirect(100).asIntBuffer();
        System.out.println("capacity = " + intBuffer.capacity() + " 정수");
    }
}

결과)
capacity = 100 byte
capacity = 50 문자
capacity = 25 정수
</code>
</pre>

### byte 해석 순서(ByteOrder)

데이터를 처리할 때 바이트 처리 순서는 운영체제마다 차이가 있다.
앞쪽 바이트부터 먼저 처리하는 것을 Big endian, 뒤쪽 바이트부터 먼저 처리하는 것을 Little endian 이라고 한다.

ByteOrder 클래스의 nativeOrder() 메소드는 현재 동작하는 운영체제가 Big endian인지 Little endian인지 알려준다.
JVM도 일종의 독립 운영체제이기 때문에 이런 문제를 취급하는데, JRE가 설치된 어떤 환경이든 JVM은 무조건 Big endian으로 동작한다. 

<pre>
<code>
import java.nio.ByteOrder;

public class Main {
    public static void main(String[] args) {
        System.out.println("OS = " + System.getProperty("os.name"));
        System.out.println("ByteOrder = " + ByteOrder.nativeOrder());
    }
}

결과)
OS = Windows 10
ByteOrder = LITTLE_ENDIAN
</code>
</pre>

### FileChannel

[FileChannel](https://docs.oracle.com/javase/7/docs/api/java/nio/channels/FileChannel.html)은 파일 읽기와 쓰기를 할 수 있다. 동기화 처리가 되어 있기 때문에 멀티 스레드 환경에서 사용해도 안전하다.

### FileChannel 생성 및 닫기

정적 메소드 open()을 호출해서 얻을 수 있지만, IO의 FileInputStream, FileOutputStream의 getChannel() 메소드를 호출해서 얻을 수도 있다.

<pre>
<code>
FileChannel fileChannel = FileChannel.open(Path path, OpenOption... options);
</code>
</pre>

첫 번째 path 매개값은 열거나 생성하고자 하는 파일의 경로를 Path 객체로 생성해서 지정한다.
두 번째 options 매개값은 열기 옵션 값이다. StandardOpenOption의 열거 상수를 나열해주면 된다.

"D:/Hello/test.txt" 파일을 생성하고 어떤 내용을 쓰고 싶다면 다음과 같이 매개값을 지정한다.

<pre>
<code>
FileChannel fileChannel = FileChannel.open(
    Paths.get("D:/Hello/test.txt"),
    StandardOpenOption.CREATE_NEW,
    StandardOpenOption.WRITE
);
</code>
</pre>

FileChannel을 더 이상 사용하지 않을 경우 close() 메소드를 호출한다.

<pre>
<code>
fileChannel.close();
</code>
</pre>

### 파일 쓰기와 읽기

파일에 바이트를 쓰려면 FileChannel의 write() 메소드를 호출한다. 매개값으로 ByteBuffer 객체를 주는데, 파일에 쓰여지는 바이트는 ByteBuffer의 position ~ limit까지이다. position이 0이고 limit이 capacity와 동일하다면 ByteBuffer의 모든 바이트가 파일에 쓰여진다. write() 메소드의 리턴값은 ByteBuffer에서 파일로 쓰여진 바이트 수이다.

<pre>
<code>
int bytesCount = fileChannel.write(ByteBuffer src);
</code>
</pre>

아래 코드는 FileChannel을 이용해서 문자열을 "D:/Hello/test1.txt" 파일에 저장한다.

<pre>
<code>
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {
    public static void main(String[] args) throws IOException{
        Path path = Paths.get("D:/Hello/test1.txt");
        Files.createDirectories(path.getParent());

        FileChannel fileChannel = FileChannel.open(
            path, StandardOpenOption.CREATE, StandardOpenOption.WRITE
        );

        String data = "Put some strings using by FileChannel";
        Charset charset = Charset.defaultCharset();
        ByteBuffer byteBuffer = charset.encode(data);

        int byteCount = fileChannel.write(byteBuffer);
        System.out.println("test1.txt = " + byteCount + " bytes written");

        fileChannel.close();
    }
}

결과)
test1.txt = 37 bytes written
</code>
</pre>

파일 읽기에서 파일로부터 바이트를 읽으려면 FileChannel의 read() 메소드를 호출한다. 매개값으로 ByteBuffer 객체를 주면 되는데, 파일에서 읽혀지는 바이트는 ByteBuffer의 position부터 저장된다. position이 0이면 ByteBuffer의 첫 바이트부터 저장된다.

read() 메소드의 리턴값은 파일에서 ByteBuffer로 읽혀진 바이트 수이다. 한 번 읽을 수 있는 최대 바이트수는 ByteBuffer의 capacity까지이다. 리턴되는 최대값은 capacity가 된다. 더 이상 읽을 바이트가 없으면 read() 메소드는 -1을 리턴한다.

<pre>
<code>
int bytesCount = fileChannel.read(ByteBuffer dst);
</code>
</pre>

버퍼에 한 바이트를 저장할 때마다 position이 1씩 증가한다. read() 메소드가 -1을 리턴할 때까지 버퍼에 저장한 마지막 바이트의 위치는 position -1 인덱스이다. 
아래 코드는 이전 예제에서 생성한 "D:/Hello/test1.txt" 파일을 읽고 콘솔에 출력한다.

<pre>
<code>
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {
    public static void main(String[] args) throws IOException{
        Path path = Paths.get("D:/Hello/test1.txt");

        FileChannel fileChannel = FileChannel.open(
            path, StandardOpenOption.READ);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);

        Charset charset = Charset.defaultCharset();
        String data = "";
        int byteCount;

        while(true){
            byteCount = fileChannel.read(byteBuffer);
            if(byteCount == -1) break;
            byteBuffer.flip();
            data += charset.decode(byteBuffer).toString();
            byteBuffer.clear();
        }

        fileChannel.close();
        System.out.println(data);
    }
}

결과)
Put some strings using by FileChanneljava/lang/String;xpwParktKimx
</code>
</pre>

# 출처
* [이것이 자바다](http://www.kyobobook.co.kr/product/detailViewKor.laf?ejkGb=KOR&mallGb=KOR&barcode=9788968481475&orderClick=LAG&Kc=)