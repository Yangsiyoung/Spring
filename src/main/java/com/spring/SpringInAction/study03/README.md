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

* EX)
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

일단 이것을 사용하기 위해서 의존성을 추가해야하는데 Spring boot starter 단에서 지원을 한다.  

```groovy
implementation 'org.springframework.boot:spring-boot-starter-hateoas'
```

우선 EntityModel, CollectionModel 2가지 기본 타입이 있다.  
우리가 DTO 로 리턴하는 것이 EntityModel 로 감싸지고, 이 EntityModel 을 리스트로 표현하고자하면 CollectionModel 를 사용하면 된다.  
* EX)
```
// 단일
EntityModel<Post>

// 컬렉션
CollectionModel<EntityModel<Post>>
```

근데 여기서 잠깐!!! 책에서 나온거랑 다르다고 놀라지말자... 버전업이되면서 기본 타입의 변경이 생겼다 아래를 참고하자.  

* ResourceSupport -> RepresentationModel  
* Resource -> EntityModel  
* Resources -> CollectionModel  
* PagedResources -> PageModel  
* ResourceAssembler -> RepresentationModelAssembler  
* ResourceAssemblerSupport -> RepresentationModelAssemblerSupport

그냥 외우기는 힘드니까 이름을 지은 이유를 생각해보자.  
자원에 대해서 표현을 하는데 단일 자원의 경우 EntityModel, 다수의 자원의 경우 CollectionModel 이라는 이름을 통해서  
구분을 하고자 했던 것 같다.  

그리고 RepresentationModel 의 경우 모델을 표현하고자하는 방식에 대한 클래스임을 나타내고자 한 것 같고  
RepresentationModelAssemblerSupport 는 내가 정의한 모델 표현 방식(RepresentationModel)으로 만들어주는 어셈블러 같은 느낌이다.  

그렇담 내가 Post 라는 객체를 통해 리소스를 표현하고자할때  
1. RepresentationModel 을 정의해서 Post 라는 객체가 어떤 모습으로 변환될지 정의하고  
2. RepresentationModelAssemblerSupport 를 정의해서 RepresentationModel 을 조립하고  
3. 단일(EntityModel) 혹은 복수의 자원(CollectionModel)을 컨트롤러에서 리턴하면된다. 

그럼 이것을 기반으로 코드를 짜보면 아래와 같다.  

* Post.java
```java
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Post {
    private Long postId;
    private String title;
    private String contents;
}
```

* PostRepresentationModel.java
```java
@Getter
public class PostRepresentationModel extends RepresentationModel<PostRepresentationModel> {
    private Long postId;

    private String title;

    private String contents;

    public PostRepresentationModel(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.contents = post.getContents();
    }
}
```

* PostRepresentationModelAssemblerSupport.java
```java
public class PostRepresentationModelAssemblerSupport extends RepresentationModelAssemblerSupport<Post, PostRepresentationModel> {

    /**
     * Creates a new {@link RepresentationModelAssemblerSupport} using the given controller class and resource type.
     *
     * @param controllerClass must not be {@literal null}.
     * @param resourceType    must not be {@literal null}.
     */
    public PostRepresentationModelAssemblerSupport(Class<?> controllerClass, Class<PostRepresentationModel> resourceType) {
        super(PostController.class, PostRepresentationModel.class);
    }

    public PostRepresentationModelAssemblerSupport() {
        super(PostController.class, PostRepresentationModel.class);
    }

    @Override
    protected PostRepresentationModel instantiateModel(Post entity) {
        return new PostRepresentationModel(entity);
    }

    @Override
    public PostRepresentationModel toModel(Post entity) {
        return createModelWithId(entity.getPostId(), entity);
    }

    public List<PostRepresentationModel> toModels(List<Post> entities) {
        return entities.stream().map((entity) -> createModelWithId(entity.getPostId(), entity)).collect(Collectors.toList());
    }
}
```

