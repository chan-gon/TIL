# NIO 기반 입출력 및 네트워킹_1

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

# 출처
* [이것이 자바다](http://www.kyobobook.co.kr/product/detailViewKor.laf?ejkGb=KOR&mallGb=KOR&barcode=9788968481475&orderClick=LAG&Kc=)