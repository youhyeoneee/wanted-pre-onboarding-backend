# 프리온보딩 백엔드 인턴십 선발 과제

## API 명세서 

| No | Title      | Method   | URL                     | 
|----|------------|----------|-------------------------|
| 1  | 채용공고 등록    | `POST`   | `/api/jobs`             | 
| 2  | 채용공고 수정    | `PATCH`  | `/api/jobs/:id`         |   
| 3  | 채용공고 삭제    | `DELETE` | `/api/jobs/:id`         |    
| 4  | 채용공고 목록 조회 | `GET`    | `/api/jobs`             |    
| 5  | 채용공고 검색    | `GET`    | `/api/jobs?serch={검색어}` |    
| 6  | 채용공고 상세 조회 | `GET`    | `/api/jobs/:id`         |     
| 7  | 채용공고 지원    | `POST`   | `/api/jobs/:id/apply`   | 

### 1. 채용공고 등록

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
    "companyId": 1,
    "position": "백엔드 주니어 개발자",
    "reward": 1000000,
    "detail": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
    "skill": "Python",
    "createdAt": "2024-07-31 11:27:16",
    "updatedAt": "2024-07-31 11:27:16",
    "deletedAt": null
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
        "httpStatus": "NOT_FOUND"
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
      "companyId": "회사 아이디는 필수 요소입니다." or "회사 아이디는 1이상의 숫자여야 합니다.",
      "position": "포지션은 필수 요소입니다."
    },
    "httpStatus": "BAD_REQUEST"
  }
}
```

### 2. 채용공고 수정

회사는 아래 데이터와 같이 채용공고를 수정합니다.
(회사 id 이외 모두 수정 가능합니다.)

#### URL
```
PATCH /api/jobs/:id
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
        "companyId": 1,
        "position": "백엔드 주니어 개발자",
        "reward": 1500000,
        "detail": "원티드랩에서 백엔드 주니어 개발자를 '적극' 채용합니다. 자격요건은..",
        "skill": "Python",
        "createdAt": "2024-07-31 13:04:01",
        "updatedAt": "2024-07-31 19:05:36",
        "deletedAt": null
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
        "httpStatus": "NOT_FOUND"
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
    "httpStatus": "BAD_REQUEST"
  }
}
```