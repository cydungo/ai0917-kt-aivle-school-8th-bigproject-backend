# ai0917-kt-aivle-school-8th-bigproject-backend
웹소설·웹툰 IP의 지속 가능성 확보를 위한 AI 기반 통합 집필 및 IP확장 지원 시스템


<HR>


# PostgreSQL 17 + pgvector 기반 벡터 데이터베이스 구축 가이드

본 가이드는 **Windows 환경**에서 **Docker**를 사용하여 **PostgreSQL 17 + pgvector**를 구축하고,  
**Tailscale**을 통해 팀원과 서버를 공유하는 **전체 실무 절차**를 다룹니다.


## 1. 사전 준비 (Prerequisites)

안정적인 구동을 위해 아래 환경이 **사전에 반드시 구축**되어 있어야 합니다.

- **Docker Desktop (Windows)**
  - 설치 및 실행 상태
  - Settings → General → **Use the WSL 2 based engine** 체크
- **WSL 2**
  - Docker Desktop과 연동되어 있어야 함
- **Tailscale**
  - 서버(호스트 PC)와 팀원 클라이언트 모두 로그인 상태


## 2. Docker 배포 및 서버 실행 (Deployment)

PostgreSQL 17과 `pgvector` 확장이 포함된 공식 이미지를 사용합니다.  
데이터 유실 방지와 **HNSW 인덱스 성능 최적화** 설정을 포함합니다.

### 2-1. 이미지 다운로드
 
     docker pull pgvector/pgvector:pg17

### 2-2. 컨테이너 실행
⚠️ HNSW 인덱스 빌드 시 공유 메모리 부족 오류 방지를 위해
--shm-size=1g 옵션은 반드시 포함해야 합니다.
  
    docker run -d
    --name postgres17-vector
    -p 5432:5432
    --shm-size=1g
    -v pgdata:/var/lib/postgresql/data
    -e POSTGRES_USER=postgres
    -e POSTGRES_PASSWORD=postgres
    -e POSTGRES_DB=vector_db
    pgvector/pgvector:pg17


### 2-3. 옵션 설명

  - --shm-size=1g : HNSW 인덱스 오류 방지 (HNSW 병렬 인덱스 생성 시 발생하는 shared memory 오류 방지 (필수))
  - -v pgdata:/var/lib/postgresql/data : 데이터 유지 (컨테이너 삭제 후에도 데이터 유지)
  - -p 5432:5432 : 포트 매핑 (호스트 ↔ 컨테이너 포트 매핑 (외부 접속 허용))

## 3. 데이터베이스 초기 설정
  
  컨테이너 실행 후, PostgreSQL 내부에서 pgvector 확장을 활성화해야 합니다.
  
  ### 3-1. 컨테이너 내부 접속
  
      docker exec -it postgres17-vector psql -U postgres -d vector_db

  ### 3-2. pgvector 확장 활성화
  
      CREATE EXTENSION IF NOT EXISTS vector;


  ### 3-3. 설치 확인
  
      \dx

  확장 목록에 **vector**가 표시되면 정상 설치 완료

## 4. 외부 접속

Tailscale의 가상 사설망 IP를 사용하여 팀원들이 PostgreSQL 서버에 접속합니다.

  ### 4-1. 접속 정보 (Connection Info)
<table> <thead> <tr> <th>항목</th> <th>설정값</th> </tr> </thead> <tbody> <tr> <td>Host (IP)</td> <td>100.95.214.66 (Tailscale IP)</td> </tr> <tr> <td>Port</td> <td>5432</td> </tr> <tr> <td>Database</td> <td>vector_db</td> </tr> <tr> <td>Username</td> <td>postgres (또는 지정된 계정)</td> </tr> <tr> <td>Password</td> <td>postgres (또는 지정된 비밀번호)</td> </tr> </tbody> </table>

  ### 4-2. Spring Boot (JPA) 설정 예시
    
  application.yaml
    
      spring.datasource.url=jdbc:postgresql://100.95.214.66:5432/vector_db
      spring.datasource.username=postgres
      spring.datasource.password=postgres
      spring.datasource.driver-class-name=org.postgresql.Driver


## 5. 트러블슈팅

### 1. 포트 충돌

- 현상

  - 컨테이너 실행 실패
  - authentication failed 오류

- 원인

  - Windows 로컬에 설치된 PostgreSQL이 이미 5432 포트 점유

- 해결

  - Windows 서비스에서 로컬 PostgreSQL 중지
  - 또는 Docker 컨테이너 포트를 다른 포트로 매핑

### 2. pg_hba.conf 인증

컨테이너 내부 파일 수정
- 현상: no pg_hba.conf entry for host 오류
- 원인: Tailscale IP 대역이 허용되지 않음
- 해결:
    
      vi /var/lib/postgresql/data/pg_hba.conf
      host all all 100.64.0.0/10 md5
      docker restart postgres17-vector


### 3. 데이터베이스 권한 문제


  - 현상 
    - permission denied for schema public

  - 해결
    
    - 관리자 계정으로 권한 부여

        - GRANT USAGE ON SCHEMA public TO team_user;
        - GRANT CREATE ON SCHEMA public TO team_user;


## 6. 주의 사항

- 개인 PC 기반 서버 → PC 종료 시 접속 차단
- 기본 postgres 계정 공유 금지 → 팀원별 계정 생성
