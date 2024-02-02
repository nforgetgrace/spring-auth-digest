# 프로젝트명
digest_management

## 백앤드서버 기능

군부대 내 단위체계에 귀속된 종단 단말기(장치)의 메타데이터를 보관하기 위한 rest기반 API서버

## 프로젝트 정보
전자정부프레임워크 4.1 버전을 기반으로 Spring Boot를 셋업

## 백앤드 공통기능

공통응답처리

로그키(트랜잭션키) MDC

공통예외처리

로깅기능

## 빌드도구

* [Maven](https://maven.apache.org/) - Dependency Management

## 참여자

* **이성호K** - *Smart-X 사업팀*
* **정재국D** - *Smart-X 사업팀*
* **박상민S** - *Smart-X 사업팀*
* **조원준S** - *Smart-X 사업팀*
* **정나린S** - *Smart-X 사업팀*
* **손홍일S** - *Smart-X 사업팀*

## 참고
 - 현재 백앤드 서버 인증관련한 설정은 아무것도 되어있지 않음
결론적으로 Req가 들어오면 족족 모두 res를 내보내줄 것임
추후 필요하면 Spring Security 추가해야하고, 
공통유틸에 있는 JWT관련 소스는 사용하지 않음. 
참고 바람.

 - 비지니스 로직이 들어가야할 곳은 digest.api.domain 하위 패키지임.
