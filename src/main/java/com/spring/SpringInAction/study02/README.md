4 ~ 5 장
===========
# 스프링 시큐리티
## 의존성
* build.gradle  
```
implementation 'org.springframework.boot:spring-boot-starter-security'
testImplementation 'org.springframework.security:spring-security-test'
```

## 의존성만 추가해도 기본 보안 설정 제공
1. 모든 HTTP 요청은 인증되어야 함
2. 역할이나 권한이 없음
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
