# SpringAssignment_4&5

스프링 3주차 개인과제 (LEVEL 4&5)
## ERD
<img width="901" alt="image" src="https://user-images.githubusercontent.com/96409909/220825537-1cb6cee4-b8e6-4305-8b41-6f5bf4106fe9.png">

## 요구사항

**1. 회원 가입 API**

- username, password를 Client에서 전달받기
- username은  `최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)`로 구성되어야 한다.
- password는  `최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자`로 구성되어야 한다.
- DB에 중복된 username이 없다면 회원을 저장하고 Client 로 성공했다는 메시지, 상태코드 반환하기
- 회원 권한 부여하기 (ADMIN, USER) - ADMIN 회원은 모든 게시글, 댓글 수정 / 삭제 가능

**2. 로그인 API**

- username, password를 Client에서 전달받기
- DB에서 username을 사용하여 저장된 회원의 유무를 확인하고 있다면 password 비교하기
- 로그인 성공 시, 로그인에 성공한 유저의 정보와 JWT를 활용하여 토큰을 발급하고,
  발급한 토큰을 Header에 추가하고 성공했다는 메시지, 상태코드 와 함께 Client에 반환하기

**3. 댓글 작성 API**

- 토큰을 검사하여, 유효한 토큰일 경우에만 댓글 작성 가능
- 선택한 게시글의 DB 저장 유무를 확인하기
- 선택한 게시글이 있다면 댓글을 등록하고 등록된 댓글 반환하기

**4. 댓글 수정 API**

- 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 수정 가능
- 선택한 댓글의 DB 저장 유무를 확인하기
- 선택한 댓글이 있다면 댓글 수정하고 수정된 댓글 반환하기

**5. 댓글 삭제 API**

- 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 삭제 가능
- 선택한 댓글의 DB 저장 유무를 확인하기
- 선택한 댓글이 있다면 댓글 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기

**6. 예외 처리**

- 토큰이 필요한 API 요청에서 토큰을 전달하지 않았거나 정상 토큰이 아닐 때는 "토큰이 유효하지 않습니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
- 토큰이 있고, 유효한 토큰이지만 해당 사용자가 작성한 게시글/댓글이 아닌 경우에는 “작성자만 삭제/수정할 수 있습니다.”라는 에러메시지와 statusCode: 400을 Client에 반환하기
- DB에 이미 존재하는 username으로 회원가입을 요청한 경우 "중복된 username 입니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
- 로그인 시, 전달된 username과 password 중 맞지 않는 정보가 있다면 "회원을 찾을 수 없습니다."라는 에러메시지와 statusCode: 400을 Client에 반환하기

**7. 전체 게시글 목록 조회 API**

- 제목, 작성자명(username), 작성 내용, 작성 날짜를 조회하기
- 작성 날짜 기준 내림차순으로 정렬하기
- 각각의 게시글에 등록된 모든 댓글을 게시글과 같이 Client에 반환하기
- 댓글은 작성 날짜 기준 내림차순으로 정렬하기

**8. 게시글 작성 API**

- 토큰을 검사하여, 유효한 토큰일 경우에만 게시글 작성 가능
- 제목, 작성자명(username), 작성 내용을 저장하고
- 저장된 게시글을 Client 로 반환하기

**9. 선택한 게시글 조회 API**

- 선택한 게시글의 제목, 작성자명(username), 작성 날짜, 작성 내용을 조회하기
  (검색 기능이 아닙니다. 간단한 게시글 조회만 구현해주세요.)
- 선택한 게시글에 등록된 모든 댓글을 선택한 게시글과 같이 Client에 반환하기
- 댓글은 작성 날짜 기준 내림차순으로 정렬하기

**10. 선택한 게시글 수정 API**

- 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 수정 가능
- 제목, 작성 내용을 수정하고 수정된 게시글을 Client 로 반환하기

**11. 선택한 게시글 삭제 API**

- 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 삭제 가능
- 선택한 게시글을 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기

## API 명세표
https://www.notion.so/22ce847b64b940e9a6aaed736fb63c53?v=a8c179f2b29f4820a3b3b5f8c80e6efb&pvs=4
## 질문사항

