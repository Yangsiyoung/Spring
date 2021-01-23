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
갓롬복...(Kotlin 에서 롬복 사용하면 안대기때문에 잘알아둬야함)  
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
문자열이 널이거나 비면 안댐(@NotEmpty 랑 다른점은 @NotBlank 는 공백 하나만 있는 경우도 잡아줌,  
@NotEmpty 는 비어있는 상태인지만 검사하기때문에 공백하나 넣어두면 값이 비어있지 않다고 판단해버린돠...)  
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

# 3장
## JdbcTemplate
* 아래 의존성 추가하고  
```
implementation 'org.springframework.boot:spring-boot-starter-jdbc'
```  
* @Repository 어노테이션으로 스테레오 타입 지정해주고

* JdbcTemplate 주입 받아서 쿼리 작성하고, ResultSet 원하는 객체로 직접 매핑해야함  

이 과정이 너무 귀찮아서 나는 사용하지 않는다.  

## 살펴볼 코드
* @SessionAttributes
  Model 에 전역적으로 사용할 수 있는 변수를 생성해 줌
```
// 세션에 order 라는 이름의 변수를 추가할 예정
// 이 어노테이션이 붙은 컨트롤러에서 Model 에 @SessionAttributes("변수명") 에 있는 변수명과 같은 이름으로
// Model 에 값을 넣는다면(ex. @SessionAttributes("변수명1"), model.addAttribute("변수명1", 값))
// 세션에 해당 변수명으로 값이 저장 됨.
// 여러화면에 걸쳐서 값을 입력받아야 완성되는 데이터의 경우에 사용하면 좋다고 한다. (ex. 자소서 입력할때 인적사항 -> 자기소개 -> 최종제출 할때 그느낌쿠)
// 추가적으로 이 어노테이션을 사용하고 모델이 값을 넣었으면, 다른 컨트롤러에서 이 어노테이션을 사용하여 모델에 넣은 값의 변수명과 같은 변수명을 사용하면
// 모델엔 앞서 이 어노테이션을 사용하여 넣었던 값이 담겨있다.
// 세션에 값이 담기고, 이걸 통해서 담긴 세션 값들은 모델에도 들어가서 모델 전역변수가 되어 값을 어디서든 사용할 수 있다고 생각하면 편할 듯.
@SessionAttributes("sessionData")
@Controller
public class HiController {

    @GetMapping("/session/{data}")
    public String setSessionAttribute(@PathVariable("data") String data, Model model) {
        // @SessionAttributes 에 선언한 변수와 같은 이름으로 모델이 값 넣기
        model.addAttribute("sessionData", data);
        return "/hi";
    }
    @GetMapping("/hi/session/data")
    public String getSessionAttributes() {
        // @SessionAttributes 에 선언한 변수 이름으로 자동으로 모델에 값 들어가있음
        return "/hi_data";
    }
}
```
  
* @ModelAttribute
  Model 에 직접 addAttribute 안해줘도 되도록 함
```
@Controller
public class Hi2Controller {
    @GetMapping("/hi2")
    public String getSessionData(@ModelAttribute(name = "ingredient") Ingredient ingredient) {
        // @ModelAttribute 덕분에 직접적으로 model.addAttribute 하지 않아도 됨
        return "/hi2_data";
    }
}
```

## JPA
* 의존성 추가
```
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
```

JPA 이외에도 MongoDB, Neo4, Redis, Cassandra 등을 지원하고 있음.  

## 살펴볼 코드
```
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Entity
public class Student {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private final String name;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
```

JPA 사용할때 리플렉션으로 값 넣어주고 있어서 기본 생성자 Private 이면 안되는줄...  
리플렉션으로 생성자 접근자도 설정할 수 있긴하지만... 귀찮아서 지원안하는줄 알았는데 이렇게까지 디테일하게 지원할줄이야...  

* CrudRepository
JpaRepository 를 그냥 사용해왔었는데  
JpaRepository 의 조상이 CrudRepository 니까  
특별히 JpaRepository 의 특수한 기능을 사용하는게 아니고 간단한 CRUD 할거면 CrudRepository 사용하자.  

 
