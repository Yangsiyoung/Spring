4 ~ 5 장
===========
# 4장 - 스프링 시큐리티
## 의존성
* build.gradle  
```
implementation 'org.springframework.boot:spring-boot-starter-security'
testImplementation 'org.springframework.security:spring-security-test'
```

## 의존성만 추가해도 기본 보안 설정 제공
1. 모든 HTTP 요청은 인증되어야 함
2. 역할(Role)이나 권한(Authority)이 없음
3. 로그인 페이지가 따로없음 -> formLogin() 설정하면 기본 로그인 페이지 제공
4. 스프링 시큐리티의 HTTP 기본 인증을 사용해서 인증 -> httpBasic() 설정했을 경우
5. 사용자는 user 라는 이름의 사용자 하나만 있으며, 비밀번호는 암호화해서 만들어준다.    
(서비스 실행 시 콘솔을 확인해보라)

## 스프링 시큐리티 구성 예시
스프링 시큐리티 기본 설정을 사용하는 경우는 거의 없을 것 이고, 내가 원하는 대로 설정하기 위해서는  
@EnableWebSecurity 어노테이션을 붙이고 WebSecurityConfigurerAdapter 를 상속하는 클래스를 만들어  
원하는대로 설정하자.  

```
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/member/**", "/h2-console/**", "/error")
                .permitAll()
                .antMatchers("/info/**")
                .hasRole("USER")
                .anyRequest()
                .authenticated();

        http.formLogin();
        http.csrf().ignoringAntMatchers("/h2-console/**"); // 없으면 h2 console 로그인 안됨
        // <iframe> 혹은 <object> 에서 우리 사이트 페이지를 렌더링 할 수 있는지 여부
        // 나는 h2 console 때문에 동일 사이트에서만 허용하는 정책을 넣어둠
        http.headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));

    }
}
```

## hasRole("역할")
역할에 ROLE_USER 와 같이 ROLE_ 을 붙여서 hasRole 에 넘기는 경우가 있는데 아래 코드를 보고  
앞으로는 그러지 않기로 약속해!  
```
/**
 * Shortcut for specifying URLs require a particular role. If you do not want to
 * have "ROLE_" automatically inserted see {@link #hasAuthority(String)}.
 * @param role the role to require (i.e. USER, ADMIN, etc). Note, it should not
 * start with "ROLE_" as this is automatically inserted.
 * @return the {@link ExpressionUrlAuthorizationConfigurer} for further
 * customization
 */
public ExpressionInterceptUrlRegistry hasRole(String role) {
    return access(ExpressionUrlAuthorizationConfigurer.hasRole(role));
}
```

```
private static String hasRole(String role) {
    Assert.notNull(role, "role cannot be null");
    Assert.isTrue(!role.startsWith("ROLE_"),
            () -> "role should not start with 'ROLE_' since it is automatically inserted. Got '" + role + "'");
    return "hasRole('ROLE_" + role + "')";
}
```

## 자 기본적인 인증과 인가를 제공해주는데, 로그인을 DB 와 연결해보자  
로그인 페이지에서 로그인 시 우리가 원하는 DB 에서 유저 데이터를 가져와서 비교 후 로그인 처리를 해야한다.  
그러기 위해서는 UserDetailsService 를 구현한 Service 코드가 필요하다.  

```
@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /*
     * 비밀번호 다룰떄는 PasswordEncoder 가 꼭 있어야한다.
     * 원하는 인코더를 Bean 등록해두면 된다.
     */
    private final PasswordEncoder passwordEncoder;


    @PostConstruct
    public void init() {
        memberRepository.save(Member.builder().nickname("라디").username("ysjleader").password(passwordEncoder.encode("1234")).role("user").build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not Found"));
        return User.builder().username(findMember.getUsername()).password(findMember.getPassword()).roles(findMember.getRole()).build();
    }
}
```

