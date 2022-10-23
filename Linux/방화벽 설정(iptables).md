# 방화벽 설정_iptables

방화벽이란 외부의 공개된 네트워크와 내부의 사설 네트워크 사이에서 외부와 내부에 전달되는 트래픽을 **정책(Policy)**에 따라 허용 또는 거부하는 역할을 하는 컴퓨터나 장치를 말한다.

CentOS7에서 기본으로 제공하는 방화벽 설정 도구는 firewalld다. 이 글은 firewalld를 사용하지 않고 iptables를 사용하여 방화벽 정책을 설정하는 내용을 담고 있다.

유사한 서비스를 제공하는 프로그램이 하나 이상 동시에 실행되고 있다면 예상치 못한 문제가 발생할 수 있다. 그래서 해당 시스템 사용을 중지(**systemctl stop firewalld**)할 뿐만이 아니라 마스킹(**systemctl mask firewalld**)을 통해 해당 서비스가 의존성에 의해 실행되는(또는 갑자기 실행되는) 상황 자체를 막을 수 있다.(서비스가 active 상태에서 mask 되면 해당 서비스가 재시작 또는 서버가 reboot 하기 전까지 계속 실행 상태를 유지한다.)

```
$ systemctl stop firewalld
$ systemctl mask firewalld
```

서비스가 마스킹 되면 심볼링크를 생성하고, 생성된 링크 파일은 **/dev/null** 경로에 저장된다.  
마스킹된 서비스 리스트는 아래 명령어를 통해 확인할 수 있다.

```
$ systemctl list-unit-files | grep -i mask
```

마스킹을 해제하고 싶다면 unmask 명령어를 사용한다.

```
$ systemctl unmask [서비스명]
```

다시 iptables 로 돌아와서, firewalld 사용을 중지했으니 iptables를 설치한다.

```
$ yum install iptables-services
```

설치 후 부팅 시 자동 실행되도록 설정한다.

```
$ systemctl enable iptables
$ systemctl enable ip6tables
```

이제 iptables 정책을 설정한다. 설정 방법은 두 가지가 있다.  
1. 커맨드 라인 입력.
2. 파일에서 직접 수정(**/etc/sysconfig/iptables**)



새로운 정책을 추가했거나 기존 정책을 삭제했다면 반드시 서비스를 재시작한다.

```
$ systemctl restart iptables
$ systemctl restart ip6tables
```

서비스 재시작 후 반드시 제대로 시작 되었는지 확인한다.

```
$ iptables -L
$ ip6tables -L
```

설정한 방화벽 정책을 다음 명령어를 통해 확인할 수 있다.

```
$ iptables -L
$ ip6tables -L
```



# 참고
* [송시의 적절한 정보공유](https://songsiaix.tistory.com/17)
* [The Geek Diary](https://www.thegeekdiary.com/how-to-mask-or-unmask-a-service-in-centos-rhel-7-and-8/)
* [rackspace technology](https://docs.rackspace.com/support/how-to/use-iptables-with-centos-7/)
* [CentOS](https://wiki.centos.org/HowTos/Network/IPTables)
* [https://kishe89.github.io/](https://kishe89.github.io/posts/2018/03/20/iptables-command.html)