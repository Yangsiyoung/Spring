12장
====
앞서 보았던 컨트롤러의 경우 같이 작동되는 다른 컴포넌트들도 블로킹이 없어야 진정한 블로킹이 없는  
컨트롤러가 될 수 있다.  

# 스프링 데이터의 리액티브 개념 이해하기
Spring Data 가 Reactive Repository 를 제공하기 시작하였고,  
카산드라, MongoDB, 카우치베이스, Redis 에 해당한다.  

관계형 DB 나 JPA 는 Reactive Repository 가 지원되지 않는다고 한다.  
이유는 Spring Data JPA 로 Reactive Programming 을 지원하려면  
DB 와 JDBC 드라이버 역시 블로킹되지 않는 Reactive Model 을 지원해야하기 때문.  

## 스프링 데이터 리액티브 개요
Repository 가 도메인 타입이나 컬렉션 대신 Mono 나 Flux 를 인자로 받거나 반환하는 메서드를 갖는다는 것이다  
근데 우리는 보통 JPA 를 사용하고있고, RDBMS 를 사용하고있을 것 이기에 아래로 넘어가자.  

## 리액티브와 리액티브가 아닌 타입 간의 변환  
우선 Reactive Programming 의 장점은 Client - Database 까지 Reactive Model 을 가질 때 발휘된다.  
근데 뭐 어쩔수없으니까 우리는 뭘해야하나면 DB 와 통신하고 넘어온 도메인을 Mono 나 Flux 로 최대한 빨리 변환 시켜줘야한다.  
그래야 나머지 부분들에 대해서는 Reactive 하게 처리할 수 있기 때문이다.  

아래를 참고하여 정리를 해보자.
<img src="https://user-images.githubusercontent.com/8858991/112272822-032fb600-8cc0-11eb-83a0-a8b736860191.png">

전체가 다 Non Blocking 여야 하지만 최대한 할수 빨간색 부분만 어쩔수없이 Blocking 으로 두고 나머지는 Mono, Flux 를 활용해서  
Reactive 하게 Programming 을 해보자는 것 이다.  

그래서 Repository 에서 조회해온 후 Mono 나 Flux 로 변환하면 된다.  

* Mono 로 변환  
`Domain 객체를 Mono 로 변환하고, 해당 도메인 Mono 를 다른 Mono 타입으로 변환해야할 때`
```java
@PostMapping
public Mono<CreateStudentResponseDTO> create(@RequestBody CreateStudentRequestDTO createStudentRequestDTO) {
    Student student = Student.builder().name(createStudentRequestDTO.getName()).grade(createStudentRequestDTO.getGrade()).build();
    Student savedStudent = studentRepository.save(student);
    Mono<Student> studentMono = Mono.just(savedStudent);
    Mono<CreateStudentResponseDTO> result = studentMono.map(CreateStudentResponseDTO::fromDomain);
    return result;
}
```
* Flux 로 변환  
`Domain List 들을 Flux 로 변환하고, 해당 도메인 Flux 를 다른 Flux 타입으로 변환해야할 때`
```java
@GetMapping
public Flux<StudentResponseDTO> list() {
    List<Student> studentList = studentRepository.findAll();
    Flux<Student> studentFlux = Flux.fromIterable(studentList);
    Flux<StudentResponseDTO> result = studentFlux.map(StudentResponseDTO::fromDomain);
    // flux to Iterable OR List
    // Iterable<StudentResponseDTO> studentResponseDTOIterable = result.toIterable();
    // List<StudentResponseDTO> studentResponseDTOList = result.toStream().collect(Collectors.toList());
    return result;
}
```  