그리고 만약 Post 속에 Comment 라는 댓글이 있고 그 댓글에 대한 리소스 표현도 같이 해줘야할땐 어떻게 해야할까???  
간단하다, Comment 에 대해서 RepresentationModel 과 RepresentationModelAssemblerSupport 를 구현하고  
PostRepresentationModel 에 Comment 에 대한 RepresentationModel 을 List 로 갖도록 추가해주면 된다.

* Post.java
```java
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Post {
    private Long postId;
    private String title;
    private String contents;
    private List<Comment> comments;
}
```

* Comment.java
```java
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment {
    private String commentId;
    private String message;
}
```
* PostRepresentationModel.java
```java
@Getter
public class PostRepresentationModel extends RepresentationModel<PostRepresentationModel> {
    private Long postId;

    private String title;

    private String contents;

    private List<CommentRepresentationModel> commments = new ArrayList<>();

    public PostRepresentationModel(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.commments = new CommentRepresentationModelAssemblerSupport().toModels(post.getComments());
    }
}

```

* CommentRepresentationModel.java
```java
@Getter
public class CommentRepresentationModel extends RepresentationModel<CommentRepresentationModel> {
    private String commentId;
    private String message;

    public CommentRepresentationModel(Comment entity) {
        this.commentId = entity.getCommentId();
        this.message = entity.getMessage();
    }
}
```

* CommentRepresentationModelAssemblerSupport.java
```java
public class CommentRepresentationModelAssemblerSupport extends RepresentationModelAssemblerSupport<Comment, CommentRepresentationModel> {
    /**
     * Creates a new {@link RepresentationModelAssemblerSupport} using the given controller class and resource type.
     *
     * @param controllerClass must not be {@literal null}.
     * @param resourceType    must not be {@literal null}.
     */
    public CommentRepresentationModelAssemblerSupport(Class<?> controllerClass, Class<CommentRepresentationModel> resourceType) {
        super(controllerClass, resourceType);
    }

    public CommentRepresentationModelAssemblerSupport() {
        super(CommentController.class, CommentRepresentationModel.class);
    }

    @Override
    protected CommentRepresentationModel instantiateModel(Comment entity) {
        return new CommentRepresentationModel(entity);
    }

    @Override
    public CommentRepresentationModel toModel(Comment entity) {
        return createModelWithId(entity.getCommentId(), entity);
    }

    public List<CommentRepresentationModel> toModels(List<Comment> entities) {
        return entities.stream().map(this::toModel).collect(Collectors.toList());
    }
}
```

* PostRepresentationModelAssemblerSupport.java
```java
public class PostRepresentationModelAssemblerSupport extends RepresentationModelAssemblerSupport<Post, PostRepresentationModel> {

    /**
     * Creates a new {@link RepresentationModelAssemblerSupport} using the given controller class and resource type.
     *
     * @param controllerClass must not be {@literal null}.
     * @param resourceType    must not be {@literal null}.
     */
    public PostRepresentationModelAssemblerSupport(Class<?> controllerClass, Class<PostRepresentationModel> resourceType) {
        super(PostController.class, PostRepresentationModel.class);
    }

    public PostRepresentationModelAssemblerSupport() {
        super(PostController.class, PostRepresentationModel.class);
    }

    @Override
    protected PostRepresentationModel instantiateModel(Post entity) {
        return new PostRepresentationModel(entity);
    }

    @Override
    public PostRepresentationModel toModel(Post entity) {
        return createModelWithId(entity.getPostId(), entity);
    }

    public List<PostRepresentationModel> toModels(List<Post> entities) {
        return entities.stream().map((entity) -> createModelWithId(entity.getPostId(), entity)).collect(Collectors.toList());
    }
}
```

* Return Data EX)
```json
{
    "postId": 1,
    "title": "title1",
    "contents": "content1",
    "comments": [
        {
            "commentId": "post1comment1",
            "message": "message",
            "_links": {
                "self": {
                    "href": "http://localhost:8080/comment/post1comment1"
                }
            }
        },
        {
            "commentId": "post1comment2",
            "message": "message",
            "_links": {
                "self": {
                    "href": "http://localhost:8080/comment/post1comment2"
                }
            }
        }
    ],
    "_links": {
        "self": {
            "href": "http://localhost:8080/post/1"
        }
    }
}
```

