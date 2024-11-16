# 보고!(BoardGo)🎲 서비스 Backend README

# 🔍서비스 소개

보고!(BoardGo) 서비스는 보드게임를 좋아하는 사람들이 오프라인으로 만날 수 있도록 소통 공간을 만들어주는 서비스 입니다. </br>
사용자들은 직접 보드게임과 장소를 선택 해 모임을 생성하고, 별도의 연락망을 사용하지 않아도 보고 서비스 내에서 채팅방을 통해 소통할 수 있도록 편리성을 제공 해 줍니다. 또한, 보드게임에 익숙하지 않은
분들을 위해 1000여 개의 보드게임을 장르별, 상황별로 검색할 수 있습니다.

## 서비스 기획 이유

보드게임은 소수 보다 다수로 진행하는 보드게임이 많고, 보드게임을 함께 즐길 수 있는 사람이 주변에 없어서 다양한 보드게임을 경험할 수 없는 경우도 있습니다. </br>
그래서 보드게임을 좋아하는 사람! 보드게임은 하고 싶은데 할 줄 모르는 사람! 주변에 함께 할 사람이 없는 사람! 을 타겟팅하여 보드게임에 특화된 모임서비스를 기획했습니다.

## 서비스 주요 기능

- 모임 생성 및 모임 참여하기
- 장르별 보드게임 보기
- 참여한 모임 종료 시 참여자에게 평점과 리뷰(평가태그) 남기기
- 사용자의 리뷰를 한 눈에 볼 수 있는 프로필
- 모임 참여자 간 대화를 할 수 있는 스레드 기능

## 서비스 도메인

---

# 🔍기술 스택

### Environment

![Intellij](https://img.shields.io/badge/Intellij-black?style=for-the-badge&logo=Intellij&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white)
![Github](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white)

### Config

![gradle](https://img.shields.io/badge/gradle-v8.8-CB3837?style=for-the-badge&logo=gradle&logoColor=white)

### Development

![Java](https://img.shields.io/badge/Java-v21-F7DF1E?style=for-the-badge&logo=Java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-v3.2.8-339933?style=for-the-badge&logo=Spring%20Boot&logoColor=55BB55)
![MariaDB](https://img.shields.io/badge/MariaDB-v11.5-000000?style=for-the-badge&logo=Next.js&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-2F2E8B?style=for-the-badge&logo=JUnit5&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-7952B3?style=for-the-badge&logo=Spring%20Data%20JPA&logoColor=white)
![MapStruct](https://img.shields.io/badge/MapStruct-v1.5.5-007FFF?style=for-the-badge&logo=MapStruct&logoColor=white)

### Communication

![Discord](https://img.shields.io/badge/Discord-4A154B?style=for-the-badge&logo=Discord&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white)
![Gather](https://img.shields.io/badge/Gather-00897B?style=for-the-badge&logo=Gather&logoColor=white)

### Infra

![AWS](https://img.shields.io/badge/AWS-4A154B?style=for-the-badge&logo=AWS&logoColor=white)

# 🔍아키텍처 설계

# 🔍ERD

# 🔍Git Flow

| **브랜치**   | **특징**                                                                                                                                                   |
|-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| `prod`    | - 운영 서버 </br> - `prod/1.0` `prod/2.1`                                                                                                                    |
| `dev`     | - 개발 서버 & 테스트서버 </br> - `prod` 브랜치에서 분기 </br> - `prod` 브랜치에서 `hoxfix` 발생 후 `prod`에 merge 됐을 경우, `prod > dev` pull 동기화                                    |
| `feature` | - 하나의 CRUD 단위로 개발 </br> - `dev` 브랜치에서 분기 </br> - 기능 개발 완료 시 `dev` 브랜치로 merge </br> - `feature/기능명` (feature/user-board) </br> - feature 기능명의 네이밍은 케밥 케이스 |
| `fix`     | - `dev` 브랜치에서 이슈 발생 시 `dev` 브랜치에서 분기 </br> - 이슈 수정 완료 시 `dev` 브랜치로 merge                                                                                 |
| `hotfix`  | - `prod` 브랜치에서 이슈 발생 시 분기 </br> - 이슈 해결 시 `prod` 브랜치에 merge </br> - 현재 운영이 `prod/1.0`이고, `hotfix/1.0` 에서 해결한 경우 prod의 다음버전인  `prod/1.1` 에 merge          |

[🪴Branch Convention](https://github.com/LuckyVicky-2team/backend/wiki/%F0%9F%AA%B4Branch-Convention) 참고

# 🔍백엔드 팀원 소개

| **팀원**                                                                                                                                                                         | **서비스 개발 담당 기능**                                                              | **이슈 및 해결과정** |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------|---------------|
| <img src="https://github.com/user-attachments/assets/10d9d2d9-d995-4f5e-bfb6-78d81f2bc5f4"  width="140" height="140"/> </br> **이예진([@yeahdy](https://github.com/yeahdy))**     | - 소셜 회원 </br> - 약관동의 </br> - 메인 홈 </br> - 모임 상호작용 </br> - 회원 마이페이지 </br> - 리뷰 |               |
| <img src="https://github.com/user-attachments/assets/3ec26fd9-06c7-4184-8ac6-222dcb95f6ec"  width="150" height="150"/> </br> **양지원([@ambosing](https://github.com/ambosing))** | - 일반회원 </br> - 모임 </br> - 보드게임 </br> - 찜하기 </br> - 쓰레드 형식 채팅                  |               |

## 공통

### 테이블 및 엔티티 설계

- 설계 시 고려한 부분 작성하기

### 서버 아키텍처 설계
개발 서버 아키텍처
![개발 서버 아키텍처](./img/dev_arch.png)
---
운영 서버 아키텍처
![운영 서버 아키텍처](./img/prod_arch.png)

### 계층 아키텍처 설계
퍼사드 패턴 사용 시
![퍼사드 패턴](./img/facade_arch.png)
---
서비스만 사용 시(같은 도메인 비즈니스 로직만 필요할 경우)
![기존 레이어드 아키텍처](./img/service_arch.png)