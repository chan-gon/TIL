# Repository 가져와서 수정 후 다시 Push


## When
로컬에서 사용했던 폴더 및 파일을 삭제했거나, 포맷 작업의 이유로 GitHub Repository를 로컬 환경으로 다시 가져와서 작업 후 저장소에 반영해야 하는 경우.

## How

1. git clone [GitHub Repository 주소]

2. 로컬 환경에서 작업 시작

3. 로컬 환경에서 작업 완료

4. git init(**반드시 clone한 저장소 내부에서 해당 커맨드를 실행시키자**)

5. git add .

6. git commit -m "**커밋 메시지**"

7. git push origin master 또는 push 하는 특정 브랜치 이름