# Bulletin-Board
> Spring boot, JdbcTemplate, JPA 로 설계한 게시판 프로젝트입니다.

## 들어가며
### 1. 프로젝트 소개

처음 배운 Spring 과 Jpa 에 익숙해지기 위해 시작한 첫 프로젝트입니다.
RestFul API, Spring data JPA로 변경한 버전은 아래 링크를 확인해주세요.

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

**1. 게시글 목록**
![게시글 목록](https://github.com/Arachneee/Bulletin-Board/assets/66822642/9e2da600-e153-4055-a290-6bfe974ac5e9)
로그인한 사용자만 게시글을 볼 수 있다.
전체 목록을 페이징 처리하여 조회한다.

**2. 게시글 등록**

로그인 한 사용자만 새로운 글을 작성할 수 있다.
작성 후 목록 화면으로 redirect 한다.

**3. 게시글 보기**
본인이 작성한 글만 수정 및 삭제가 가능하다.

**4. 게시글 수정 화면**
제목과 내용만 수정할 수 있다.

**5. 게시글 검색 화면**
검색 키워드에 포함된 글을 페이징하여 보여준다.

</details>
<br/> 

<details>
    <summary>회원 관련</summary>   

**1. 회원가입 화면**   
회원가입 시 유효성 검사 및 중복확인을 진행하며 완료시 회원 정보를 저장하고 로그인 화면으로 이동한다.

**2. 로그인 화면**   
로그인 실패시 어떤 이유로 실패 했는지 메시지가 나오고, 로그인에 성공하면 게시글 전체 리스트 화면으로 redirect 한다.


</details>
<br/>   

<details>
    <summary>댓글 관련</summary>   

**1. 댓글 작성 화면**   

댓글 작성시 현재 페이지를 redirect 한다.

**2. 댓글 수정**   
다른 사용자는 다른 사람의 댓글을 수정/삭제할 수 없다.   
수정은 댓글 작성자만이 할 수 있다. 수정 완료 후 현재 페이지를 redirect 한다.

**3. 댓글 삭제**   
삭제 또한 댓글 작성자만이 할 수 있다. 삭제 후 현재 페이지를 redirect 한다.

**4. 공감**
댓글의 작성자와 이미 공감한 사람은 공감할 수 없다.
공감수가 가장 많은 댓글이 베스트 댓글로 선정된다. 공감 후 현재 페이지를 redirect 한다.

</details>
<br/>   

## 구조 및 설계
### 1. 패키지 구조
<details>

<summary>패키지 구조 보기</summary>  

 </details>   
 <br/> 

### 2. DB 설계

## 마치며

### 1. 프로젝트 보완사항
RestFul API로 설계하지 못하였고 

## TODOLIST
### RestFul API 로 설계 변경
### Spring Data JPA 로 변경, Page 처리
### QueryDsl 로 동적 쿼리 변경
### Spring Sequrity 추가
### 로그처리 AOP 설계
### MySql 로 변경
### 대댓글 추가
### MyPage 추가
### 쪽지보내기 추가

