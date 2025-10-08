# Employee and Department Management API
![Java](https://img.shields.io/badge/Java-17-blue) 
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-green)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-lightblue)
![JWT](https://img.shields.io/badge/JWT-Authentication-orange)
![Testing](https://img.shields.io/badge/Testing-JUnit%2FMockito-success)

A Spring Boot REST API with JWT authentication for managing company employees and departments. Features secure role-based access, data validation, and Dockerized PostgreSQL database.

## ✨ What it does

- **JWT Authentication**: Secure token-based authentication
- **Employee & Department Management**: Full CRUD operations with proper relationships
- **Role-Based Security**: Different access for EMPLOYEE, MANAGER, and ADMIN roles
- **Comprehensive Testing**: Unit and integration tests with JUnit 5 and Mockito
- **Data Validation**: Ensures only valid data gets into the database
- **Containerized Deployment**: Full Docker Compose setup (App + PostgreSQL)
- **Auto Documentation**: Swagger UI for testing all endpoints
- **Global Error Handling**: Clean, consistent error messages
- **RESTful Best Practices**: Includes pagination and sorting for efficient data retrieval
  
## 🛠️ Built With

- **Java 17** + **Spring Boot 3**
- **Spring Security** with JWT authentication
- **Spring Data JPA** + **PostgreSQL**
- **JWT** for secure token-based authentication
- **JUnit 5 & Mockito** for comprehensive testing
- **Docker** & **Docker Compose**
- **Maven** for building
- **SpringDoc OpenAPI** for documentation

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven
- Docker & Docker Compose

### Option 1: Run locally (Spring Boot in IDE, DB in Docker)
1. **Clone the repository:**
   ```bash
   git clone https://github.com/Orvielle-Atanacio/Employee-Management-API.git
   
   cd Employee-Management-API
   ```
2. Start PostgreSQL in Docker:
    ```bash
    docker-compose up -d db
    ```
3. Load test data:
   ```bash
   docker cp ./src/main/resources/data.sql postgres:/tmp/data.sql

   docker exec -it postgres psql -U orvi27 -d employee_db -f /tmp/data.sql
   ```
   
5. Run the Spring Boot app locally:
   ```bash
   mvn spring-boot:run
   ```
   → Uses application.properties to connect to localhost:5332
   
### Option 2: Run everything in Docker (app + DB)
1. Build jar file:
   ```bash
   mvn clean package -DskipTests
   ```
2. Start containers:
   ```bash
   docker-compose up -d --build
   ```
### **Access the API:**
   - **Swagger UI Documentation**: http://localhost:8080/swagger-ui.html
   - **API Base URL**: http://localhost:8080
   - **Health Check Endpoint**: http://localhost:8080/health
   - **OpenAPI JSON**: http://localhost:8080/v3/api-docs



## 🔐 Default Users

These users are automatically created when the application starts:

| Username | Password |   Role   |   What they can do   |
|----------|----------|----------|----------------------|
| employee | password | EMPLOYEE | View data            |
| manager  | password | MANAGER  | Create & update      |
| admin    | password | ADMIN    | Full access + delete |

## 🗂️ Project Structure

**employee-management-api/**  
├── **src/**  
│   ├── **main/**  
│   │   ├── **java/com/luv2code/springboot/cruddemo/**  
│   │   │   ├── `controller/`       → REST API endpoints  
│   │   │   ├── `entity/`           → JPA database models  
│   │   │   ├── `repository/`       → Data access layer  
│   │   │   ├── `service/`          → Business logic  
│   │   │   ├── `dto/`              → Request/response objects  
│   │   │   ├── `exception/`        → Error handling  
│   │   │   ├── `security/`         → JWT authentication config  
│   │   │   └── `config/`           → Configuration classes  
│   │   └── **resources/**  
│   │       ├── `application.properties` → Local development config  
│   │       ├── `application-docker.properties` → Docker environment config  
│   │       ├── `schema.sql`        → Database schema  
│   │       └── `data.sql`          → Test data + users  
│   └── **test/**  
│       └── **java/com/luv2code/springboot/cruddemo/rest/**  
│           ├── `DepartmentRestControllerTest.java` → Department API tests  
│           └── `EmployeeRestControllerTest.java` → Employee API tests  
├── `docker-compose.yml`    → Multi-container Docker setup  
├── `Dockerfile`           → Application container definition  
├── `pom.xml`              → Maven dependencies & build config  
└── `README.md`            → Project documentation  

## 🔧 Configuration

### Database Configuration
- **PostgreSQL** running on port 5332
- **Database**: employee_db
- **Username**: orvi27
- **Password**: password

### JWT Configuration
- **Secret Key**: Configurable in application properties
- **Token Expiration**: 24 hours
- **Algorithm**: HS256

## 🧠 Key Features Implemented

### Security
- JWT-based authentication with Spring Security
- Role-based authorization with database-backed users
- Password encryption using BCrypt
- Stateless session management

### Testing
- Comprehensive controller testing with MockMvc
- Security integration testing with @WithMockUser
- Service layer mocking for isolated testing
- CSRF protection testing

### Database
- Proper entity relationships (Employee ↔ Department)
- Automatic schema creation with SQL files
- Test data initialization
- Foreign key constraints

### API Design
- RESTful endpoints with proper HTTP status codes
- Pagination and sorting support
- Global exception handling
- Input validation

## 📊 API Endpoints

### Authentication
- `POST /api/auth/login` - Login and get JWT token

### Employees
- `GET /api/employees` - Get all employees (paginated)
- `GET /api/employees/{id}` - Get employee by ID
- `POST /api/employees` - Create new employee
- `PUT /api/employees/{id}` - Update employee
- `DELETE /api/employees/{id}` - Delete employee

### Departments
- `GET /api/departments` - Get all departments
- `GET /api/departments/{id}` - Get department by ID
- `POST /api/departments` - Create new department
- `PUT /api/departments/{id}` - Update department
- `DELETE /api/departments/{id}` - Delete department

## 🐛 Troubleshooting

### Common Issues
- **JWT Token not working**: Check if token is properly formatted in Authorization header
- **Database connection issues**: Verify PostgreSQL container is running
- **Authentication failures**: Confirm username/password and check user roles in database
- **Test failures**: Ensure all dependencies are properly mocked

### Logs
```bash
# View application logs
docker-compose logs -f app

# View database logs  
docker-compose logs -f db
```

## 🧠 What I Learned

- **JWT Security**: Implemented stateless authentication with Spring Security and JWT tokens
- **Role-Based Authorization**: Configured EMPLOYEE, MANAGER, ADMIN access levels
- **Comprehensive Testing**: Built controller tests with JUnit 5, Mockito, and Spring Security Test
- **Database Design**: Created entity relationships and proper data validation
- **REST API Best Practices**: Implemented pagination, DTOs, and global exception handling
- **Docker Deployment**: Containerized application and database with Docker Compose
- **Spring Boot Features**: Leveraged auto-configuration, profiles, and Spring Data JPA
- **API Documentation**: Integrated Swagger UI for automatic API documentation
  
## 📸 API in Action

![Swagger UI Documentation](https://github.com/Orvielle-Atanacio/Employee-and-Department-Management-API/raw/main/url-based-roles-rest-security-practice/assets/swagger-ui.png)
