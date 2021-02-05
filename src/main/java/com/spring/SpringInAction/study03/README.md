6장
===

# REST 컨트롤러

## 스프링 MVC 의 HTTP 요청 처리 어노테이션
|어노테이션|HTTP Method|용도|
|-------|-----------|---|
|@GetMapping|HTTP GET 요청|리소스 읽기|
|@PostMapping|HTTP POST 요청|리소스 생성|
|@PutMapping|HTTP PUT 요청|리소스 수정(전체 수정)|
|@PatchMapping|HTTP PATCH 요청|리소스 수정(일부 수정)|
|@DeleteMapping|HTTP DELETE 요청|리소스 지우기|
|@RequestMapping|HTTP 요청처리, method 프로퍼티에 HTTP 메서드 지정||

## 살펴볼 HTTP 요청 처리 어노테이션 속성
### produces
Request 를 보낸 Client 의 헤더 속성 중 Accept 가 produces 에 설정된 값과 동일한 경우에만 처리 함.  

### consumes 
Request 를 보낸 Client 의 헤더 속성 중 Content-Type 이 consumes 에 설정된 값과 동일한 경우에만 처리 함.  

* EX
```java
@RequestMapping(value = "/study")
@RestController
public class StudyController {

    @GetMapping(value = "/produce", produces = "application/json")
    public String produceJson() {
        return "Client accept only application/json";
    }

    @GetMapping(value = "/produce", produces = "application/x-www-form-urlencoded")
    public String produceFormUrlEncoded() {
        return "Client accept only application/x-www-form-urlencoded";
    }

    @PostMapping(value = "/consume", consumes = "application/json")
    public String consumeJson() {
        return "Server accept only application/json";
    }

    @PostMapping(value = "/consume", consumes = "application/x-www-form-urlencoded")
    public String consumesFormUrlEncoded() {
        return "Server accept only application/x-www-form-urlencoded";
    }

    @PostMapping(value = "/produceAndConsume", produces = "application/json", consumes = "application/json")
    public String produceJsonAndConsumeJson() {
        return "Client accept only application/json And Server accept only application/json";
    }
}
```

## 하이퍼미디어
REST API 를 구현하는 다른 방법으로는 HATEOAS 라는 방법이 있다고 한다.  
기존 REST API 의 경우 정해진 API URL 을 클라이언트 측에서 저장해서 사용하는 방식이었다.  
여기서 API URL 이 변경되면 관련된 모든 클라이언트들도 API URL 값을 바꾸는 작업을 진행해야하는데  
어휴 생각만해도 아찔하다...  

그래서 HATEOAS 는 일단 원하는 리소스에 접근하는 최소한의 API URL 만 알고 요청하면,  
응답으로 해당 리소스에 대해서 처리 가능한 API URL 들을 알 수 있다.  
그냥 Response 에 API URL List 를 던져준다고 생각하면 편하다.  


