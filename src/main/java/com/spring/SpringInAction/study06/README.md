11장
======

# 스프링 WebFlux 사용하기

스프링 MVC 와 같은 서블릿 기반의 웹프레임워크는 **스레드 블로킹**과 **다중 스레드**로 수행된다.  
즉, 요청을 처리할 때 **스레드 풀**에서 **작업 스레드**를 가져와서 해당 요청을 처리하고,  
**작업 스레드**가 종료될 때까지 **요청 스레드**는 **블로킹**된다.  

그러나 **비동기 웹 프레임워크**(이벤트 루핑을 적용한 프레임워크)는 더 **적은 수**의 스레드로 더 많은 요청을 처리할 수 있기에 **경제적**이다.  
좀 더 이야기하자면 처리하는데 10초가 걸리는 작업이 있다면 기존에는 10초가 걸리는 작업이 끝날때 까지 스레드가 기다렸다면,  
이제는 10초 걸리는 작업을 실행하고 그에 대한 콜백을 등록해두고 다른 요청을 처리하다가 10초짜리 작업 끝나면 이벤트 받아서 처리한다는 것  

이게 뭔 뜻이냐면 기존에는 아래처럼 요청 하나가 들어오면 처리될때까지 다음 요청을 받을 수 없었지만  
<img src="https://user-images.githubusercontent.com/8858991/111618166-62f11180-8827-11eb-8b59-4cae5a47b493.png">

비동기 웹 프레임워크에서는 요청 들어오면 작업 실행하고 콜백 등록해두고 다음 요청을 처리할 수 있다는 뜻  
<img src="https://user-images.githubusercontent.com/8858991/111618193-697f8900-8827-11eb-9c8b-5da5d2357beb.png">

## 리액티브 컨트롤러 작성하기  
Flux 와 Mono 타입을 리턴 타입으로 가지도록 컨트롤러를 작성하면 된다.  
하지만 단순히 컨트롤러의 리턴타입 뿐만 아니라 이후에 사용되는(Service, Repository 등) 코드에서도 리액티브 형식을 따라야한다.  

### 단일 값 반환하기 
```java
@GetMapping("/mono")
public Mono<MonoDTO> getMono() {
    return Mono.just(new MonoDTO("data1", "data2"));
}
```

### 리액티브하게 입력 처리하기
```java
@PostMapping("/mono")
public Mono<MonoDTO> postMono(@RequestBody Mono<MonoDTO> monoDTO) {
    return monoDTO;
}
```

`~~음... Mono 자체에 기본 생성자가 없어서 Jackson 에서 컨버팅을 못해주고 있는데... 해결법 아시는분???~~  
==> ㅡㅡ tomcat 으로 돌리고있었네이거...`  

# 함수형 요청 핸들러 작성하기  
책에서 애노테이션 기반 프로그래밍에 대한 단점을 지적하며 이야기를 시작함(디버깅 힘들다, 무엇인지만 정의하고 어떻게 해야하는지 찾기 힘들다 등)  
(근데 애노테이션을 자바(스프링) 에서 채택할때 심플한 개발을 위해서였던걸로 기억하는데... 빼면 또 설정을 위한 코드가 많아지는게 아닌가... 조심스럽다...)  

## 스프링 함수형 프로그래밍 모델 기본 타입
* RequestPredicate : 요청 타입  
* RouterFunction : 어떻게 핸들러에게 전달되어야 하는가  
* ServerRequest : HTTP Request 이며 Header 와 Body 사용 가능  
* ServerResponse : HTTP Response 이며, Header 와 Body 를 포함  

### Hello Spring 을 응답해주는 간단한 예제
```java
@Bean
public RouterFunction<?> helloSpringRouter() {
    return route(GET("/functional/main"), new HandlerFunction<ServerResponse>() {
        @Override
        public Mono<ServerResponse> handle(ServerRequest request) {
            return ServerResponse.ok().body(Mono.just("Hello Spring"), String.class);

        }
    });
}
```

코드가 기니까 static method import 하고 lambda 를 사용하면 아래와 같이 사용가능하다.  
```java
@Bean
public RouterFunction<?> helloRouter() {
    return route(GET("/functional/hello"), request -> ServerResponse.ok().body(just("hello"), String.class));
}
```

나는 잘 모르니까 RouterFunction 의 route 부터 까보자  
```java
public static <T extends ServerResponse> RouterFunction<T> route(
			RequestPredicate predicate, HandlerFunction<T> handlerFunction) {

		return new DefaultRouterFunction<>(predicate, handlerFunction);
	}
```

RequestPredicate 타입과 HandlerFunction 타입을 인자로 받는다.  
RequestPredicate 둘다 @FunctionalInterface 어노테이션이 붙어있다.  
이말은 둘다 람다로 표현 가능하다는 것  

