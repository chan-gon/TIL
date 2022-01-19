# Git 브랜치 전환

특정 브랜치를 사용하려면 checkout 명령어를 통해 명시적으로 브랜치를 지정해야 한다.

```
git checkout [branch]
```

checkout 명령어에 **-b** 옵션을 추가하면 브랜치 생성과 체크아웃을 동시에 할 수 있다.

```
git checkout -b newBranch

// Switched to a new branch 'newBranch'
```

만약 기존에 존재하는 브랜치 내용과 동일한 내용을 가진 새로운 브랜치를 생성하고 싶다면 아래와 같이 작성한다.  
기존 브랜치 원격 저장소의 이름이 **origin** 이고, 내용을 복사하고 싶은 브랜치 이름이 **oldBranch**라고 한다면,

```
git checkout -b newBranch origin/oldBranch

// Branch 'newBranch' set up to track remote branch 'oldBranch' from 'origin'.
// Switched to a new branch 'newBranch'
```