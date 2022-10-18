## REST

How do I improve HTTP without breaking the Web?  
REST는 서버와 클라이언트의 독립적인 진화를 목적으로 한다. 

## Stateless

클라이언트-서버간에 발생하는 request-response는 항상 이벤트가 발생하는 그 때에만 유효하다. 즉, 서버는 과거 클라이언트의 요청을 기억하지 않는다. 만약 클라이언트가 서버에 동일한 요청을 두 번 했다면 서버는 해당 요청에 대한 응답을 두 번 할 뿐이다.  

Stateless 특성을 실현하려면 서버에 전달하는 각 요청에는 서버가 해당 요청을 제대로 수행할 수 있는 모든 정보가 포함되어 있어야 한다.

## Client-Server 구조

Client와 Server는 서로 독립적으로 동작한다. Client는 요청만, Server는 요청에 대한 처리만 한다. 이런 관심사의 분리(Separation of concerns) 원칙을 고수함으로써 각 컴포넌트들의 유지보수성, 확장성이 증대된다.




# 참고
- https://www.ics.uci.edu/~fielding/pubs/dissertation/fielding_dissertation.pdf
- https://restfulapi.net/rest-architectural-constraints/#layered-system
- https://medium.com/extend/what-is-rest-a-simple-explanation-for-beginners-part-2-rest-constraints-129a4b69a582
- https://www.youtube.com/watch?v=RP_f5dMoHFc&list=RDLVRP_f5dMoHFc&start_radio=1&rv=RP_f5dMoHFc&t=4&ab_channel=naverd2