#### Spring Security를 적용했을 때 장점.
  - 일단 Spring Security는 스프링 기반의 보안 프레임워크로, 애플리케이션에 보안 기능을 추가하는데 사용이 된다.
  1. 인증과 인가 기능을 제공한다.
  2. 보안 이슈들을 처리를 할 수 있다.
    - CSRF,클릭재킹과 같은 공격유형을 막기 위한 기능을 제공한다.
  3. 다양한 인증방식을 제공한다.
    - 우리는 이번에 JWT를 사용했지만, OAuth 과 같이 다른 다양한 인증방식을 지원한다.
  4. 유지보수성 향상
    - 일단 스프링 프레임워크로 통합되어있기 때문에 개발자들이 일관된 코드 작성이 가능 → 유지보수가 쉽다.

#### Spring Security를 사용하지 않는다면 어떻게 인증/인가를 효율적으로 처리하는 방법.
  - 일단은 Security를 사용하지 않는다면, Security를 사용하면서 단계적으로 있었던 부분들을 전부 직접 설계하는 방법으로 처리를 할 수 있을 것 같다.
  - 혹은 현재 사용하는 통신이 HTTP통신 이므로, HTTPS 를 사용해서 애플리케이션의 통신을 암호화하여서 데이터 유출을 방지할 수도 있을 것 같다.
#### AOP
  - 일단 OOP를 보완하기 위한 개념이라고 생각하면 된다. (다른부분이 아님)
  - 애플리케이션에서 공통적으로 발생하는 부가적인 작업자체를 “모듈화” 하여서 분리시키는 프로그래밍 기법이다.
  - 즉 핵심기능과 부가기능을 분리해서 애플리케이션의 유지보수성을 늘린다고 생각하면 된다. 

#### JWT를 사용하여 인증/인가를 구현 했을 때의 장/단점에 대해 숙련주차의 답변을 Upgrade 하여 작성.
  - 일단 RefreshToken 에 대한 적용을 하지 않았기 때문에 JWT를 사용하여 인증/인가를 구현 했을때의 장/단점에 대해서 말해보겠다.
  - 장점
    - 서버부하를 줄일 수 있다.
    - 확장성이 좋다.
    - 클라이언트측, 즉 프론트에서 JWT를 처리할 수 있다.
  - 단점
    - 토큰의 크기로 인한 네트워크 성능저하
    - 유효기간에 따른 토큰 무효화

#### 즉시로딩 / 지연로딩에 대한 설명.
- 즉시 로딩 (Eager Loading)
  - 모든 엔티티를 즉시 로드하여서 연관성을 처리하는 방법이다.
  - 객체가 필요하지 않은 경우에도 모든 엔티티를 로드하기 때문에, 시스템 전체 성능에 영향을 미칠 수 있다.
  - @ManyToOne, @OneToOne 과 같은 관계에서 사용이 된다.
- 지연로딩 (LazyLoading)
  - 필요할때 엔티티를 로드하는 방법이다.
  - 객체가 실제로 사용되는 경우에만 데이터베이스에서 해당 객체를 로드하므로, 성능에 이점을 가져올 수 있다.
  - @OneToMany, @ManyToMany 와 같은 관계에서 사용된다.
- 예시
  - 즉시 로딩은, 만약 User 엔티티와 Order 엔티티가 있을 때, User 엔티티에서 Order 엔티티와 관계를 맺는 필드를 즉시 로딩으로 설정하면, User를 조회할 때 연관된 Order 엔티티들도 함께 조회된다.
  - 지연 로딩은, User 엔티티에서 Order 엔티티와 관계를 맺은 필드를 지연로딩으로 설정한다고 가정하자.
    - User를 조회할때, 연관덴 Order 엔티티들은 먼저 조회되지 않는다.
    - User 객체에서 연관된 Order 엔티티를 사용할 떄 데이터베이스에서 조회되며, 이를 지연로딩이라고 한다.
- 특징
  - 즉시로딩 → 객체간의 관계가 매우 간단하고 성능 문제가 없을때 사용하면 좋다.
  - 지연로딩 → 복잡한 객체간의 관계를 가질때, 필요한 경우에만 로딩하도록 구현하면 좋다.


