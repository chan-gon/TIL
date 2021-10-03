# 기본 API 클래스 part.3

## Arrays 클래스
Arrays 클래스는 배열 조작(manipulating arrays) 기능을 가지고 있다. 배열 조작이란 배열의 복사, 항목 정렬, 항목 검색과 같은 기능을 말한다. 단순한 배열 복사는 System.arraycopy() 메소드를 사용할 수 있으나, Arrays는 추가적으로 항목 정렬, 항목 검색, 항목 비교와 같은 기능을 제공한다. 
Arrays 클래스의 모든 메소드는 정적(static) 이므로 Arrays 클래스로 바로 사용이 가능하다.

![Arrays 클래스 메소드](./image/java_chapter11_15.png)
 
출처 : https://palpit.tistory.com/entry/Java-%EC%9E%90%EB%B0%94-%EA%B8%B0%EB%B3%B8-API-Arrays-Class

### 배열 복사
배열 복사를 위해 copyOf(원본배열, 복사할길이), copyOfRange(원본배열, 시작인덱스, 끝인덱스)이다. copyOf() 메소드는 원본 배열의 0번 인덱스에서 복사할 길이만큼 복사한 타겟 배열을 리턴하는데, 복사할 길이는 원본 배열의 길이보다 커도 되며, 타겟 배열의 길이가 된다. 다음은 arr1[] 배열의 전체 항목을 복사해서 arr2[] 배열을 생성한다.

<pre>
<code>
char[] arr1 = {'A','B','C','D'};
char[] arr2 = Arrays.copyOf(arr1, arr1.length);
</code>
</pre>

copyOfRange(원본배열, 시작인덱스, 끝인덱스)는 원본 배열의 시작 인덱스에서 끝 인덱스까지 복사한 배열을 리턴한다. 시작 인덱스는 포함되지만, 끝 인덱스는 포함되지 않는다. 다음은 arr1[] 배열 항목 중 1번,2번 인덱스 항목을 arr2[] 배열의 0번, 1번 인덱스 항목으로 복사한다.
<pre>
<code>
char[] arr1 = {'A','B','C','D'};
char[] arr2 = Arrays.copyOfRange(arr1, 1, 3); // 시작 인덱스는 포함, 끝인덱스는 포함하지 않으므로 arr1[] 배열의 1번, 2번 인덱스만 복사된다.
</code>
</pre>

단순 배열 복사만이 목적이라면 Arrays 클래스를 사용하지 않고 System.arraycopy() 메소드를 이용할 수 있다. 
System.arraycopy() 메소드는 5개의 매개값이 필요하다.
<pre>
<code>
System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
System.arraycopy(원본배열, 원본시작인덱스, 타겟배열, 타겟시작인덱스, 복사개수)
</code>
</pre>

다음은 Arrays와 System.arraycopy()를 이용해 배열을 복사한다.
<pre>
<code>
public class Main {
    public static void main(String[] args) {
        char[] arr1 = {'A','B','C','D'};

        // 방법 1
        char[] arr2 = Arrays.copyOf(arr1, arr1.length); // arr1 전체를 복사

        // 방법 2
        char[] arr3 = Arrays.copyOfRange(arr1, 1, 3); // 부분 복사

        // 방법 3
        char[] arr4 = new char[arr1.length];
        System.arraycopy(arr1, 0, arr4, 0, arr1.length); // arr1 전체를 arr4로 복사
    }
}
</code>
</pre>

### 배열 항목 비교
Arrays의 equals() 와 deepEquals()는 배열 항목을 비교한다. equals()는 1차 항목의 값만 비교하고, deepEquals()는 1차 항목이 서로 다른 배열을 참조할 경우 중첩된 배열의 항목까지 비교한다.

<pre>
<code>
public class Main {
    public static void main(String[] args) {
        int[][] original = {{1,2}, {3,4}};
        
        // 얕은 복사후 비교
        int[][] cloned1 = ArrayscopyOf(original, original.length);
        System.out.println(original.equals(cloned1)); // 배열 번지 비교 = false
        System.out.println(Arrays.equals(original, cloned1)); // 1차 배열 항목값 비교 = true
        System.out.println(Arrays.deepEquals(original, cloned1)); // 중첩 배열 항목값 비교 = true

        // 깊은 복사후 비교
        int[][] cloned2 = Arrays.copyOf(original, original.length);
        cloned2[0] = Arrays.copyOf(original[0], original[0].length);
        cloned2[1] = Arrays.copyOf(original[1], original[1].length);
        System.out.println(original.equals(cloned2)); // 배열 번지 비교 = false
        System.out.println(Arrays.equals(original, cloned2)); // 1차 배열 항목값 비교 = false
        System.out.println(Arrays.deepEquals(original, cloned2)); // 중첩 배열 항목값 비교 = true
    }
}
</code>
</pre>

