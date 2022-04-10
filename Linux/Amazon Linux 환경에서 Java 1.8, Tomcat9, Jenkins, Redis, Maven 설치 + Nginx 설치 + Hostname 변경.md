# Amazon Linux 환경에서 Java 1.8, Tomcat9, Jenkins, Redis, Maven 설치 + Nginx 설치 + Hostname 변경

## Java 1.8 설치

```
sudo yum install -y java-1.8.0-openjdk-devel.x86_64
```

설치 후 확인

```
java -version
```

## Hostname 변경

```
sudo hostnamectl set-hostname [호스트 이름]
```

작업 후 인스턴스 재부팅

```
sudo reboot
```

## Tomcat 9 설치

[링크](https://tomcat.apache.org/download-90.cgi)에서 tar.gz 링크를 복사합니다.  

서버로 이동해서 wget 명령어 뒤에 붙여넣기를 합니다. 붙여넣는 방법은 Windows와 같은 Ctrl + V 가 아니라 마우스 오른쪽을 누르면 자동으로 붙여넣기가 됩니다.(먼저 복사를 했다는 가정 하에 작동합니다.)

```
wget [tar.gz 다운로드 주소]
```

다운로드 파일을 압축 해제 합니다.  
* 를 사용하면 apache로 시작하는 파일 이라는 의미를 가집니다.

```
tar xvfz apache*
```

압출 해제 후 다운받은 tar.gz 파일은 필요없기 때문에 삭제합니다.  

```
rm -rf apache*.gz
```

/usr/local/tomcat 경로로 압축 해제 파일을 옮깁니다. 경로 설정에 절대적인 기준은 없는 것 같습니다.  
/usr/local 경로에 tomcat 이라는 폴더가 존재하지 않습니다. 그래서 tomcat 이라는 폴더가 새로 생성되고, 기존의 압축 해제 폴더인 apache~ 폴더가 tomcat으로 변경되었습니다.

```
mv apache* /usr/loca/tomcat
```

톰캣 설정 확인은 tomcat 경로의 /conf 폴더 내부의 파일에서 확인 가능합니다.  
이곳에서는 server.xml, tomcat-users.xml 등의 파일 열람 및 수정 작업을 할 수 있습니다.  
톰캣 실행은 /bin 폴더의 startup.sh 스크립트를 실행하면 됩니다. 톰캣 중지는 /bin 폴더의 shutdown.sh 스크립트를 실행합니다.

```
/bin 폴더로 이동 후
./startup.sh

./shutdown.sh
```

톰캣의 기본 포트 번호는 8080 입니다. 톰캣이 제대로 실행 되었는지 확인하려면 8080 포트가 LISTEN 하고 있는지 확인하면 됩니다. 톰캣이 실행중이라면 8080 포트가 보이고, 톰캣이 중단되어 있다면 8080 포트가 보이지 않을 것입니다.

```
netstat -tln
```

## Jenkins 설치

최신화 작업을 진행합니다.

```
sudo yum update -y
```

기본 패키지에는 Jenkins가 포함되어 있지 않아 sudo yum install jenkins 명령어를 실행하면 패키지가 없다는 메시지가 출력됩니다. 그래서 yum 패키지 저장소에 Jenkins를 수동으로 등록하는 작업을 먼저 해줍니다.

```
sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
```

그 다음 Jenkins 저장소 키를 등록합니다.

```
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key
```

이제 패키지가 수동으로 저장소에 등록되었습니다. 설치 작업을 진행합니다.

```
sudo yum install jenkins -y
```

설치 후 Jenkins를 실행해봅니다.

```
sudo service jenkins start
```

OK 메시지가 출력되면 정상적으로 실행된 것입니다.  

Jenkins의 기본 포트 번호는 8080 입니다. Tomcat과 동일하기 때문에 Jenkins와 Tomcat의 포트 번호를 다르게 수정해야 합니다.  
[아이피 번호]:8080을 브라우저에 입력하면 Jenkins 로그인 화면이 등장합니다.  
빨간 블록의 글씨에 있는 경로로 이동해서 Administrator password를 입력하면 됩니다. 참고로 secrets 폴더 및 내부 파일은 권한 변경 작업을 해야만 이동 및 열람 가능합니다.  

## Maven 설치

아래 명령어를 한 줄씩 차례대로 입력합니다.

```
sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
sudo yum install -y apache-maven
mvn --version
```

## Redis 설치

저는 프로젝트에 Redis 구현을 했습니다. 그래서 Jenkins에서 빌드 작업을 할 때 Redis가 실행되어 있지 않으면 에러가 발생하더군요.  
먼저 update 작업을 합니다.

```
sudo yum update -y
```

gcc make를 설치합니다.

```
sudo yum install gcc make -y
```

Redis를 설치하고 압축을 해제합니다.

```
sudo wget http://download.redis.io/releases/redis-6.2.5.tar.gz
sudo tar xzf redis-6.2.5.tar.gz
```

압축을 해제한 폴더로 이동해서 gcc make로 컴파일 합니다.

```
cd redis-6.2.5
make
```

디렉토리를 생성하고 Redis 설정 파일을 복사합니다.

```
sudo mkdir /etc/redis
sudo mkdir /var/lib/redis
sudo cp src/redis-server src/redis-cli /usr/local/bin/
sudo cp redis.conf /etc/redis/
```

redis.conf 설정 파일을 수정합니다.

```
sudo vi /etc/redis/redis.conf
```

redis.conf 파일의 내용 중에서 아래 내용을 찾아서 수정합니다.  
redis.conf 파일의 내용이 많기 때문에 / 키를 누르고 검색어 입력 후 엔터를 누르면 원하는 단어를 검색할 수 있습니다. 이 기능을 활용해서 수정합니다.

/검색어

```
bind 0.0.0.0

daemonize [yes]

logfile [/var/log/redis_6379.log]

dir [/var/lib/redis]
```

Redis 서버 초기화 스크립트를 다운로드 합니다.

```
sudo wget https://raw.github.com/saxenap/install-redis-amazon-linux-centos/master/redis-server
```

다운로드 받은 파일을 /etc/init.d 경로로 옮기고 권한을 설정합니다.

```
sudo mv redis-server /etc/init.d
sudo chmod 755 /etc/init.d/redis-server
```

redis-server 파일의 redis 항목을 체크합니다.

```
sudo vim /etc/init.d/redis-server

// redis-server에 아래 내용으로 설정되어 있는지 확인!
redis="/usr/local/bin/redis-server"
prog=$(basename $redis)
```

redis-server를 서버 시작과 함께 자동으로 실행하도록 설정합니다.

```
sudo chkconfig --add redis-server
sudo chkconfig --level 345 redis-server on
```

redis를 실행해봅니다.

```
sudo service redis-server start
```

redis 실행을 확인합니다.

```
redis-cli ping

// PONG 메시지가 리턴되면 제대로 실행된 것입니다.
```

redis 접속은 아래 명령어로 할 수 있습니다.

```
redis-cli
```

## Nginx 설치

일반적으로 소개하는 sudo yum install nginx 명령어를 실행하면 패키지를 찾을 수 없다는 메시지가 출력됩니다. yum 명령어를 통해 nginx 패키지를 찾을 수 없습니다. 그래서 아래의 명령어를 통해 nginx를 설치합니다.

```
sudo amazon-linux-extras install nginx1
```

설치가 완료되면 설치 여부를 확인합니다.

```
nginx -v
```

nginx 시작 명령어 입니다.

```
sudo service nginx start
```