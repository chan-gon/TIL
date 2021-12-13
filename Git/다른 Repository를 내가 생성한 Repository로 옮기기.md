# 다른 Repository를 내가 생성한 Repository로 옮기기

다른 사람의 Repository를 내가 생성한 Repository로 옮겨서 뜯어보고 실습 해보고 싶을 때 이렇게 하면 된다.  

## 1. 자신의 Repository 생성

## 2. 다른 사람의 Repository 주소를 복사 -> clone

윈도우 커맨드 또는 Git CMD를 실행 후 자신의 컴퓨터 로컬 환경에서 해당 Repository를 저장할 위치로 이동 후 다음 명령어를 입력한다.
- git clone [Repistory 주소]

## 3. git remote

clone 작업을 한 Repository 내용을 내가 생성한 Repository 원격 저장소로 이동시킨다.  
다음 명령어를 입력한다.
- git remote add origin [자신의 Repository 주소]

*만약 remote origin already exists 와 같은 에러가 발생하면 'git remote rm origin' 명령어를 통해 제거 후 다시 명령어를 입력한다.*

## 4. 브랜치 생성

- git branch -M main

*-M 옵션은 기존에 동일한 이름의 branch가 있더라도 덮어쓰는 옵션이다.*

## 5. git push -u origin main

push 및 확인