```
// @Configuration 파일 어딘가 등록해두자...
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

Password Encoder 및 자세한 내용은 [이 곳](https://github.com/Yangsiyoung/SpringSecurity/blob/master/docs/Authentication.md#10-bcryptpasswordencoder) 참조  


그나저나 책에서  
```
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(위에서 만든 UserDetailsService 인터페이스 구현한 빈(서비스));
}
```
요런식으로 설정하는데, 나는 여태 안해도 잘되었는데... 이거 꼭 해야하나??? 나중에 찾아보기~~  

## 스프링 시큐리티 구성 메서드 정리
스프링 시큐리티 구성 클래스 내 configure 메서드 오버라이드 시 사용가능한 메서드  

|메서드|역할|
|----|---|
|access(String)|인자로 전달된 SpEL 표현식이 true 면 허용|
|anonymous()|익명 유저 접근 하용|
|authenticated()|인증된 유저만 접근 허용|
|denyAll()|모든 접근 거부|
|fullyAuthenticated()| 익명 유저가 아니고 remember-me 가 아닌 인증된 유저만 접근 허용|
|hasAnyAuthority(String...)|인자로 전달된 권한 중 하나라도 포함하면 접근 허용|
|hasAnyRole(String...)|인자로 전달된 역할 중 하나라도 포함하면 접근 허용|
|hasAuthority(String)|인자로 전달된 권한에 대해서만 허용|
|hasRole(String)|인자로 전달된 역할에 대해서만 허용|
|not()|뒤에 나오는 설정을 반대로 설정|
|permitAll()|모든 접근 허용|
|rememberMe()|이전 로그인 정보를 쿠키나 데이터베이스에 저장 후 일정 기간 내 재접했을때<br/> remember-me 로 인증하는데 remember-me에 대한 접근만 허용|

## 스프링 시큐리티 SpEL
|표현식|설명|
|----|---|
|authentication|유저의 인증 객체|
|denyAll|항상 false|
|hasAnyRole(역할, 역할 ...)| 전달된 역할 중 하나라도 해당된다면 true|
|hasRole(역할)|전달된 역할에 해당된다면 true|
|hasIpAddress(IP 주소)|지정된 IP 주소로부터 요청이 온 것이면 true|
|isAnonymous()|익명 유저이면 true|
|isAuthenticated()|인증된 유저이면 true|
|isFullyAuthenticated()|익명 유저가 아니고 remember-me 가 아닌 인증된 유저면 true|
|isRememberMe()|remember-me 로 인증된 유저면 true|
|principal|유저의 Principal 객체|

* **SpEL 은 섞어서 쓸 수 있으므로 좀 더 다양한 조건으로 판단이 가능함**  

## CSRF 방어
CSRF 란 유저가 원하지 않았음에도 해당 유저인척 요청을 하는 요청 위조이다.  
예를들어 은행 사이트 A에 유저가 로그인을 한 상태에서 다른 악의적인 사이트 B 를 방문하였을때,  
악의적인 사이트 B에서 자동으로 은행 사이트 A로 송금 요청 폼을 전송하게되고,  
이미 은행 사이트 A 에 유저는 로그인한 상태이니 마치 해당 유저가 직접 송금 요청을 하는 것과 같게 판단을 하여  
송금이 되는 것으로 이해하면 된다.  

방어법은 Referrer 를 체크하거나, CSRF 토큰을 사용하면 되는데 CSRF 토큰 방식을 스프링 시큐리티에서  
자동으로 제공해준다.  

기본 설정에서 CSRF 가 활성화 되어있으므로 form 으로 데이터를 제출하거나, POST 등으로 데이터를 전송할때  
CSRF 에 의하여 막힐 것 이다.  

만약 특정 경로만 CSRF 설정을 풀고싶다면 @EnableWebSecurity 구성 클래스에서  
```
http.csrf().ignoringAntMatchers("/h2-console/**");
```
요런식으로 추가하자.  

**스프링 시큐리티 기본 csrf 토큰 변수 명은 _csrf 이다**
타임리프를 쓰면 자동으로 csrf 히든 태그를 만들어주지만 안쓴다면 아래와 같은 태그를 폼에 추가하면 된다.  
```
// JSP 코드
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
```

아니면 \<form:form> 태그로 form 을 만들면된다.(스프링 MVC 에서 제공하는 JSP 태그)  

## 사용자 인지
### 로긴한자 누구인가 어뜨케 확인하는가
1. Principal 타입의 인자를 컨트롤러 메서드에서 받는다.
```
@GetMapping("/principal")
public String principal(Principal principal) {
    return principal.getName();
}
```
2. Authentication 타입의 인자를 컨트롤러 메서드에서 받는다.
```
@GetMapping("/authentication")
public String authentication(Authentication authentication) {
    return authentication.getName() + " " + authentication.getAuthorities();
}
```
3. SecurityContextHolder 를 통해서
```
@GetMapping("/securityContext")
public String securityContext() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getName() + " " + authentication.getAuthorities();
}
```
4. @AuthenticationPrincipal 어노테이션을 메서드 파라미터에 지정
```
@GetMapping("/authenticationPrincipal")
public String authenticationPrincipal(@AuthenticationPrincipal Member user) {
    return user.getUsername() + " " + user.getAuthorities();
}
```

**여기서 @AuthenticationPrincipal 지정하고 받는 인자 타입은 아무거나해도 되는게 아니라  
UserDetailsService 에서 리턴한 객체타입이어야 가능**

```
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not Found"));
    return findMember;
}
```

**여기서 만약에 Member 타입이 아니라 User 타입 리턴하면 형변환 안되어서 컨트롤러에서 받을때 안된다잉!!!**


# 5장 - 구성 속성 사용
스프링은 환경 추상화를 통해서 모든 속성을 한 곳에서 관리하고 있다.  
그래서 한 군데로 모은 후 빈에게 이 정보들을 전달하고 있다.  

## @ConfigurationProperties
사용 예시
1. application.properties 파일에 원하는 값 정의
```
ysjleader.hello=hello my name is siyoung
```

2. 사용하고자 하는 빈에서 설정 및 사용
```
@ConfigurationProperties(prefix = "ysjleader")
@RequiredArgsConstructor
@RestController
public class MainController {

