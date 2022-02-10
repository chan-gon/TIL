# @PathVariable, @RequestParam, @RequestBody, @ResponseBody

## @PathVariable

- URI 경로의 일부를 파라미터로 사용할 때 이용(URI 경로에서 값을 가져온다)  
- 템플릿 변수의 값을 추출하고 그 값을 메소드 변수에 할당하는데 사용된다.  

```
@RequestMapping("/users/{userid}", method=RequestMethod.GET)
public String getUser(@PathVariable String userId) {
  // implementation omitted...
}
```

요청 결과 localhost:8080/users/charlie 와 같은 URI가 넘어왔다고 하자.  
'charlie'는 URI 템플릿 변수의 값으로, getUser 메소드 매개변수 userId의 요청 값으로 바인딩된다.  

```
@RequestMapping("/owners/{ownerId}", method=RequestMethod.GET)
public String findOwner(@PathVariable("ownerId") String theOwner, Model model) {
  // implementation omitted
}
```

URI 템플릿 변수 이름과 메소드 매개변수 이름이 반드시 동일해야 하는 것은 아니다.  

```
@RequestMapping("/owners/{ownerId}/pets/{petId}", method=RequestMethod.GET)
public String findPet(@PathVariable String ownerId, @PathVariable String petId, Model model) {
  Owner owner = ownerService.findOwner(ownderId);  
  Pet pet = owner.getPet(petId);  
  model.addAttribute("pet", pet);  
  return "displayPet"; 
}
```

여러개의 @PathVariable 어노테이션을 사용할 시 URI 템플릿 변수 역시 어노테이션으로 지정된 매개변수 숫자에 맞게 설정되어야 한다.  

## @RequestParam

- 쿼리 스트링에서 값을 가져온다.  

```
@GetMapping("/api/foos")
@ResponseBody
public String getFoos(@RequestParam String id) {
    return "ID: " + id;
}
```

해당 어노테이션에 명시된 매개변수 값은 반드시 파라미터 값이 넘어와야 한다. 넘어오지 않으면 400에러(Bad Request)가 발생한다.
위 코드에서 **@RequestParam 어노테이션의 매개변수로 지정된 id에 대한 파라미터 값은 반드시 넘어와야 한다.**

```
@GetMapping("/api/foos")
@ResponseBody
public String getFoos(@RequestParam(required = false) String id) { 
    return "ID: " + id;
}
```

필수 파라미터가 아닌 경우 required=false 설정을 추가한다.(기본 설정은 required=true) 이렇게 하면 파라미터가 넘어오지 않아도 에러가 발생하지 않는다.

```
@GetMapping("/api/foos")
@ResponseBody
public String getFoos(@RequestParam(required = false) String id, defaultValue="charlie") { 
    return "ID: " + id;
}
```

추가로 필수가 아닌 파라미터 값이 없을 때 null 값을 할당하는데, null 할당이 불가능한 기본 데이터 타입의 경우 타입 변환 에러가 발생한다. 따라서 defaultValue 속성을 통해 기본 값을 설정한다.  

## @RequestBody

- JSON 데이터를 원하는 타입의 객체로 변환해야 하는 경우에 사용
- HTTP 요청의 몸체(body)를 자바 객체로 받을 수 있게 해준다.
- 주로 비동기 처리 구현 시 @ResponseBody와 함께 자주 사용된다.

@RequestBody 어노테이션이 달린 메소드의 매개변수는 HTTP 요청 본문(request body)과 연결된다.  
HTTP 요청 본문은 HttpMessageConverter에 의해 처리되는데, 요청 시 전달받은 HTTP의 Content-Type 헤더에 선언된 콘텐츠 타입을 기준으로 메소드 인자값을 처리한다.

비동기 데이터 처리 방식 중의 하나인 JSON을 사용한다고 해보자.  
클라이언트에서 서버로 데이터를 요청하기 위해 JSON 데이터를 요청 본문에 담아 서버로 보내면 서버에서 @RequestBody 어노테이션을 사용해 HTTP 요청 본문에 담긴 값들을 자바 객체로 변환시켜 객체에 저장한다.  

## @ResponseBody

해당 어노테이션을 가진 메소드의 리턴값은 반드시 HTTP 요청 본문(response body)에 바인딩된다.  
HttpMessageConverter를 통해 요청 HTTP Content-Type 헤더에 선언된 데이터 형식에 맞게 메소드 리턴값을 반환한다.  

원한다면 @RequestMapping 어노테이션의 produces 또는 consumes 속성을 통해 메소드 선언부에서 명확하게 리턴값을 명시할 수 있다.  
이렇게 produces 또는 consumes 속성을 통해 리턴값을 명시하면 요청 헤더의 설정값과 메소드에 명시된 리턴값과 일치해야만 메소드 리턴값을 반환할 것이다.   
produces와 consumes 속성은 요청 헤더에서 참고하는 헤더가 다르다는 점을 주의하자. 

**produces 속성의 경우 Accept 헤더를 참고한다.**
Accept 헤더와 produces 속성에 명시된 데이터 타입이 일치해야만 메소드 리턴값을 반환한다.  

Accept 헤더는 Accept 헤더에 명시된 데이터 타입만 허용한다는 의미를 가진다.  
브라우저가 요청 메시지의 Accept 헤더 값을 application/json으로 설정했다면 클라이언트는 웹 서버에게 json 데이터만처리할 수 있으니 json 형식으로 응답하라고 말하는 것이다. 즉 **'Accept 헤더 보이지? 이 데이터 타입으로만 응답해야 한다!'** 라고 말하는 것이다. 

```
@RequestMapping(value = "/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public String test() {
	return "test";
}
```

**consumes 속성의 경우 Content-Type 헤더를 참고한다.**
Content-Type헤더와 consumes 속성에 명시된 데이터 타입이 일치해야만 메소드 리턴값을 반환한다.  

Content-Type 헤더는 클라이언트가 웹 서버에게 **'Content-Type 헤더에 명시된 데이터 타입은 이런 타입이니까 참고하라구!'** 라고 말하는 것과 같다. 즉 이런 타입의 데이터가 전송되니 참고해서 데이터 타입을 처리할 수 있도록 해라는 의미이다.  

```
@RequestMapping(value = "/test", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public String test() {
	return "test";
}
```

**Spring 4 버전부터 클래스에 @RestController 어노테이션이 @ResponseBody 어노테이션의 기능을 포괄하기 때문에 @RestController 어노테이션만 사용하면 된다. @Controller 어노테이션도 마찬가지.**

# 참고

* [docs.spring.io](https://docs.spring.io/spring-framework/docs/3.0.0.M3/reference/html/ch18s02.html)
* [cheersshennah](https://cheershennah.tistory.com/179)
* [webstone](https://webstone.tistory.com/66) 