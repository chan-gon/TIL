# JOIN

join은 두 개 이상의 테이블 또는 뷰를 합친 결과를 출력하는 쿼리문 작성 기법이다.  
만약 사용하는 테이블에 중복되는 이름을 가진 컬럼이 존재한다면, 반드시 해당 컬럼을 쿼리문에 명명해서 불확실성을 제거한다.  

## 조인 조건

모든 조인 구문은 FROM 또는 WHERE 절을 사용한 하나 이상의 조인 조건을 포함한다.

## Equijoins

동일 연산자(equality operator), 즉 **=** 연산자를 사용하는 조인 구문이다.  
equijoin은 **비교하는 조인 조건의 컬럼 값이 동일한 행**만 반환한다. 이는 **조인 조건에 부합하는** 행을 반환하는 inner join과는 미묘하게 다르다고 할 수 있다. 

## Self Joins

self join은 자기 자신과 조인하는 조인 구문 작성 방법이다. 하나의 테이블에서 PK(Primary Key)를 참조하는 외래키(Foreign Key)가 있을 경우 사용된다. 

## Inner Joins

simple join이라고 부르기도 한다. 두 개 또는 그 이상의 테이블을 조인하며, 오직 조인 컨디션에 부합하는 결과만 출력한다. 

## Outer Joins

outer join은 inner join과 마찬가지로 조인 컨디션에 부합하는 결과를 출력하고, 추가로 조인 조건에 부합하지 않는 결과도 출력한다. 즉 A outer join B를 하면 조인을 통해 출력하고자 하는 A, B의 컬럼 내용을 모두 출력한다.

outer join은 left outer join, right outer join으로 세분화 된다. 

만약 A, B 두 개의 테이블을 조인하는데 A 테이블의 데이터는 모두 출력하고, B 테이블의 데이터는 조인 조건에 맞는 데이터만 출력하고 싶다면 A left outer join과 같이 A 테이블을 왼쪽에 위치시킨다.

반대로 B 테이블의 데이터는 모두 출력하고, A 테이블의 데이터는 조인 조건에 맞는 데이터만 출력하고 싶다면 A right outer join 을 작성한다. 이렇게 하면 오른쪽에 위치할 B 테이블의 데이터는 모두 outer join의 결과를 출력한다.

만약 A, B 테이블 두 테이블의 모든 데이터를 출력하고 싶다면 full outer join을 사용하자.

## 참고
* [Oracle SQL Language Reference 11g Release 2(11.2)](https://docs.oracle.com/cd/E11882_01/server.112/e41084/title.htm)
* [w3resource](https://www.w3resource.com/sql/tutorials.php)
