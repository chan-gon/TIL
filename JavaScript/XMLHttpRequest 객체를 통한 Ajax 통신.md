# XMLHttpRequest 객체를 통한 Ajax 통신

JavaScript는 브라우저에 내장된 XMLHttpRequest 객체를 통해 Ajax 통신을 구현한다.  
XMLHttpRequest는 XML 형식을 포함한 다양한 종류의 데이터를 취급할 수 있다. 

아래 이미지는 JavaScript 환경에서 Ajax 통신이 어떻게 진행되는지 설명한다.  

![ajax](./image/ajax_1.gif)

```
1. 웹 페이지 내에서 이벤트 실행.
2. JavaScript에 의해 XMLHttpRequest 객체 생성.
3. XMLHttpRequest 객체가 웹 서버로 요청사항 전달.
4. 웹 서버가 요청을 처리한다.
5. 웹 서버가 웹 페이지로 응답을 보낸다.
6. JavaScript가 웹 서버에서 보낸 응답을 읽어들인다.
7. JavaScript에 의해 최초에 실행한 이벤트에서 설계한 방식으로 응답에 대한 특정 행위가 발생한다.
```

Chrome, IE, Edge 등의 현존하는 모든 브라우저에는 XMLHttpRequest 객체가 내장되어 있다.  
JavaScript는 브라우저에 내장된 XMLHttpRequest를 활용해서 데이터 형식에 구애받지 않는 유연한 비동기 처리를 구현할 수 있다.  

XMLHttpRequest 객체를 활용한 통신 순서는 다음과 같다.  

```
1. XMLHttpRequest 객체 생성
2. 콜백 함수 정의
3. XMLHttpRequest 객체 open
4. 웹 서버로 요청 전송(send)
```

코드를 통해 살펴보자.  
w3school에서 제공하는 예제의 내용을 약간 수정했다.  

```
<body>
    <div id="demo">
        <h2>버튼을 클릭하면 이곳이 변경됩니다.</h2>
        <button type="button" onclick="changeText()">Change Content</button>
    </div>

      <script>
          function changeText() {
          
              // XMLHttpRequest 객체 생성
              const xhttp = new XMLHttpRequest();
              
              // 콜백 함수 정의
              xhttp.onload = function() {
                  document.getElementById('demo').childNodes[1].innerHTML = this.responseText;
              }
              
              // 객체 open
              xhttp.open("GET", "ajax_info.text", true);
              
              // 객체 전송(send)
              xhttp.send();
          }
      </script>
</body>
```

XMLHttpRequest 객체의 open 메소드를 살펴보자. 설정 내용이 많기 때문에 중요해 보인다.  

```
open(메소드, URL, async, user, psw)
- 메소드: GET 또는 POST
- URL : 리소스 위치
- async: 비동기 구현인지 아닌지의 여부(true=비동기, false=동기)
- user(optional) : 이름
- psw(optional) : 비밀번호
```

위 코드의 실행 결과는 아래와 같다.  
버튼을 클릭하면 서버 내부에서 ajax_info.text 파일을 검색해서 파일 내부의 내용을 비동기 형식으로 출력한다.  
파일 내부의 내용은 "텍스트 변경!" 이기 때문에 해당 내용이 버튼 클릭과 함께 출력되었다.  

![ajax](./image/ajax_2.gif)

XMLHttpRequest 객체 속성 중에 onreadystatechange와 readyState도 유용하게 사용된다.  

```
onreadystatechange
- readyState 속성이 변경되었을 경우 호출되는 콜백 함수 정의한다.

readyState
- XMLHttpRequest 상태를 가진다.

<XMLHttpRequest 상태>
0: request not initialized
1: server connection established
2: request received
3: processing request
4: request finished and response is ready
```

결괏값을 호출하는데 유용하게 사용되는 몇 가지 XMLHttpRequest 객체 속성을 소개한다.  

```
responseText
- 웹 서버의 응답 데이터를 String 형태로 반환.

responseXML
- 웹 서버의 응답 데이터를 XML 형태로 반환.

status
- 요청에 대한 응답 상태 번호를 반환.
- 상태 번호는 HTTP 상태 번호를 기준으로 한다.(200이면 OK, 404면 NOT FOUND)
```

XMLHttpRequest 객체 속성을 활용해서 이전보다 조금 더 복잡한 코드를 구성해보자.  
w3school에서 제공하는 예제를 약간 변경했다.  

open() 메소드에 설정된 "ajax_info.text" 파일을 성공적으로 찾을 경우와 찾지 못하는 경우로 분리해서 서로 다른 출력 값을 가지도록 onreadystatechange 속성을 통해 콜백 함수를 구성했다.  

```
<body>
    <div id="demo">
        <h2>버튼을 클릭하면 이곳이 변경됩니다.</h2>
        <button type="button" onclick="changeText()">Change Content</button>
    </div>

    <script>
        function changeText() {
            const xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    document.getElementById('demo').childNodes[1].innerHTML = this.responseText;
                }
                if (this.status == 404) {
                    document.getElementById('demo').childNodes[1].innerHTML = "NOT FOUND";
                }
            };
            xhttp.open("GET", "ajax_info.text");
            xhttp.send();
        }
    </script>
</body>
```

XMLHttpRequest 객체를 활용한 자세한 통신 방법은 아래 링크를 통해 알아보자.  

JavaScript는 XMLHttpRequest 객체를 통해 Ajax 구현을 한다는 것을 알게 되었다.  
다음 시간에는 JavaScript 대신 JQuery를 통해 write less, do more 하게 Ajax 구현하는 방법을 살펴보자.  

# 참고
* [W3SCHOOL](https://www.w3schools.com/js/js_ajax_http.asp)