```json
{
    "_embedded": {
        "memberPosts": [
            {
                "postId": 1,
                "title": "title1",
                "contents": "content1",
                "comments": [
                    {
                        "commentId": "post1comment1",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post1comment1"
                            }
                        }
                    },
                    {
                        "commentId": "post1comment2",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post1comment2"
                            }
                        }
                    }
                ],
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/post/1"
                    }
                }
            },
            {
                "postId": 2,
                "title": "title2",
                "contents": "content2",
                "comments": [
                    {
                        "commentId": "post2comment1",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post2comment1"
                            }
                        }
                    },
                    {
                        "commentId": "post2comment2",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post2comment2"
                            }
                        }
                    }
                ],
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/post/2"
                    }
                }
            },
            {
                "postId": 3,
                "title": "title3",
                "contents": "content3",
                "comments": [
                    {
                        "commentId": "post3comment1",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post3comment1"
                            }
                        }
                    },
                    {
                        "commentId": "post3comment2",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post3comment2"
                            }
                        }
                    }
                ],
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/post/3"
                    }
                }
            },
            {
                "postId": 4,
                "title": "title4",
                "contents": "content4",
                "comments": [
                    {
                        "commentId": "post4comment1",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post4comment1"
                            }
                        }
                    },
                    {
                        "commentId": "post4comment2",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post4comment2"
                            }
                        }
                    }
                ],
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/post/4"
                    }
                }
            },
            {
                "postId": 5,
                "title": "title5",
                "contents": "content5",
                "comments": [
                    {
                        "commentId": "post5comment1",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post5comment1"
                            }
                        }
                    },
                    {
                        "commentId": "post5comment2",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post5comment2"
                            }
                        }
                    }
                ],
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/post/5"
                    }
                }
            },
            {
                "postId": 6,
                "title": "title6",
                "contents": "content6",
                "comments": [
                    {
                        "commentId": "post6comment1",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post6comment1"
                            }
                        }
                    },
                    {
                        "commentId": "post6comment2",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post6comment2"
                            }
                        }
                    }
                ],
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/post/6"
                    }
                }
            },
            {
                "postId": 7,
                "title": "title7",
                "contents": "content7",
                "comments": [
                    {
                        "commentId": "post7comment1",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post7comment1"
                            }
                        }
                    },
                    {
                        "commentId": "post7comment2",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post7comment2"
                            }
                        }
                    }
                ],
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/post/7"
                    }
                }
            },
            {
                "postId": 8,
                "title": "title8",
                "contents": "content8",
                "comments": [
                    {
                        "commentId": "post8comment1",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post8comment1"
                            }
                        }
                    },
                    {
                        "commentId": "post8comment2",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post8comment2"
                            }
                        }
                    }
                ],
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/post/8"
                    }
                }
            },
            {
                "postId": 9,
                "title": "title9",
                "contents": "content9",
                "comments": [
                    {
                        "commentId": "post9comment1",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post9comment1"
                            }
                        }
                    },
                    {
                        "commentId": "post9comment2",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post9comment2"
                            }
                        }
                    }
                ],
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/post/9"
                    }
                }
            },
            {
                "postId": 10,
                "title": "title10",
                "contents": "content10",
                "comments": [
                    {
                        "commentId": "post10comment1",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post10comment1"
                            }
                        }
                    },
                    {
                        "commentId": "post10comment2",
                        "message": "message",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/comment/post10comment2"
                            }
                        }
                    }
                ],
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/post/10"
                    }
                }
            }
        ]
    }
}
```

## 데이터 기반 서비스 활성화
데이터 기반 REST API 를 자동으로 노출시켜주며, @RestController 로 지정한 클래스를 사용하지 않음...  

아.. 이건 좀 생각해봐야할듯... 공부하긴해야하는데... 아직 마음의 준비가 안되었다.  
