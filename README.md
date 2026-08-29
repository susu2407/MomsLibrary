# 엄마의 도서관 (Mom's Library)

가족의 책·논문·아티클을 등록하고, 카테고리와 태그로 분류·검색하는 개인 자료 관리 시스템입니다.
단순 CRUD를 넘어, 삭제 시 연결 데이터를 어떻게 처리할지 같은 실질적인 도메인 규칙을 설계하는 데 집중했습니다.

---

## 화면

<!-- TODO: 스크린샷 삽입 -->
| 목록 | 상세 |
|---|---|
| ![목록 화면](./docs/screenshots/list.png) | ![상세 화면](./docs/screenshots/detail.png) |

| 자료 등록/수정 | 카테고리/태그 관리 |
|---|---|
| ![자료 폼](./docs/screenshots/form.png) | ![카테고리 관리](./docs/screenshots/category.png) |

---

## 기술 스택

| 기술                              | 선택 이유 |
|---------------------------------|---|
| Java 21,<br> Spring Boot 3.5.14 | 학습한 스택을 실전 프로젝트에 적용 |
| Spring Data JPA                 | 객체 중심으로 1:N, N:M 관계를 다루는 방식을 익히기 위해 |
| SQLite                          | 별도 DB 서버 없이 파일 하나로 동작하는 개인용 도구에 적합 |
| Thymeleaf                       | Spring MVC 서버사이드 렌더링 방식을 익히기 위해 |
| JUnit5 + Mockito                | 테스트 가능한 코드 작성 연습 |

---

## ERD

![ERD](./docs/screenshots/erd.png)

Entity 설계 기준입니다. SQLite 특성상 실제 DB에서는 외래키 제약이 강제되지 않아,
DB 도구에서 조인 관계가 자동으로 표시되지는 않습니다.

---

## 핵심 설계 판단

| 항목                     | 판단 | 이유 |
|------------------------|---|---|
| 카테고리 삭제 <br>(하위 있음)    | 삭제 금지 (`CategoryHasChildrenException`) | 같은 이름의 하위 카테고리가 서로 다른 상위에 속할 수 있어, 상위 삭제 시 구조가 애매해짐 |
| 카테고리 삭제 <br>(연결 자료 있음) | "기타" 카테고리로 자동 이동 | 자료가 미분류로 남는 것보다 안전하게 이동 |
| 태그 삭제                  | 연결된 DocumentTag 선삭제 후 태그 삭제 | 참조 무결성 유지, 고아 데이터 방지 |
| 예외 처리                  | `@ControllerAdvice` 전역 처리 | 컨트롤러마다 try-catch 반복 방지 |
| Entity id              | setter 제거, DB 자동 생성값으로만 취급 | 애플리케이션 코드에서 실수로 id를 덮어쓰는 것 방지 |
| 컨트롤러 분리                | CategoryController / TagController | 화면은 공유해도 책임은 분리 |

---

## 테스트

JUnit5 + Mockito, 총 11개. <br>
작성 중 실제 버그 1건 발견·수정<br>
(`assignTags()`에서 파라미터로 받은 원본 리스트를 잘못 참조하던 문제).

---

## 실행 방법

```bash
./gradlew bootRun
```

실행 후 `http://localhost:8080/documents` 접속

---

## 더 알아보기

프로젝트 기획 과정, 상세 도메인 규칙 흐름, 기능 구현 현황, Git 브랜치 전략,
향후 개선 검토 목록은 [기획 및 설계 문서](https://docs.google.com/document/d/1KKAAIxbI4kPSwmBg4zxTeplgTGF2HaBwx5nZPiwPllw/edit?usp=sharing)에서 확인할 수 있습니다.