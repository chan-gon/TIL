# jpa 사작하기

*인프런의 [자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic/dashboard) 강의를 듣고 정리한 내용입니다.*

### jpa 실습 환경 설정

jpa 실습 환경 설정을 위해 다음의 작업을 진행합니다.

1. H2 Database 설치 및 연결 확인
2. IDE를 통한 실습 프로젝트 생성
    - Maven으로 진행할 것.
    - Java 8 이상 사용할 것.

상기 작업은 구글링을 통해 쉽게 할 수 있으므로 생략하겠습니다.

메이븐 프로젝트 생성 후 JPA 및 H2 Database 사용을 위해 관련 의존성을 추가합니다.
- Hibernate EntityManager Relocation
- H2 Database Engine    

*Note*
만약 자바 8 버전 이상을 사용하고 있다면, 아래와 같이 JAXB API 의존성을 추가해야 합니다.  
JAXB는 Java Architecture For XML Bind의 약자로, marshal(Java Object를 XML 문서로 변환), unmarshal(XML 문서를 Java Object로 매핑) 작업을 수행합니다.  
문제는 자바 11 버전에서는 JAXB를 내장하고 있지 않기 때문에 의존성을 추가해서 외부 라이브러리를 당겨 와야 합니다. 이 작업이 누락될 시 **javax/xml/bind/JAXBException** 에러가 발생합니다.  
만약 본인이 자바 8 버전을 사용한다면 JAXB 2.2.8 버전을 내부에서 제공하고 있기 때문에 의존성 추가 작업이 필요없습니다.

pom.xml

```
<dependencies>
        <!-- https://mvnrepository.com/artifact/org.hibernate/hibernate-entitymanager -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>5.3.10.Final</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.h2database/h2 -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>1.4.200</version>
        </dependency>
    
        <dependency>
            <groupId>javax.xml.bind</groupId>
            <artifactId>jaxb-api</artifactId>
            <version>2.3.0</version>
        </dependency>
</dependencies>
```

### persistence.xml 작성하기

JPA는 persistence.xml 파일을 읽어서 설정 정보를 관리합니다.  
중요한 점은, **resources/META-INF/** 경로 아래에 위치해야 해당 파일을 인식해서 읽을 수 있습니다.

persistence.xml 파일의 주요 설정 내용은 어떤 DB에 연결할 것이고, 어떤 DB 작성 방식으로 쿼리를 다룰 것인가에 대한 설정을 합니다.

persistence.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2"
             xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
    <persistence-unit name="hello">
        <properties>
            <!-- 필수 속성 -->
            <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
            <property name="javax.persistence.jdbc.user" value="sa"/>
            <property name="javax.persistence.jdbc.password" value=""/>
            <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
            <!-- 옵션 -->
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.use_sql_comments" value="true"/>
            <!--<property name="hibernate.hbm2ddl.auto" value="create" />-->
        </properties>
    </persistence-unit>
</persistence>
```

- javax.persistence.jdbc.driver : JDBC 드라이버

- javax.persistence.jdbc.user : 데이터베이스 접속 ID
- javax.persistence.jdbc.password : 데이터베이스 접속 PASSWORD
- javax.persistence.jdbc.url : 데이터베이스 접속 URL
- hibernate.dialect : 데이터베이스 방언 설정
- hibernate.show_sql : hibernate가 실행한 SQL을 출력
- hibernate.format_sql : hibernate가 실행한 SQL을 보기 좋게 정렬
- hibernate.use_sql_comments : hibernate가 실행한 SQL을 출력할 때 주석도 함께 출력

### 객체와 테이블을 생성하고 매핑

자신이 사용하는 DB에 테이블을 생성하고, 테이블 및 테이블의 컬럼과 매핑될 수 있는 객체를 생성해야 합니다.

저는 Id를 PK로 하는 Member라는 이름의 테이블을 생성했습니다. 테이블 스펙에 맞게 POJO 클래스를 생성합니다.

여기서 중요한 것은, JPA가 이 클래스는 DB의 Member 테이블에 매핑되는 클래스라고 명시하는 @Entity 어노테이션을 클래스 레벨에서 선언해야 합니다.  
그리고 테이블의 PK를 명시하는 필드에는 @Id 어노테이션을 붙여서 JPA에게 알려줍니다.

```
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Member {

    @Id
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

### 실행 메소드 작성

아래 실행 메소드는 DB의 Member 테이블에 매핑 객체를 통해 데이터를 삽입하는 과정을 나타냅니다.

```
public class JpaMain {

    public static void main(String[] args) {
        // persistence.xml에 설정된 영속성 유닛(persistence unit) 이름 및 설정 내용을 불러와서 EntityManager 인스턴스 생성 준비를 한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // EntityManager 인스턴스를 생성한다.
        EntityManager entityManager = emf.createEntityManager();
        
        EntityTransaction tx = entityManager.getTransaction();

        // 테이블에 삽입할 엔티티 및 컬럼 값 정의
        Member member = new Member();
        member.setId(3L);
        member.setName("Kim");    

        try {
            // 데이터 변경 작업은 반드시 트랜잭션 내부에서 해야 한다.(조회는 제외)
            tx.begin();

            /*
                데이터 변경 작업은 반드시 트랜잭션 내부에서 해야 한다.
                엔티티 저장.
                이 타이밍에 엔티티가 DB에 저장되는 것은 아니고, 영속성 컨텍스트를 통해 엔티티를 영속성 컨텍스트에 저장하겠다는 선언이다.
            */
            entityManager.persist(member);

            // 커밋
            // 이 시점에서 삽입 쿼리문을 DB로 전송한다. 
            tx.commit();
        } catch (Exception e) {
            // 에러 발생시 롤백
            tx.rollback();
        } finally {
            // 엔티티 매니저 종료
            // 종료하지 않으면 성능 저하의 원인이 될 수 있다
            entityManager.close();
        }
        // 엔티티 매니저 팩토리 종료
        emf.close();
    }
}
```

# 참고 
- https://jaeho214.tistory.com/73
- https://www.inflearn.com/course/ORM-JPA-Basic/dashboard