# 프리온보딩 백엔드 인턴십 선발 과제

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
        "companyId": 1,
        "position": "백엔드 주니어 개발자",
        "reward": 1500000,
        "detail": "원티드랩에서 백엔드 주니어 개발자를 '적극' 채용합니다. 자격요건은..",
        "skill": "Python",
        "createdAt": "2024-07-31 13:04:01",
        "updatedAt": "2024-07-31 19:05:36",
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

### 3. 채용공고 삭제

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
        "httpStatus": "NOT_FOUND"
    }
}
```

### 4. 채용공고 목록 조회

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
            "jobId": 1,
            "companyName": "원티드",
            "nation": "한국",
            "area": "서울",
            "position": "백엔드 주니어 개발자",
            "reward": 1500000,
            "skill": "Python"
        },
        {
            "jobId": 2,
            "companyName": "네이버",
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

### 5. 채용공고 검색

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
      "jobId": 11,
      "companyName": "네이버",
      "nation": "한국",
      "area": "판교",
      "position": "Django 백엔드 개발자",
      "reward": 1000000,
      "skill": "Python"
    },
    {
      "jobId": 12,
      "companyName": "원티드",
      "nation": "한국",
      "area": "서울",
      "position": "Python 백엔드 개발자",
      "reward": 1000000,
      "skill": "Django"
    },
    {
      "jobId": 17,
      "companyName": "구글",
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

### 6. 채용공고 상세 조회

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
    "jobId": 10,
    "companyName": "원티드",
    "nation": "한국",
    "area": "서울",
    "position": "백엔드 주니어 개발자",
    "reward": 1500000,
    "detail": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
    "skill": "Python",
    "otherJobIds": [
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
        "httpStatus": "NOT_FOUND"
    }
}
```

## 7. 채용공고 지원

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
    "jobId": 1,
    "userId": 2,
    "createdAt": "2024-08-05 14:54:22"
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
            "jobId": "채용공고 아이디는 1이상의 숫자여야 합니다.",
            "userId": "유저 아이디는 1이상의 숫자여야 합니다."
        },
        "httpStatus": "BAD_REQUEST"
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
        "httpStatus": "NOT_FOUND"
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
        "httpStatus": "NOT_FOUND"
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
        "httpStatus": "CONFILCT"
    }
}
```

