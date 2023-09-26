# Bulletin-Board
> Spring boot, JdbcTemplate, JPA 로 설계한 게시판 프로젝트입니다.

## 들어가며
### 1. 프로젝트 소개

처음 배운 Spring 과 JPA 에 익숙해지기 위해 시작한 게시판 프로젝트입니다.<br>
RestFul API, Spring data JPA, QueryDsl 로 리펙토링 한 버전은 링크를 확인해 주세요.
[RestFul API 버전(미완성)](https://github.com/Arachneee/Bulletin-Board-API)

### 2. 프로젝트 기능

- **게시글 -** CRUD 기능, 조회수, 검색, 정렬, 페이징, 검색조건 유지
- **회원 -** 회원가입 및 로그인, 회원가입 중복검사, 비회원 접근 제어
- **댓글 -** CRUD 기능, 정렬, 페이징, 공감수, 베스트 댓글, 중복 공감 제어

### 3. 사용 기술

#### 프레임워크 / 라이브러리
- Java 17
- SpringBoot 3.3.1
- JdbcTemplate
- JPA
- Thymeleaf

#### Build Tool
- Gradle 8.2.1

#### DataBase
- H2 2.1.214

### 4. 실행 화면
<details>
    <summary>게시글 관련</summary> 

**1. 게시글 목록**<br>
로그인한 사용자만 게시글을 볼 수 있다.
전체 목록을 페이징 처리하여 조회한다.
![게시글 목록](https://github.com/Arachneee/Bulletin-Board/assets/66822642/9e2da600-e153-4055-a290-6bfe974ac5e9)

**2. 게시글 등록**<br>
로그인 한 사용자만 새로운 글을 작성할 수 있다.
작성 후 목록 화면으로 redirect 한다.
![게시글 작성](https://github.com/Arachneee/Bulletin-Board/assets/66822642/38c24bc1-7a5d-4f4c-9f53-542c6700550d)

**3. 게시글 보기**<br>
본인이 작성한 글만 수정 및 삭제가 가능하다.
![게시글 보기](https://github.com/Arachneee/Bulletin-Board/assets/66822642/be18cc35-e77d-4e70-aef6-bbe11d036113)

**4. 게시글 수정 화면**<br>
제목과 내용만 수정할 수 있다.
![게시글 수정](https://github.com/Arachneee/Bulletin-Board/assets/66822642/52600fe2-ffdd-46ee-b0b2-5c3bbab5ef24)

**5. 게시글 검색 화면**<br>
키워드가 포함된 제목, 내용, 작성자로 검색할 수 있다.
작성일자, 조회순으로 정렬할 수 있다.
![게시글 검색](https://github.com/Arachneee/Bulletin-Board/assets/66822642/744368b9-5200-4d54-b74d-88a45958e921)


</details>
<br/> 

<details>
    <summary>회원 관련</summary>   

**1. 회원가입 화면**<br>
회원가입 시 유효성 검사 및 중복확인을 진행하며 완료시 회원 정보를 저장하고 로그인 화면으로 이동한다.
![회원가입](https://github.com/Arachneee/Bulletin-Board/assets/66822642/1e19c036-49d7-437a-ac66-0ccb0c69c91f)

**2. 로그인 화면**<br>
비로그인 상태로 페이지 접근시 로그인 화면으로 이동한다.
로그인 실패시 실패 메시지가 나오고, 로그인에 성공하면 기존에 접근하려고한 페이지로 이동한다.
![로그인](https://github.com/Arachneee/Bulletin-Board/assets/66822642/f0bee326-9a2d-4105-8098-119530299fff)



</details>
<br/>   

<details>
    <summary>댓글 관련</summary>   

**1. 댓글 작성 화면**<br>
댓글 작성시 현재 페이지를 redirect 한다.
![댓글작성](https://github.com/Arachneee/Bulletin-Board/assets/66822642/6c89970b-6140-4869-99e0-e42ba67cb527)


**2. 댓글 수정/삭제**<br>   
자신의 댓글만 수정/삭제할 수 없다.   
수정/삭제 완료 후 현재 페이지를 redirect 한다.
![댓글 수정](https://github.com/Arachneee/Bulletin-Board/assets/66822642/5897dfbf-c32d-4f6f-b597-e36cbae72f5a)


**4. 공감**<br>
댓글의 작성자와 이미 공감한 사람은 공감할 수 없다.
공감수가 가장 많은 댓글이 베스트 댓글로 선정된다. 
공감 후 현재 페이지를 redirect 한다.<br>
![댓글](https://github.com/Arachneee/Bulletin-Board/assets/66822642/3803b476-256a-42ea-ad2b-64f072030e83)


</details>
<br/>   

## 구조 및 설계
### 1. 패키지 구조
<details>
<summary>패키지 구조 보기</summary>  

```markdown
src.main.java.com.arachneee.bulletinboard:
│  BulletinboardApplication.java
│  
├─domain
│      Comment.java
│      CommentEmpathy.java
│      Member.java
│      Post.java
│      
├─repository
│  │  CommentEmpathyRepository.java
│  │  CommentRepository.java
│  │  MemberRepository.java
│  │  PostRepository.java
│  │  
│  ├─comment
│  │      JdbcCommentRepository.java
│  │      JpaCommentRepository.java
│  │      
│  ├─commentempathy
│  │      JpaCommentEmpathyRepository.java
│  │      
│  ├─member
│  │      JdbcMemberRepository.java
│  │      JpaMemberRepository.java
│  │      MemoryMemberRepository.java
│  │      
│  └─post
│          JdbcPostRepository.java
│          JpaPostRepository.java
│          MemoryPostRepository.java
│          
├─service
│      CommentService.java
│      LoginService.java
│      MemberService.java
│      PostService.java
│      
└─web
    │  WebConfig.java
    │  
    ├─argumentresolver
    │      Login.java
    │      LoginMemberArgumentResolver.java
    │      
    ├─controller
    │      CommentController.java
    │      HomeController.java
    │      LoginController.java
    │      MemberController.java
    │      PostController.java
    │      
    ├─dto
    │      CommentViewDto.java
    │      PostPreDto.java
    │      PostViewDto.java
    │      
    ├─form
    │      CommentAddForm.java
    │      LoginForm.java
    │      MemberAddForm.java
    │      PostAddForm.java
    │      PostEditForm.java
    │      
    ├─interceptor
    │      LoginCheckInterceptor.java
    │      
    ├─search
    │      CommentSearchCondition.java
    │      PostSearchCondition.java
    │      
    └─session
            SessionConst.java
```
 </details>   
 <br/> 

### 2. DB 설계
![ERD](https://github.com/Arachneee/Bulletin-Board/assets/66822642/c45c356a-e117-4f58-8210-993d68d0140a)

## 고민한 내용
- [검색조건을 유지할 수 있는 방법은 무엇일까?](https://velog.io/@jhg2819/Spring-%EA%B2%80%EC%83%89%EC%A1%B0%EA%B1%B4%EC%9D%84-%EC%9C%A0%EC%A7%80%ED%95%A0-%EC%88%98-%EC%9E%88%EB%8A%94-%EB%B0%A9%EB%B2%95%EC%9D%80-%EB%AC%B4%EC%97%87%EC%9D%BC%EA%B9%8C)
- [DTO와 FORM객체의 차이가 무엇이고 어느 레이어까지 사용해야 하는가?](https://velog.io/@jhg2819/Spring-DTO%EC%99%80-FORM%EA%B0%9D%EC%B2%B4%EC%9D%98-%EC%B0%A8%EC%9D%B4)
- [@ModelAttribute가 어떻게 동작하는가?](https://velog.io/@jhg2819/Spring-ModelAttribute%EB%8A%94-%EC%96%B4%EB%96%BB%EA%B2%8C-%EB%8F%99%EC%9E%91%ED%95%A0%EA%B9%8C)
- [조회쿼리 성능을 어떻게 최적화 할까?](https://velog.io/@jhg2819/JPA-%EC%A1%B0%ED%9A%8C%EC%BF%BC%EB%A6%AC-%EC%84%B1%EB%8A%A5%EC%9D%84-%EC%96%B4%EB%96%BB%EA%B2%8C-%EC%B5%9C%EC%A0%81%ED%99%94-%ED%95%A0%EA%B9%8C)


## 마치며

### 1. 프로젝트 보완사항
회원가입, 게시글, 댓글, 공감 기능을 Spring, JPA, Thymeleaf로 구현하였는데 화면에서 해야 할 일을 서버에서 처리하면서 
역할의 구분이 제대로 이루어지지 않았습니다. 그래서 Controller의 역할이 너무 많아졌습니다.<br>
따라서 이 프로젝트를 RestFul API로 전부 변경할 계획입니다.

또한 JdbcTemplate를 JPA로 변경하면서 Sql을 하드코딩하는 작업이 줄었으나 화면에 특화된 Repository를 설계하면서 DAO의 역할이 너무 많아졌음을 느꼈고 
동적 쿼리를 수행하는 코드의 가독성, Paging 처리의 번거로움 느꼈습니다.<br>
따라서 Spring Data Jpa와 QueryDsl로 변경할 계획입니다.


#### ToDo
- RestFul API 로 설계 변경
- Spring Data JPA, QueryDsl 로 변경
- Spring Sequrity 도입 보안 강화
- 로그처리 AOP 설계
- MySql 로 변경
- 대댓글 기능 추가
- 첨부파일 기능 추가
- MyPage 추가
- 쪽지보내기 기능 추가

### 2. 후기
독학으로 Spring을 처음 공부하고 혼자 프로젝트를 수행하면서 학습한 이론을 프로젝트로 적용하는 것의 중요성을 알았습니다.<br>
이론에서 배운 내용을 실제 프로젝트에서 적용하면서 Spring과 JPA에 익숙해 질 수 있었고 동작하는 과정을 직접 보면서 동작원리를 이해하기 쉬었습니다.  <br>
또한 여러 오류를 접하고 해결하면서 내부 동작 메커니즘을 이해하는 것의 중요성을 알 수 있었습니다.<br>
Spring과 JPA는 많은 기능이 추상화 되어있어 내부적으로 어떻게 동작할지 생각하며 코드를 짜는 것이 오류를 줄일 수 있는 방법임을 알게 되었습니다.<br>
프로젝트를 하며 고민한 내용을 정리하면서 내가 부족한 부분이 무엇이고 앞으로 어떤 학습을 해야할 지 알 수 있었습니다.<br>