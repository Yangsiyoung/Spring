9장
===
# 비동기 메시지 전송하기
애플리케이션 간 **응답을 기다리지 않고** **간접적**으로 메시지를 전송하는 방법  
애플리케이션 간 **결합도를 낮춰 줌**

## 응답을 기다리지 않고, 간접적?
특정 메시지를 전달하기 위해 직접 API 호출을 하는 것이 아니라  
메시지 큐 서버에 메지시를 전달하고 끝  
따라서 응답을 기다릴 필요가 없고, 메시지 큐 서버를 통해서 상호작용하므로 간접적이란 것  

# Artemis
ActiveMQ 를 새롭게 구현한 차세대 브로커  

## 의존성(Starter 의존성 O)
```groovy
implementation 'org.springframework.boot:spring-boot-starter-artemis'
```

## 의존성을 넣었으니, Properties 설정도 해야겠지?
|속성|설명|
|---|---|
|spring.artemis.host|브로커 호스트(artemis 떠있는 곳, Required)|
|spring.artemis.port|브로커 포트(Default: 61616, Required)|
|spring.artemis.user|브로커 사용자 ID(Optional) - Inmemory Broker 띄울 때 사용하는 듯|
|spring.artemis.password|브로커 사용자 password(Optional) - Inmemory Broker 띄울 때 사용하는 듯|

ex) 
```properties
spring.artemis.host=localhost
spring.artemis.port=61616
spring.artemis.embedded.enabled=false
spring.jms.template.default-destination=tacocloud.order.queue
#spring.artemis.user=
#spring.artemis.password=

login.jms.destination=ysjleader.login.request
```
## 메시지 보내기
```java
private final JmsTemplate jmsTemplate;

@Value("${login.jms.destination}")
private String messageDestination;


public void login(String id, String password) {
    Member member = findUserInfo(id);
    member.comparePassword(password);
    sendLoginEmail(member.getEmail());

}
```

1. JmsTemplate DI 받기  
2. JmsTemplate 을 이용해서 원하는 Destination 에 메시지 보내기

## 메시지 받기
메시지 보내기에서 원하는 메시징 큐 서버가 떠있는 곳으로 원하는 데이터를 원하는 목적지에 보낸 데이터를  
받기위해서는 뭐가 필요할까? 메지시를 보낼 때 사용했던 메시징 큐 서버와 연결해두고 목적지 데이터에 대한 수신작업에 대한 코드가 포인트겠지?

### 먼저 컨버터부터 만들기
Message 가 오갈때 객체를 Message 타입으로 변환해주고 Message 에 ID 를 부여해줘야한다.  
아래와 같이 작성하자  
```java
@Bean
public MessageConverter messageConverter() {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTypeIdPropertyName("messageID");
    Map<String, Class<?>> typeIdMappings = new HashMap<>();
    typeIdMappings.put("loginMessage", Member.class);
    converter.setTypeIdMappings(typeIdMappings);
    return converter;
}
```
### 방법 1) Pull
메시지를 받는쪽에서 자신이 메시지 받을 준비가 되었을때 직접 호출하여 받음으로써 부하가 걸리지 않도록 조절 가능  
호출 후 메시지가 들어올 때 까지 대기함  
```java
public void receiveMessage() {
    Member member = (Member)jmsTemplate.receiveAndConvert("ysjleader.login.request");
    log.info("########### SEND LOGIN EMAIL (PULLED) ###########");
    log.info(member.getEmail());
}
```

### 방법 2) Push
메시지가 수신 가능할 때 자동으로 호출되어 메시지가 넘어옴  
메시지가 너무 빨리 도착하면 과부하 걸릴 수 있음  
```java
@JmsListener(destination = "ysjleader.login.request")
public void sendLoginEmail(Member member) {
    log.info("########### SEND LOGIN EMAIL (PUSHED) ###########");
    log.info(member.getEmail());
}
```

# RabbitMQ
필요할 때 찾아서 공부해서 사용하자(Starter 의존성 O)  

# Kafka
필요할 때 찾아서 공부해서 사용하자(Starter 의존성 X)

