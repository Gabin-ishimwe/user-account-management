﻿[![Microservice Backend CI/CD](https://github.com/Gabin-ishimwe/user-account-management/actions/workflows/main.yml/badge.svg)](https://github.com/Gabin-ishimwe/user-account-management/actions/workflows/main.yml)

# User Account Management

Microservice infrastructure of user account management project of company Z


## Deployment

#### Backend API (⚠️ offline)
```bash

  http://138.68.107.35/
```
#### Postman API Documentation
```bash
 https://documenter.getpostman.com/view/19575892/2s93eX1YiJ
```

## API endpoints reference

| HTTP Method | Endpoint                              | Description                                                       |
|:------------|:--------------------------------------|:------------------------------------------------------------------|
| `POST`      | `/api/v1/auth/sign-up`                | Endpoint to create new user and account                           |
| `POST`      | `/api/v1/auth/sign-in`                | Endpoint to login user in the application                         |
| `GET`       | `/api/v1/auth/verify-email?token=abc` | Endpoint to verify user's email                                   |
| `POST`      | `/api/v1/auth/forgot-password`        | Endpoint to request for reset password                            |
| `POST`      | `/api/v1/auth/reset-password`         | Endpoint to view balance amount on user's account                 |
| `POST`      | `/api/v1/auth/enable-mfa`             | Endpoint to enable multi-factor authentication                    |
| `POST`      | `/api/v1/auth/send-otp`               | Endpoint to request for OTP                                       |
| `POST`      | `/api/v1/auth/authenticate-otp`       | Endpoint to authenticate with OTP                                 |
| `POST`      | `/api/v1/auth/refresh-token`          | Endpoint to request for refresh token                             |
| `POST`      | `/api/v1/auth/logout`                 | Endpoint to logout user                                           |
| `GET`       | `/api/v1/auth/`                       | Endpoint to get all users (only accessible by `ADMIN`)            |
| `PUT`       | `/api/v1/profile`                     | Endpoint to update user's profile                                 |
| `GET`       | `/api/v1/profile`                     | Endpoint to get one user profile                                  |
| `GET`       | `/api/v1/profile/all`                 | Endpoint to get all user profile                                  |
| `GET`       | `/api/v1/account`                     | Endpoint to get one user's account                                |
| `PUT`       | `/api/v1/account`                     | Endpoint to update one user's account                             |
| `GET`       | `/api/v1/account/all`                 | Endpoint to get all user's account                                |
| `PUT`       | `/api/v1/account/verification`        | Endpoint to verify a user account  (only accessible by `ADMIN`)   |
| `POST`      | `/api/v1/role`                        | Endpoint to assign role to user  (only accessible by `ADMIN`)     |
| `PUT`       | `/api/v1/role`                        | Endpoint to remove a role from user  (only accessible by `ADMIN`) |

## Features

- Sign in
- Sign up
- Multifactor authentication (SMS)
- Reset password
- Update profile infos
- Update account infos
- Get user profile/account
- Verify user account
- Assign/remove roles


## System Design

![App Screenshot](./system_design.png)

## Database Design

![App Screenshot](./database_design.png)


## Installation

Prerequisites
- Java JDK 19
- Docker
- git
- Postman (optional)

Clone this repository

```bash
  git clone https://github.com/Gabin-ishimwe/user-account-management.git
```

Navigate to the project directory

```bash
  docker compose up -d
```

Access all endpoints on this url
```bash
  http://localhost:8081
```

## Tech Stack

**Server:** Docker, Java, Spring Boot, Spring Security, Spring Data JPA, Twilio, Kafka

**Database:** PostgreSQL

**CI/CD:** Jib, Github actions, Digital Ocean

## Design Pattern

- Saga Design Pattern
- Service Discovery
- Database per service
- API Gateway


## Authors

- [Gabin Ishimwe](https://www.github.com/Gabin-ishimwe)

