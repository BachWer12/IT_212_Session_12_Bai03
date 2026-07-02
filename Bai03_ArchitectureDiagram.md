# Architecture Diagram - Hệ thống eKYC
## Luồng Client -> Controller -> Service -> Database

```mermaid
sequenceDiagram
    participant Client as Client (Mobile/Web)
    participant Ctrl as RegistrationController
    participant Service as RegistrationServiceImpl
    participant Validator as Validator (@Valid)
    participant Repo as RegistrationRepository
    participant DB as Database (PostgreSQL)

    Client->>Ctrl: POST /api/v1/ekyc/register
    Note over Client: JSON body: fullName, phone, email, citizenId

    Ctrl->>Validator: @Valid RegistrationRequest
    Validator-->>Ctrl: Validation passes

    Ctrl->>Service: register(request)
    Note over Service: SLF4J Log: Bat dau dang ky

    Service->>Repo: existsByCitizenId(citizenId)
    Repo->>DB: SELECT COUNT(*) WHERE citizenId = ?
    DB-->>Repo: 0 (khong trung)
    
    Service->>Repo: existsByPhone(phone)
    Repo->>DB: SELECT COUNT(*) WHERE phone = ?
    DB-->>Repo: 0 (khong trung)
    
    Service->>Repo: existsByEmail(email)
    Repo->>DB: SELECT COUNT(*) WHERE email = ?
    DB-->>Repo: 0 (khong trung)

    Service->>Repo: save(Registration)
    Repo->>DB: INSERT INTO registrations (...)
    DB-->>Repo: Registration entity (voi ID)
    Repo-->>Service: Registration

    Note over Service: SLF4J Log: Dang ky thanh cong
    
    Service-->>Ctrl: RegistrationResponse
    
    Ctrl-->>Client: 201 Created
    Note over Client: JSON: registrationId, message, nextStep: "OCR_VERIFICATION"
```

## Sơ đồ kiến trúc tổng thể (Component Diagram)

```mermaid
graph TB
    subgraph "Client"
        MobileApp[Mobile App]
    end

    subgraph "Spring Boot Application"
        Controller[RegistrationController<br/>REST API]
        Service[RegistrationService<br/>Business Logic]
        Repo[RegistrationRepository<br/>JPA Data]
        Validation[Validation Layer<br/>Custom Validators]
        GlobalHandler[GlobalExceptionHandler<br/>@RestControllerAdvice]
    end

    subgraph "Data Layer"
        DB[(PostgreSQL<br/>Hồ sơ đăng ký)]
    end

    subgraph "Cross-Cutting"
        Log[SLF4J Logging]
    end

    MobileApp -->|HTTP Request| Controller
    Controller --> Validation
    Controller --> Service
    Service --> Repo
    Repo --> DB
    Service --> Log
    Controller --> GlobalHandler
    GlobalHandler -->|Error Response| MobileApp
    Controller -->|Success Response| MobileApp
```

## Package Structure

```
com.abcbank.ekyc
├── controller/
│   └── RegistrationController.java      # REST API endpoint
├── dto/
│   ├── RegistrationRequest.java         # Request DTO (input)
│   ├── RegistrationResponse.java        # Response DTO (output)
│   └── ErrorResponse.java              # Error DTO chuẩn
├── entity/
│   ├── Registration.java               # JPA Entity
│   └── RegistrationStatus.java         # Enum trạng thái
├── exception/
│   ├── DuplicateResourceException.java  # Lỗi trùng lặp
│   ├── BusinessException.java           # Lỗi nghiệp vụ
│   └── GlobalExceptionHandler.java      # Xử lý exception tập trung
├── repository/
│   └── RegistrationRepository.java     # Spring Data JPA
├── service/
│   ├── RegistrationService.java         # Interface
│   └── impl/
│       └── RegistrationServiceImpl.java # Implementation
└── validator/
    ├── ValidCitizenId.java              # Annotation custom
    ├── CitizenIdValidator.java          # Validator CCCD 12 số
    ├── ValidVietnamesePhone.java        # Annotation custom
    └── VietnamesePhoneValidator.java    # Validator SĐT VN
```
