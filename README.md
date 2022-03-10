# JWT를 활용한 로그인프로젝트 / 2022.02

## 프로젝트 명

JWT를 활용한 로그인 프로젝트

---

## 제작기간

2/03 - 2/10

---

## 프로젝트 설명

React.js와 Java/SpringBoot 프레임워크 환경에서 시큐리티로그인과 JWT 토큰 전송을 통한 인증/인가 를 구현하는 프로젝트입니다.

---

## 프로젝트 목표

- 스프링 시큐리티를 통해, 모두에게 열린 페이지와 인증된 사용자만 접근할 수 있는 페이지를 구분한다.
- 스스로 정의한 스프링 필터를 활용하여, JWT Token으로 사용자에게 적절하게 authentication을 한다.
- AccessToken과 RefreshToken을 성공적으로 제공한다.
- AccessToken 기한 만료 시 RefreshToken을 통해 사용자를 확인하고 AccessToken을 재발급하는 과정에 성공한다.

---

## 개발도구

Java / SpringBoot framework / react.js

---

## 개발환경

OS: MacOS

Editor: IntelliJ

Java8 , MySQL database

---

## 주요 기능

- 사용자 정의 필터를 통한 사용자 인증
- 로그인 시 입력한 userId/password 로 AccessToken과 RefreshToken 생성/발급
- 매 요청 시마다 http 요청의 헤더에 담긴 AccessToken을 확인하고 Authentication 부여
- AccessToken 만료 시 만료되었다는 응답을 view에 전달하고, view에서 재발급 요청을 받아 DB에 저장된 RefreshToken와 비교하고 Token 재발급
