# Pickypal

emart24 입출고 재고관리 프로그램 (WMS)

## 📌Project overview
### Repository structure
- `/Pickypal`: GUI 프로젝트 폴더
- `/api`: 백엔드 프로젝트 폴더
- `/cli`: CLI 프로젝트 폴더

### Tech stack
![pickypal-tech-stack](https://github.com/user-attachments/assets/a2401050-31fb-40b8-a812-e7d14c264f08)

### ER Diagram
![pickypal-erd](https://github.com/user-attachments/assets/ac5dfd7d-53c0-42dd-9391-e709d5187a2a)

## 📌How to start API server

1. `api` 프로젝트의 `/resources/application.yml`에 하단의 property를 기입

```yml
secret:
  firebase:
    api-key: {Firebase web API key}
  jwt:
    salt: {Your custom JWT salt}
```

2. `application.yml`의 JDBC url을 확인하고 해당 DB를 생성

3. 하단의 `Test data setting`을 참고하여 테스트 데이터 설정

4. `ApiApplication.main()` 실행

## 📌Test data setting

다음의 API를 순차적으로 호출하여 테스트 데이터를 세팅합니다.

### 1️⃣ 상품 크롤링 및 DB 적재

| Method | Endpoint |
|:-----:|:-----------------------------:|
| `GET` | `localhost:8080/crawler/save` |

### 2️⃣ 본사 재고 99개 적재
| Method | Endpoint |
|:-----:|:-----------------------------:|
| `GET` | `localhost:8080/backdoor/head/full-stock` |

### 3️⃣ B00006(강남리테일점) 재고 0~20개 적재
| Method | Endpoint |
|:-----:|:-----------------------------:|
| `GET` | `localhost:8080/backdoor/branch/random-stock?branch_id=B00006` |

### 4️⃣ 납품업체가 null인 stock 제거 (data cleanse)
| Method | Endpoint |
|:-----:|:-----------------------------:|
| `GET` | `localhost:8080/backdoor/cleanse` |

## 📌Git convention

### Branch
- `main`: version release (protected from force push)
- `develop`: integrate implementation (protected from force push)
- `feature`: implementation (related to GitHub issues)

<img src="https://github.com/user-attachments/assets/d20a15a2-f867-46cc-874d-0ceb5bdd3fc0" alt="pickypal-git-branch-convention" width="500" height="auto">

### Commit message

`{type}{(scope)}: {subject} {(issue #)}` 포맷으로 작성

- `type`은 소문자로 작성
- `scope`은 생략 가능
- `subject`는 명령조로 작성
- `subject`는 대문자로 시작
- `subject`의 첫 단어(ex. Add, Modify) 이후에는 반드시 **client인지 backend인지 명시**
    - 원래 repo 분리하는데 Trello 칸반 연동 단위가 repo라서 어쩔 수 없었음