### 배열 항목 정렬
기본 타입 또는 String 배열은 Arrays.sort() 메소드의 매개값으로 지정하면 자동으로 오름차순 정렬이 된다. 사용자 정의 클래스 타입일 경우 클래스가 Comparable 인터페이스를 구현하고 있어야 정렬이 된다.

Member 배열에서 Member 객체들을 name 필드값으로 정렬하고 싶다면 다음과 같이 클래스를 작성하면 된다.
<pre>
<code>
public class Member implements Comparable<Member> {
    String name;
    Member(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Member o) {
        return name.compareTo(o.name);
    }
}
</code>
</pre>
Comparable<Member>는 Member 타입만 비교하기 위해 제네릭 <>을 사용하였고, comapreTO() 메소드는 비교값을 리턴하도록 오버라이딩했다. compareTo() 메소드의 리턴값은 오름차순일 때 자신의 매개값보다 낮을 경우 음수, 같을 경우 0, 높을 경우 양수를 리턴한다. 반대로 내림차순일 때 자신이 매개값보다 낮을 경우 양수, 같은 경우 0, 높을 경우 음수를 리턴한다. name 필드값으로 정렬하기 위해 유니코드로 비교를 해야 하므로 String의 compareTo() 리턴값을 사용한다. 

<pre>
<code>
public class Main {
    public static void main(String[] args) {
        int[] scores = {99,98,97};
        Arrays.sort(scores); // {97,98,99}
        
        String[] names = {"홍길동", "박동수", "김민수"};
        Arrays.sort(names); // 김민수, 박동수, 홍길동

        Member m1 = new Member("홍길동");
        Member m2 = new Member("박동수");
        Member m3 = new Member("김민수");
        Member[] members = {m1, m2, m3};
        Arrays.sort(members); // 김민수, 박동수, 홍길동
    }
}
</code>
</pre>

### 배열 항목 검색
배열 항목에서 특정 값이 위치한 인덱스를 얻는 것을 배열 검색이라고 한다. 배열 항목을 검색하려면 반드시 Arrays.sort() 메소드로 항목들을 오름차순으로 정렬한 후, Arrays.binarySearch() 메소드로 항목을 찾아야 한다.
<pre>
<code>
public class Main {
    public static void main(String[] args) {
        // 기본 타입값 검색
        int[] scores = {99,98,97};
        Arrays.sort(scores); // {97,98,99}
        int index = Arrays.binarySearch(scores, 99);
        System.out.println("99의 인덱스: " + index); // 2

        // 문자열 검색
        String[] names = {"홍길동", "박동수", "김민수"};
        Arrays.sort(names); // 김민수, 박동수, 홍길동
        index = Arrays.binarySearch(names, "홍길동");
        System.out.println("홍길동의 인덱스: " + index); // 2

        // 객체 검색
        Member m1 = new Member("홍길동");
        Member m2 = new Member("박동수");
        Member m3 = new Member("김민수");
        Member[] members = {m1, m2, m3};
        Arrays.sort(members); // 김민수, 박동수, 홍길동
        index = Arrays.binarySearch(members, m1);
        System.out.println("홍길동의 인덱스: " + index); // 2
    }
}
</code>
</pre>

## Math, Random 클래스
java.lang.Math 클래스는 수학 계산에 사용할 수 있는 메소들르 제공한다. Math 클래스가 제공하는 메소드는 모두 정적(static) 이므로 Math 클래스로 바로 사용이 가능하다. 

![Math 클래스 메소드](./image/java_chapter11_16.png)

출처 : https://palpit.tistory.com/entry/Java-%EC%9E%90%EB%B0%94-%EA%B8%B0%EB%B3%B8-API-Math-Random-Class