    private String hello; // ysjleader.hello

    public void setHello(String hello) {
        this.hello = hello;
    }

    @GetMapping("/properties")
    public String properties() {
        return hello;
    }
}
```

여기서 포인트는 @ConfigurationProperties(prefix = "") 를 통해 사용할 값들의 prefix 를 지정하고  
빈에서 setter 를 선언해줘야한다는 점이다.  

내 생각은 빈 생성과 초기화 단계 중 초기화 단계에서 setter 를 통해 필요한 값들을 빈 생성 후에 주입해주는 듯 하다.  

그럼 이제 이런 값들을 들고있는 빈을 따로 선언하고, 사용하고자 하는 빈에서 DI 를 받아서 진행해보도록 하자.
0. 테스트를 위해 프로퍼티를 하나 더 추가했다.
```
ysjleader.hello=hello my name is 
ysjleader.name=siyoung
```

1. 사용할 값들을 들고있는 빈 선언
```
@ConfigurationProperties(prefix = "ysjleader")
@Getter
@Setter
@Component
public class MyConfData {
    private String hello; // ysjleader.hello
    private String name; // ysjleader.name
}
```

2. 방금 선언한 빈을 DI 받아서 사용
```
@RequiredArgsConstructor
@RestController
public class MainController {

    private final MyConfData myConfData;

    @GetMapping("/properties")
    public String properties() {
        return myConfData.getHello() + " " + myConfData.getName();
    }
}
```
내생각엔 요렇게 따로 빼서 관리하는게 좋을 듯하다

## 이상은 없지만 IDE 경고 문구를 없애려면?
아래 의존성 추가
```
annotationProcessor "org.springframework.boot:spring-boot-configuration-processor"
```

## 프로파일 속성 관리
로컬 개발 - 테스트 - 운영 서버 환경이 각각 달라서 프로퍼티 설정이 다를 텐데  
각각의 프로퍼티 파일을 만들어서 관리를 하여야하고, 규칙이 있다.  
**규칙 : application-{프로파일 이름}.yml 혹은 application-{프로파일 이름}.properties**  

### 사용법
local, test, prod 요 세가지 타입의 프로파일이 있어서,  
application-local.properties, application-test.properties, application-prod.properties  
요렇게 만들었다고 가정하자.  

그럼 실행시에 VM 옵션을 주거나 스프링 기본 프로퍼티(application.properties) 에 설정을 해두어야하는데 값은 아래와 같다.  
```
spring.profiles.active=원하는 프로파일(ex. local, test, prod ...)
```

## @Profile
빈위에 @Profile("프로파일") 을 설정하면 해당 프로파일일 때만 빈이 생성된다.  
반대로 @Profile("!프로파일") 을 설정하면 해당 프로파일이 아닐때만 빈이 생성된다.  
