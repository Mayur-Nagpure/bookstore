# Online Book Store

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Installation](#installation)
- [Usage](#usage)
- [Testing](#testing)
- [Project Roadmap](#project-roadmap)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

---

## Overview

An online book store application built with Spring Boot. Features include a user-friendly bookstore, shopping cart, secure authentication, order processing, billing, email notifications, and an AI chatbot for book recommendations.

---

## Features

- Browse, search, and buy books online
- Shopping cart management
- Secure user authentication
- Order checkout and payment integration
- Email notifications
- AI chatbot for help and recommendations (OmniDim)
- Thymeleaf-based UI
- Environment-based secured configuration

---

## Project Structure

bookstore/
├── src/
│ ├── main/
│ ├── test/
├── target/
├── README.md
├── .gitignore
|──  Dockerfile
|----....


## Getting Started

Follow these steps to set up and run the project locally.

---

## Prerequisites

- **Programming Language:** Java 11+
- **Build Tool:** Maven
- **Container Runtime:** Docker (optional for containerized use)

---

## Setup

1. Clone the repository:
    ```
    git clone https://github.com/Mayur-Nagpure/bookstore.git
    cd bookstore
    ```

2. Configure environment variables or `env.properties` (do NOT commit secrets):

    ```
    DB_USER=your_db_username
    DB_PASS=your_db_password
    EMAIL_USER=your_email
    EMAIL_PASS=your_email_password
    OMNIDIM_KEY=your_chatbot_key
    GOOGLE_CLIENT_ID=your_google_client_id
    GOOGLE_CLIENT_SECRET=your_google_client_secret
    GITHUB_CLIENT_ID=your_github_client_id
    GITHUB_CLIENT_SECRET=your_github_client_secret
    ```

---

## Installation

**Build from source:**
mvn clean package

---

## Usage

**Run with environment variables or property file:**
java -jar target/bookstore.jar --spring.config.additional-location=env.properties

**Using Docker:**
docker build -t mayur-nagpure/bookstore .
docker run -it mayur-nagpure/bookstore

---

## Testing

Run tests with:
mvn test

---

## Project Roadmap

 [] Implement core search and filter functionality
 [] Add Docker Compose support for local development
 [] Expand AI chatbot integration and improve responses
 [] Complete checkout and payment processing features

---

## Contributing

- 💬 [Join the Discussions](https://github.com/Mayur-Nagpure/bookstore/discussions)
- 🐛 [Report Issues](https://github.com/Mayur-Nagpure/bookstore/issues)
- 💡 [Submit Pull Requests](https://github.com/Mayur-Nagpure/bookstore/pulls)

---

## License

This project is licensed under the [MIT License](LICENSE).

---

## Acknowledgments

Thanks to  open-source libraries, and the OmniDim widget for the AI chatbot feature.
