

## Estructura

```txt
AllProject
│
├── Entity
│   ├── atributos
│   ├── constructor
│   ├── Getter
│   ├── setter
│   ├── relaciones
│   ├── validaciones
│   └── overrides (toString, equals)
│
├── IRepository
│   ├── save()
│   ├── update()
│   ├── delete()
│   ├── findById()
│   ├── findAll()
│   ├── exists()
│   ├── pagination()
│   └── filters()
│
├── IService
│   ├── create()
│   ├── update()
│   ├── delete()
│   ├── getById()
│   ├── getAll()
│   ├── validations()
│   ├── businessRules()
│   └── transaction()
│
├── Service
│   ├── implement IRepository
│   ├── business logic
│   ├── validations
│   ├── exception handling
│   ├── mapper DTO ↔ Entity
│   ├── transaction management
│   └── response handling
│
├── Controller
│   ├── endpoints
│   ├── request mapping
│   ├── response entity
│   ├── validations
│   ├── exception handling
│   ├── authentication
│   ├── authorization
│   └── swagger documentation
│
├── DTO
│   ├── atributos
│   ├── constructor
│   ├── Getter
│   ├── setter
│   ├── validations
│   ├── requestDTO
│   └── responseDTO
│
├── IDTO
│   ├── entityToDTO()
│   ├── dtoToEntity()
│   ├── mapper()
│   ├── projections()
│   └── customResponse()
│
└── Utils
    ├── JWT
    ├── Encrypt
    ├── Constants
    ├── Helpers
    ├── Validators
    ├── Exceptions
    ├── Messages
    ├── DateUtils
    ├── Pagination
    └── Logger
```