여기서 RequestPredicate 는 RequestPredicates 라는 RequestPredicate 타입을 리턴하는  
static method 들을 미리 만들어둔 abstract class 가 있다.  

그로니 RequestPredicates.GET() 과 같은 메서드를 사용한 것 이고,  
HandlerFunction 의 경우 아래와 같이 되어있는데  
```java
@FunctionalInterface
public interface HandlerFunction<T extends ServerResponse> {

	/**
	 * Handle the given request.
	 * @param request the request to handle
	 * @return the response
	 */
	Mono<T> handle(ServerRequest request);

}
```

실제 Request 를 어떻게 처리할지에(handle) 대한 부분이니까   
우리가 Controller 에서 Request 에 대해 서비스를 호출하거나 리턴 값을 지정하는 등을 구현하는 것과 같이  
우리가 원하는 대로 구현을 하면된다. (그러니까 미리 구현된 구현체가 없는 것 이겠지?)  

그리고 method chaining 도 가능
```java
@Bean
public RouterFunction<?> chainRouter() {
    return route(GET("/functional/chain1"), request -> ServerResponse.ok().body(just("chain1"), String.class))
            .andRoute(GET("/functional/chain2"), request -> ServerResponse.ok().body(just("chain2"), String.class));
}
```

#### Post 받기

* case 1
```java
@Bean
public RouterFunction<?> simplePostRouter() {
    return route(POST("/functional/user").and(contentType(MediaType.APPLICATION_JSON)),
                        request -> {
                            Mono<SignUpRequestDTO> signUpRequestDTOMono = request.bodyToMono(SignUpRequestDTO.class);
                            Mono<User> userMono = signUpRequestDTOMono.map((dto) -> User.builder().nickname(dto.getNickname()).email(dto.getEmail()).build());
                            return ServerResponse.ok().body(userMono, User.class);
                        });
}
```

Service 도 사용하면 아래와 같이  
```java
@Bean
public RouterFunction<?> simplePostRouter2() {
    return route(POST("/functional/user2").and(contentType(MediaType.APPLICATION_JSON)),
            request -> ServerResponse.ok().body(userService.signUp(request.bodyToMono(SignUpRequestDTO.class)), SignUpResponseDTO.class));
}
```

* 항상 주의할 점은 Controller, Service, Repository 다 리액티브하게 해야한다는 것

#### 라우터 로직을 분리해보자
```java
@Bean
public RouterFunction<?> mainRouterFunction() {
    //return route(GET(""), this::main).andRoute(POST(""), this::mainPost).andRoute(POST("/form-encoded").and(contentType(MediaType.APPLICATION_FORM_URLENCODED)), this::mainPostForm);
    return route()
                .GET("", MainRouterFunctions::main)
                .POST("", contentType(MediaType.APPLICATION_JSON), MainRouterFunctions::mainPost)
                .POST("", contentType(MediaType.APPLICATION_FORM_URLENCODED), MainRouterFunctions::mainPostForm)
                //.POST("", contentType(MediaType.MULTIPART_FORM_DATA), MainRouterFunctions::mainPostMultipartForm)
            .build();
}
```

```java
public class MainRouterFunctions {

    public static Mono<ServerResponse> main(ServerRequest serverRequest) {
        return ServerResponse.ok().body(Mono.just("Hello"), String.class);
    }

    public static Mono<ServerResponse> mainPost(ServerRequest serverRequest) {
        Mono<MainRequestDTO> mainRequestDTOMono = serverRequest.bodyToMono(MainRequestDTO.class);
        Mono<MainResponseDTO> mainResponseDTOMono = mainRequestDTOMono.map((mainRequestDTO -> new MainResponseDTO(mainRequestDTO.getParam1(), mainRequestDTO.getParam2())));
        return ServerResponse.ok().body(mainResponseDTOMono, MainRequestDTO.class);
    }

    public static Mono<ServerResponse> mainPostForm(ServerRequest serverRequest) {
        Mono<MainRequestDTO> mainRequestDTOMono = serverRequest.formData().flatMap((stringStringMultiValueMap -> Mono.just(new MainRequestDTO(stringStringMultiValueMap.getFirst("param1"), stringStringMultiValueMap.getFirst("param2")))));
        Mono<MainResponseDTO> mainResponseDTOMono = mainRequestDTOMono.map((mainRequestDTO -> new MainResponseDTO(mainRequestDTO.getParam1(), mainRequestDTO.getParam2())));
        return ServerResponse.ok().body(mainResponseDTOMono, MainRequestDTO.class);
    }

    public static Mono<ServerResponse> mainPostMultipartForm(ServerRequest serverRequest) {
        return null;
    }
}
```
