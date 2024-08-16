# 프리온보딩 백엔드 인턴십 선발 과제

## 목차 

* [개요](#개요)
* [기술 스택 및 실행 환경](#기술-스택-및-실행-환경)
* [디렉터리 구조](#디렉터리-구조)
* [ERD](#erd)
* [API 명세서](#api-명세서)
  
## 개요

- 본 서비스는 기업의 채용을 위한 웹 서비스 입니다.
- 회사는 채용공고를 생성하고, 이에 사용자는 지원합니다.

## 기술 스택 및 실행 환경

### 기술 스택

- Java 17.0
- Spring boot 3.3.2
- MySql 8.0

### 실행 환경
1. application.properties 설정
```
spring.application.name=wanted-pre-onboarding-backend

spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```
2. WantedApplication 실행

## 디렉터리 구조

<details>
<summary><strong>구조 설명</strong></summary>
<div markdown="1">
<br>
디렉터리 구조는 크게 domain과 global로 구분합니다. 
  
- domain : 서비스의 핵심 비즈니스 로직 코드가 도메인별로 구현되어 있습니다.
- global : 핵심 비즈니스 로직에 종속적이지 않고 전역에서 사용할 수 있는 리스폰스 형식, 예외 처리 등을 관리합니다.

### domain

구현해야할 비즈니스 로직에 맞춰 도메인을 구분하였습니다. 도메인의 종류는 아래와 같습니다.
- company : 회사
- job : 채용공고
- job_application_history : 채용공고 지원내역
- user : 사용자

도메인의 하위 패키지는 동일하게 구성되어 있습니다.
- controller : MVC의 controller 레벨의 역할을 수행합니다. 사용자 요청이 진입하는 지점이며 적합한 처리 로직에 분기를 해주는 기능을 수행합니다.
- dto : 해당 도메인에서 비즈니스 로직을 수행할 때 사용하는 dto를 관리합니다. 사용자 요청으로 들어오는 request, 요청에 대한 결과를 담은 response 객체를 주로 관리합니다.
- entity : 해당 도메인에서 핵심으로 사용하는 DB 테이블과 매칭되는 클래스를 관리합니다. entity는 매칭되는 테이블과 동일한 property를 가지고 있습니다.
- repository : Spring Data JPA 구현에 필요한 repository 인터페이스를 관리합니다.
- service : 해당 도메인에서 사용자 요청을 적합한 비즈니스 로직을 통해 필요한 데이터로 가공하는 역할을 수행합니다.
- exception : 해당 도메인에서 발생 가능한 사용자 정의 예외 클래스를 관리합니다.

### global

global은 domain이 공통으로 사용하는 error, util을 관리합니다.

- error : 예외 처리를 관리합니다. GlobalExceptionHandler로 발생한 예외를 domain이 직접 처리하지 않고 전역에서 처리할 수 있도록 구현했습니다.
- util : ApiUtils로 API에서 공통으로 사용하는 response 형식을 관리합니다.
</details>

<details>
<summary><strong>구조도</strong></summary>
<div markdown="1">
  
```
src
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           └── wanted
│   │               ├── WantedApplication.java
│   │               ├── domain
│   │               │   ├── company
│   │               │   │   ├── entity
│   │               │   │   │   └── Company.java
│   │               │   │   └── repository
│   │               │   │       └── CompanyRepository.java
│   │               │   ├── job
│   │               │   │   ├── controller
│   │               │   │   │   └── JobController.java
│   │               │   │   ├── dto
│   │               │   │   │   ├── ApplyJobRequestDto.java
│   │               │   │   │   ├── ApplyJobResponseDto.java
│   │               │   │   │   ├── JobDetailResponseDto.java
│   │               │   │   │   ├── JobInfoResponseDto.java
│   │               │   │   │   ├── JobResponseDto.java
│   │               │   │   │   ├── RegisterJobRequestDto.java
│   │               │   │   │   └── UpdateJobRequestDto.java
│   │               │   │   ├── entity
│   │               │   │   │   └── Job.java
│   │               │   │   ├── exception
│   │               │   │   │   ├── CompanyNotFoundException.java
│   │               │   │   │   ├── JobApplicationDuplicatedException.java
│   │               │   │   │   ├── JobNotFoundException.java
│   │               │   │   │   └── UserNotFoundException.java
│   │               │   │   ├── repository
│   │               │   │   │   └── JobRepository.java
│   │               │   │   └── service
│   │               │   │       └── JobService.java
│   │               │   ├── job_application_history
│   │               │   │   ├── entity
│   │               │   │   │   └── JobApplicationHistory.java
│   │               │   │   ├── repository
│   │               │   │   │   └── JobApplicaionHistoryRepository.java
│   │               │   │   └── service
│   │               │   │       └── JobApplicationHistoryService.java
│   │               │   └── user
│   │               │       ├── entity
│   │               │       │   └── User.java
│   │               │       └── repository
│   │               │           └── UserRepository.java
│   │               └── global
│   │                   ├── error
│   │                   │   └── GlobalExceptionHandler.java
│   │                   └── util
│   │                       └── ApiUtils.java
│   └── resources
│       ├── application.properties
│       ├── static
│       └── templates
└── test
    └── java
        └── com
            └── example
                └── wanted
                    ├── WantedApplicationTests.java
                    └── domain
                        ├── company
                        │   └── repository
                        │       └── CompanyRepositoryTest.java
                        ├── job
                        │   ├── controller
                        │   │   └── JobControllerTest.java
                        │   ├── repository
                        │   │   └── JobRepositoryTest.java
                        │   └── service
                        │       └── JobServiceTest.java
                        └── job_application_history
                            ├── repository
                            │   └── JobApplicaionHistoryRepositoryTest.java
                            └── service
                                └── JobApplicationHistoryServiceTest.java
```

</details>

## ERD

<img src = "https://github.com/user-attachments/assets/760523f0-d45e-4a23-b88e-f1d915b48d61" width="600">

## API 명세서

| No | Title      | Method   | URL                     | 
|----|------------|----------|-------------------------|
| 1  | 채용공고 등록    | `POST`   | `/api/jobs`             | 
| 2  | 채용공고 수정    | `PATCH`  | `/api/jobs/:jobId`      |   
| 3  | 채용공고 삭제    | `DELETE` | `/api/jobs/:jobId`      |    
| 4  | 채용공고 목록 조회 | `GET`    | `/api/jobs`             |    
| 5  | 채용공고 검색    | `GET`    | `/api/jobs?serch={검색어}` |    
| 6  | 채용공고 상세 조회 | `GET`    | `/api/jobs/:jobId`      |     
| 7  | 채용공고 지원    | `POST`   | `/api/jobs/apply`       | 

<details>
<summary><strong>1. 채용공고 등록</strong></summary>
<div markdown="1">
<br>

회사는 아래 데이터와 같이 채용공고를 등록합니다.

#### URL
```
POST /api/jobs
```

#### Request Body
```json
{
    "company_id": 1,
    "position": "백엔드 주니어 개발자",
    "reward": 1000000,
    "detail": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
    "skill": "Python"
}
```
#### Success Response
- Status Code : 201
```json
{
  "success": true,
  "response": {
    "id": 1,
    "company_id": 1,
    "position": "백엔드 주니어 개발자",
    "reward": 1000000,
    "detail": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
    "skill": "Python",
    "created_at": "2024-07-31 11:27:16",
    "updated_at": "2024-07-31 11:27:16",
  },
  "error": null
}
```

#### Fail Response

1. 존재하지 않은 회사 아이디일 경우
- Status Code : 404
```json
{
    "success": false,
    "response" : null,
    "error": {
        "message": "회사가 존재하지 않습니다.",
        "http_status": "NOT_FOUND"
    }
}
```

2. 유효하지 않은 필드값일 경우
- Status Code : 400

```json
{
  "success": false,
  "response": null,
  "error": {
    "message": {
      "reward": "보상금은 0이상의 숫자여야 합니다.",
      "company_id": "회사 아이디는 필수 요소입니다." or "회사 아이디는 1이상의 숫자여야 합니다.",
      "position": "포지션은 필수 요소입니다."
    },
    "http_status": "BAD_REQUEST"
  }
}
```
</details>

<details>
<summary><strong>2. 채용공고 수정</strong></summary>
<div markdown="1">
<br>

회사는 아래 데이터와 같이 채용공고를 수정합니다.
(회사 id 이외 모두 수정 가능합니다.)

#### URL
```
PATCH /api/jobs/:jobId
```

#### Request Body
```json
{
    "position": "백엔드 주니어 개발자",
    "reward": 1500000, # 변경됨
    "detail": "원티드랩에서 백엔드 주니어 개발자를 '적극' 채용합니다. 자격요건은..", # 변경됨
    "skill": "Python"
}
```
or
```json
{
    "position": "백엔드 주니어 개발자",
    "reward": 1000000,
    "detail": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
    "skill": "Django" # 변경됨
}
```

#### Success Response

- Status Code : 200
```json
{
    "success": true,
    "response": {
        "id": 1,
        "company_id": 1,
        "position": "백엔드 주니어 개발자",
        "reward": 1500000,
        "detail": "원티드랩에서 백엔드 주니어 개발자를 '적극' 채용합니다. 자격요건은..",
        "skill": "Python",
        "created_at": "2024-07-31 13:04:01",
        "updated_at": "2024-07-31 19:05:36",
    },
    "error": null
}
```

#### Fail Response

1. 존재하지 않은 채용공고 아이디일 경우
- Status Code : 404
```json
{
    "success": false,
    "response": null,
    "error": {
        "message": "0번 채용공고가 존재하지 않습니다.",
        "http_status": "NOT_FOUND"
    }
}
```

2. 유효하지 않은 필드값일 경우
- Status Code : 400
```json
{
  "success": false,
  "response": null,
  "error": {
    "message": {
      "reward": "보상금은 0이상의 숫자여야 합니다.",
      "position": "포지션은 필수 요소입니다."
    },
    "http_status": "BAD_REQUEST"
  }
}
```

</details>

<details>
<summary><strong>3. 채용공고 삭제</strong></summary>
<div markdown="1">
<br>

DB에서 삭제됩니다.

#### URL
```
DELETE /api/jobs/:jobId
```

#### Success Response
- Status Code : 200
```json
{
  "success": true,
  "response": "1번 채용공고가 삭제되었습니다.",
  "error": null
}
```

#### Fail Response
1. 존재하지 않은 채용공고 아이디일 경우
- Status Code : 404
```json
{
    "success": false,
    "response": null,
    "error": {
        "message": "0번 채용공고가 존재하지 않습니다.",
        "http_status": "NOT_FOUND"
    }
}
```

</details>

<details>
<summary><strong>4. 채용공고 목록 조회</strong></summary>
<div markdown="1">
<br>

사용자는 채용공고 목록을 아래와 같이 확인할 수 있습니다.

#### URL
```
get /api/jobs
```

#### Success Response
- Status Code : 200
```json
{
    "success": true,
    "response": [
        {
            "job_id": 1,
            "company_name": "원티드",
            "nation": "한국",
            "area": "서울",
            "position": "백엔드 주니어 개발자",
            "reward": 1500000,
            "skill": "Python"
        },
        {
            "job_id": 2,
            "company_name": "네이버",
            "nation": "한국",
            "area": "판교",
            "position": "Django 백엔드 개발자",
            "reward": 1000000,
            "skill": "Django"
        }
    ],
    "error": null
}
```

* 채용공고 목록이 비었을 경우
```json
{
    "success": true,
    "response": [],
    "error": null
}
```

</details>

<details>
<summary><strong>5. 채용공고 검색</strong></summary>
<div markdown="1">
<br>

사용자는 (회사명, 국가, 지역, 채용 포지션, 사용 기술에) 검색어가 포함된 채용공고 목록을 아래와 같이 확인할 수 있습니다.

### URL
```
/api/jobs?serch=Django
```

### Success Response
- Status Code : 200
```json
{
  "success": true,
  "response": [
    {
      "job_id": 11,
      "company_name": "네이버",
      "nation": "한국",
      "area": "판교",
      "position": "Django 백엔드 개발자",
      "reward": 1000000,
      "skill": "Python"
    },
    {
      "job_id": 12,
      "company_name": "원티드",
      "nation": "한국",
      "area": "서울",
      "position": "Python 백엔드 개발자",
      "reward": 1000000,
      "skill": "Django"
    },
    {
      "job_id": 17,
      "company_name": "구글",
      "nation": "미국",
      "area": "뉴욕",
      "position": "Django 백엔드 개발자",
      "reward": 1000000,
      "skill": "Django"
    }
  ],
  "error": null
}
```

* 검색한 채용공고 목록이 비었을 경우
```json
{
    "success": true,
    "response": [],
    "error": null
}
```

</details>

<details>
<summary><strong>6. 채용공고 상세 조회</strong></summary>
<div markdown="1">
<br>

사용자는 채용상세 페이지를 아래와 같이 확인할 수 있습니다.
- “채용내용”이 추가적으로 담겨있음.
- 해당 회사가 올린 다른 채용공고 가 추가적으로 포함됩니다

### URL
```
GET /api/jobs/:jobId
```

### Success Response

```json
{
  "success": true,
  "response": {
    "job_id": 10,
    "company_name": "원티드",
    "nation": "한국",
    "area": "서울",
    "position": "백엔드 주니어 개발자",
    "reward": 1500000,
    "detail": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
    "skill": "Python",
    "other_job_ids": [
      12,
      13,
      14
    ]
  },
  "error": null
}
```

#### Fail Response
1. 존재하지 않은 채용공고 아이디일 경우
- Status Code : 404
```json
{
    "success": false,
    "response": null,
    "error": {
        "message": "0번 채용공고가 존재하지 않습니다.",
        "http_status": "NOT_FOUND"
    }
}
```

</details>

<details>
<summary><strong>7. 채용공고 지원</strong></summary>
<div markdown="1">
<br>

사용자는 채용공고에 아래와 같이 지원합니다.
사용자는 1회만 지원 가능합니다.

### URL
```
POST /api/jobs/apply
```

### Request Body
```json
{
  "job_id": 1,
  "user_id": 2
}
```

### Success Response
- Status Code : 201
```json
{
  "success": true,
  "response": {
    "id": 2,
    "job_id": 1,
    "user_id": 2,
    "created_at": "2024-08-05 14:54:22"
  },
  "error": null
}
```

### Fail Response

1. 유효하지 않은 필드값일 경우
- Status Code : 400
```json
{
    "success": false,
    "response": null,
    "error": {
        "message": {
            "job_id": "채용공고 아이디는 1이상의 숫자여야 합니다.",
            "user_id": "유저 아이디는 1이상의 숫자여야 합니다."
        },
        "http_status": "BAD_REQUEST"
    }
}
```

2. 존재하지 않은 채용공고 아이디일 경우
- Status Code : 404
```json
{
    "success": false,
    "response": null,
    "error": {
        "message": "1번 채용공고가 존재하지 않습니다.",
        "http_status": "NOT_FOUND"
    }
}
```

3. 존재하지 않은 유저 아이디일 경우
- Status Code : 404
```json
{
    "success": false,
    "response": null,
    "error": {
        "message": "2번 유저가 존재하지 않습니다.",
        "http_status": "NOT_FOUND"
    }
}
```

4. 이미 지원한 경우
- Status Code : 409
```json
{
    "success": false,
    "response": null,
    "error": {
        "message": "이미 지원한 채용공고 입니다.",
        "http_status": "CONFILCT"
    }
}
```

</details>