<pre>
<code>
public class Main {
    public static void main(String[] args) {
        int v1 = Math.abs(-5);
        double v2 = Math.abs(-3.14);
        System.out.println(v1); // 5
        System.out.println(v2); // 3.14

        double v3 = Math.ceil(5.3);
        double v4 = Math.ceil(-5.3);
        System.out.println(v3); // 6.0
        System.out.println(v4); // 5.0

        double v5 = Math.floor(5.3);
        double v6 = Math.floor(-5.3);
        System.out.println(v5); // 5.0
        System.out.println(v6); // 6.0

        int v7 = Math.max(5,9);
        double v8 = Math.max(5.3, 2.5);
        System.out.println(v7); // 9
        System.out.println(v8); // 5.3

        int v9 = Math.min(5,9);
        double v10 = Math.min(5.3, 2.5);
        System.out.println(v7); // 5
        System.out.println(v8); // 2.5


        // Math.random() 메소드는 0.0과 1.0 사이의 범위에 속하는 하나의 double 타입의 값을 리턴한다. 0.0은 범위에 포함되고, 1.0은 포함되지 않는다.
        double v11 = Math.random();
        System.out.println(v11); //0.7318080259075397(랜덤)

        // Math.random() 메소드로 1부터 10까지의 난수를 얻으려면
        int num = (int)(Math.random()*10) + 1;
        System.out.println(num);

        long v12 = Math.round(5.3);
        long v13 = Math.round(5.7);
        System.out.println(v12); // 5
        System.out.println(v13); // 6

        // 소수 셋째자리에서 반올림하는 코드
        // round() 메소드는 항상 소수점 첫째 자리에서 반올림해서 정수값을 리턴한다.
        // 원하는 소수 자릿수에서 반올림된 값을 얻기 위해서는 반올림할 자릿수가 소수점 첫째 자리가 되도록 10n을 곱한 후, round() 메소드의 리턴값을 얻는다. 그리고 나서 다시 10n.0을 나눠주면 된다.
        
        double value = 12.3456;
        double temp1 = value * 100;
        long temp2 = Math.round(temp1);
        double v14 = temp2 / 100.0; 
        System.out.println(v14); // 12.35
    }
}
</code>
</pre>

Math.random() 메소드로 임의의 주사위 눈 얻기
<pre>
<code>
public class Main {
    public static void main(String[] args) {
       int num = (int)(Math.random()*6)+1;
       System.out.println(num);
    }
}
</code>
</pre>

## Random 클래스
java.util.Random 클래스는 난수를 얻어내기 위해 다양한 메소드를 제공한다. Math.random() 메소드는 0.0에서 1 사이의 double 난수를 얻는 데만 사용한다면, Random 클래스는 boolean, int, long, float, double 난수를 얻을 수 있다. 또 다른 차이점은 Random 클래스는 종자값(seed)을 설정할 수 있다. 종자값은 난수를 만드는 알고리즘에 사용되는 값으로 종자값이 같으면 같은 난수를 얻는다. Random 클래스로부터 Random 객체를 생성하는 방법은 다음 두 가지가 있다.

| 생성자 | 설명 |
| --- | --- |
| Rnadom() | 호출 시마다 다른 종자값(현재시간 이용)이 자동 설정된다. |
| Random(long seed) | 매개값으로 주어진 종자값이 설정된다. |

다음은 Random 클래스가 제공하는 메소드이다.

![Math 클래스 메소드](./image/java_chapter11_17.png)

출처 : https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=mals93&logNo=220727857479

다음은 로또 여섯 숫자를 얻는 방법이다.
<pre>
<code>
public class Main {
    public static void main(String[] args) {
       
       // 선택번호
       int[] selectedNumber = new int[6];
       Random random = new Random(3);
       for(int i = 0; i<6; i++) {
           selectedNumber[i] = random.nextInt(45)+1;
           System.out.println(selectedNumber[i] + " ");
       }

       // 당첨번호
       int[] winningNumber = new int[6];
       random = new Random(5);
       for(int i = 0; i<6; i++) {
           winningNumber[i] = random.nextInt(45)+1;
           System.out.println(winningNumber[i] + " ");
       }

       // 당첨여부
       Arrays.sort(selectedNumber);
       Arrays.sort(winningNumber);
       boolean result = Arrays.equals(selectedNumber, winningNumber);
       if(result) {
           System.out.println("당첨");
       }else{
           System.out.println("낙첨");
       }
    }
}
</code>
</pre>
선택 번호 6개를 얻기 위해 Random 객체를 생성할 때 종자값으롤 3을 주었고, 당첨 번호 6개를 얻기 위해 Random 객체를 생성할 때 종자값으로 5를 주었다. 서로 다른 종자값을 주었기 때문에 선택 번호와 당첨 번호는 다를 수밖에 없다. 만약 종자값이 같으면 같은 난수를 얻기 때문에 선택 번호와 당첨 번호가 동일하게 된다.
종자값을 매개값으로 설정하지 않아도 자동으로 설정되지만, 로또 번호는 선택 번호와 당첨 번호가 같도록 할 확률을 낮추어야 하기 때문에 의도적으로 매개값을 설정했다.


# 출처
* [이것이 자바다](http://www.kyobobook.co.kr/product/detailViewKor.laf?ejkGb=KOR&mallGb=KOR&barcode=9788968481475&orderClick=LAG&Kc=)
* [java.util.Arrays](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Arrays.html)
* [java.lang.Math](https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html)