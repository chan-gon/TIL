# 메이븐 프로젝트 배포 및 자동화(with 톰캣)

Tomcat 웹 서버를 별다른 디렉토리 설정 없이 포트 번호만 입력해서 접속하면(ip 주소:8080) Tomcat 폴더의 webapps/ROOT 디렉토리의 웹 애플리케이션이 서비스 된다. 서버에 톰캣 설치 후 ROOT 디렉토리를 보면 기본 리소스가 모여있는 것을 확인할 수 있다. 따라서 본인이 구현한 웹 애플리케이션을 Tomcat을 통해 배포하고 싶다면 Tomcat 폴더의 webapps/ROOT 디렉토리에 배포를 원하는 웹 애플리케이션을 위치시키면 된다.

## 작업환경

- Ubuntu 18.04.6 LTS
- Tomcat 8.5.73
- Apache Maven 3.8.4

## Git Clone

- 배포를 원하는 Git Repository를 clone 한다.

```
clone [repository 주소]
```

## 메이븐 빌드

- 메이븐 프로젝트이기 때문에 빌드를 진행한다. git에서 clone한 프로젝트 디렉토리에서 진행한다.
- Dmaven.test.skip=true는 테스트를 생략하라는 의미다.

```
mvn clean package -Dmaven.test.skip=true
```

빌드가 완료되면 프로젝트 내에 target 디렉토리가 생성되고, target 디렉토리 내부에 빌드 결과물이 생성된다.

target 디렉토리를 확인하면 배포 가능한 상태로 생성된 프로젝트 디렉토리와 프로젝트 war 파일이 생성된 것을 확인할 수 있다.

배포하는 방법은 프로젝트 디렉토리를 그대로 사용하거나, war 파일을 사용하는 두 가지 방법이 있다. 이 글에서는 프로젝트 디렉토리를 Tomcat 서버로 복사해서 배포하는 방법을 사용한다.

## Tomcat 디렉토리에 경로 설정

- webapps 디렉토리에 있는 ROOT 디렉토리를 제거 후 배포를 원하는 프로젝트 디렉토리가 ROOT처럼 동작하도록 설정한다.
- 먼저 webapps의 ROOT 디렉토리를 제거한다.

```
rm -rf ROOT
```

프로젝트 빌드 결과물이 위치한 target 폴더로 이동 후 생성된 프로젝트 디렉토리를 Tomcat 디렉토리의 webapps 디렉토리의 ROOT로 이동하도록 설정한다. 

```
mv [프로젝트 디렉토리] [Tomcat의 webapps/ROOT]
```

Tomcat의 webapps/ROOT 디렉토리로 이동하면 프로젝트가 ROOT 디렉토리에 배포된 것을 확인할 수 있다.

Tomcat을 실행하고 설정된 포트 번호로 접속을 시도하여 프로젝트 실행을 확인한다.

## 쉘 스크립트 작성을 통한 빌드 자동화

로컬 환경에서 작업 후 git repository에 반영한 내용을 서버에서 pull 해서 빌드 후 Tomcat의 ROOT 디렉토리에 다시 배포하는 과정을 자동화한다.

예를 들어 아래와 같이 script.sh 파일을 작성 후 최신 내용을 업로드한 프로젝트를 다시 배포하고 싶다면 script.sh 파일을 실행하면 된다.

```
#!/bin/bash

TOMCAT_DIR=~/tomcat

git pull
mvn clean package -Dmaven.test.skip=true

$TOMCAT_DIR/bin/shutdown.sh

rm -rf $TOMCAT_DIR/webapps/ROOT

mv [배포하는 프로젝트 디렉토리] $TOMCAT_DIR/webapps/ROOT

$TOMCAT_DIR/bin/startup.sh
```

- **TOMCAT_DIR=~/tomcat** : 중복되는 내용은 변수처럼 작성해서 사용할 수 있다.
- **git pull** : git repository에서 변경 내용을 가져오기 위해 pull 한다.
- **mvn clean package -Dmaven.test.skip=true** : 프로젝트 빌드 작업(테스트 생략)
- **$TOMCAT_DIR/bin/shutdown.sh** : 원활한 작업을 위해 Tomcat종료
- **rm -rf $TOMCAT_DIR/webapps/ROOT** : 기존 webapps/ROOT 디렉토리 제거
- **mv [배포하는 프로젝트 디렉토리] $TOMCAT_DIR/webapps/ROOT** : 새롭게 배포된 프로젝트 디렉토리를 webapps/ROOT 디렉토리로 배포
- **$TOMCAT_DIR/bin/startup.sh** : Tomcat시작

## 참고

* [web server 및 어플리케이션의 이해와 tomcat 구조](https://jang8584.tistory.com/72)
* [Tomcat 서버에 웹 애플리케이션 빌드 및 배포](https://www.youtube.com/watch?v=bzM1WL4qdoA)
* [쉘 스크립트를 활용한 배포 자동화](https://www.youtube.com/watch?v=U7tZnEiYJyE)