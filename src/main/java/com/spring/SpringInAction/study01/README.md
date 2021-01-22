1 ~ 3장
=========
# 1장
기본적인 스프링 내용 + 세팅

## @SpringBootApplication
@SpringBootApplication 은 아래의 3개 어노테이션인 결합한 것  
* @SpringBootConfiguration
현재 클래스를 구성 클래스로 지정

* @EnableAutoConfiguration
스프링 부트 자동구성을 활성화

* @ComponentScan
컴포넌트 검색을 활성화 함

**실제로 @SpringBootApplication 어노테이션 대신 위의 어노테이션 3개로만 구성했을 때 정상동작을 확인 함.**  
**@SpringBootConfiguration 대신 @Configuration 으로만 구성했을 때에도 마찬가지로 정상동작하는 것을 확인 함.**  

# 2장
## 살펴볼 코드
* Ingredient.java
```
@Data
public class Ingredient {
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
```

책에서는 @RequiredArgsConstructor 붙어있는데 빼도된다.
이유는 @Data 어노테이션을 보자.
* Data.java
```
/**
 * Generates getters for all fields, a useful toString method, and hashCode and equals implementations that check
 * all non-transient fields. Will also generate setters for all non-final fields, as well as a constructor.
 * <p>
 * Equivalent to {@code @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode}.
 * <p>
 * Complete documentation is found at <a href="https://projectlombok.org/features/Data">the project lombok features page for &#64;Data</a>.
 * 
 * @see Getter
 * @see Setter
 * @see RequiredArgsConstructor
 * @see ToString
 * @see EqualsAndHashCode
 * @see lombok.Value
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Data {
	/**
	 * If you specify a static constructor name, then the generated constructor will be private, and
	 * instead a static factory method is created that other classes can use to create instances.
	 * We suggest the name: "of", like so:
	 * 
	 * <pre>
	 public @Data(staticConstructor = "of") class Point { final int x, y; }
	 * </pre>
	 * 
	 * Default: No static constructor, instead the normal constructor is public.
	 * 
	 * @return Name of static 'constructor' method to generate (blank = generate a normal constructor).
	 */
	String staticConstructor() default "";
}
```

**Generates getters for all fields, a useful toString method, and hashCode and equals implementations that check  
all non-transient fields. Will also generate setters for all non-final fields, as well as a constructor.**  
가릿?!  

## Controller 관련 코드에 등장한 @Slf4j
갓롬복...  
**Causes lombok to generate a logger field.** 로거 필드를 자동으로 맨들어쥼.  
```
@Slf4j
public class LogExample {
}
```
요렇게 붙이면 아래와 같다고 함.
```
public class LogExample {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogExample.class);
}
```

롬복은 그냥 컴파일타임에 코드를 Generate 할 뿐이니까...  
스프링 부트가 해당 로깅 라이브러리를 내장하고 있는 듯 한데 공식문서를 보쟈...  
**_By default, if you use the “Starters”, Logback is used for logging. Appropriate Logback routing is also included to ensure that dependent libraries that use Java Util Logging,  
Commons Logging, Log4J, or SLF4J all work correctly._**  

나는 의존성을 starter 를 사용하고 있으니 Log4J 도 SLF4J 를 자동으로 지원하는것이었군...  

## Spring MVC 요청 - 대응 어노테이션 두두등장!
* @RequestMapping - HTTP Method 통합적으로 처리 가능
* @GetMapping - GET 요청 처리 (조회)
* @PostMapping - POST 요청 처리 (생성)
* @PutMapping - PUT 요청 처리 (수정 - 전체 수정)
* @DeleteMapping - DELETE 요청 처리 (삭제)
* @PatchMapping - PATCH 요청 처리 (수정 - 일부 수정)  

**@GetMapping ~ @PatchMapping 요녀석들은 스프링 4.3부터 추가된 녀석이라고 함  
그로니 어디선가 플젝할떄 4.3 이상인지 확인해보고 쓸수있으면 쓰자**  

## 유효성 검사하기
일단 여기에서 나오는 어노테이션을 사용하기 위해선 아래의 의존성을 추가해죠야한다.  
```
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

몬가 기존에는 안넣어도 되던거였눈데... 왜 사라졋는고하니..  
[왜 없애버리셨나요? : 스프링 개발자 답변](https://github.com/spring-projects/spring-boot/issues/19550)  
기본으로 같이 제공해줬더니 마니 안쓰더란다...  

* @NotNull
Null 이면 안댐  
(**doc : The annotated element must not be {@code null}.**)  
* @Size
최대 최소 사이즈 정할 수 있음  
문자열만 가능한 것이 아니라 Map, Collection, 배열 가능  
(**doc : {@code CharSequence} (length of character sequence is evaluated)  
          {@code Collection} (collection size is evaluated)  
          {@code Map} (map size is evaluated)  
          Array (array length is evaluated)**)
* @NotEmpty
값이 널이거나 비면 안댐, 그리고 @Size 처럼, Map, Collection 등이 비었는지도 검사 가능
(**doc : The annotated element must not be {@code null} nor empty.**)

* @NotBlank
문자열이 널이거나 비면 안댐(@NotEmpty 랑 다른점은 @NotBlank 는 공백 하나만 있는 경우도 잡아줌, @NotBlank 는 빈값 자체를 검사하기때문에  
공백하나 넣어두면 값이 비어있지 않다고 판단해버린돠...)  
(**doc : Validate that the annotated string is not {@code null} or empty.**)

* @CreditCardNumber
룬 알고리즘 검사에 합격한 유효한 신용 카드 번호인지 확인해준다. 엄격하다 무섭다.

* @Pattern
정규표현식을 활용하여 좀 더 구체적으로 검사하고자 할 떄

* @Digits
숫자 자릿수 설정
integer = 정수 최대 자릿수
fraction = 소수의 경우 소수의 최대 자릿수

* 사용법
* Order.java (유효성 조건 설정)
```
@Data
public class Order {
    @NotNull(message = "ordererName is null")
    @Size(min=2, message = "Name is must be at least 2 characters long")
    private String ordererName;

    @NotEmpty(message = "menu is empty")
    private String menu;

    @NotBlank(message = "address is blank")
    private String address;

    @Size(min = 1, message = "Beverage is must be at least 1")
    private String[] beverages;

    @Digits(integer = 3, fraction = 0, message = "zipCode length is over than 3")
    private String zipCode;
}
```
* HelloController.java (유효성 검사)
```
@Slf4j
@RestController
public class HelloController {
    @GetMapping
    public String hello(@Valid Order order, Errors errors) {
        if (errors.hasErrors()) {
            return Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
        }
        return order.toString();
    }
}
```

* @Valid - 해당 객체의 유효성 검사를 수행하라고 알려줌
* Errors - 유효성 에러가 발생하면 여기에 저장 됨

## 뷰 컨트롤러 맨드는 법
```
@Configuration
public class ViewControllerConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/원하는 경로").setViewName("원하는 뷰 파일이름");
    }
}
```

