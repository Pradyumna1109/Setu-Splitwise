**Expense-Sharing Application**

This is a simple expense-sharing application similar to Splitwise, built using Spring Boot and PostgreSQL. 
It supports features like creating groups, splitting expenses, and tracking balances.

**Features**

User Registration, Authentication and Authorization

Create groups with any number of users

Create and View Expenses

Split Expenses Equally, Unequally, or by percentage

Track Balances with Friends



**🛠️ Tech Stack**

Backend: Spring Boot

Database: PostgreSQL

ORM: Ebean

Containerization: Docker, Docker Compose

**🏁 Getting Started**

**Prerequisites**

Docker and Docker Compose installed

**Installation Steps**

Clone the Repository

cd setu-spliwise

Build the Docker Containers:   docker-compose build

Start the Containers:   docker-compose up

Check if Containers are Running:   docker ps

**⚙️ Configuration**
The application uses environment variables for database configuration. Adjust them if needed in the docker-compose.yml or .env file.

Database URL: jdbc:postgresql://db:5432/splitwise

Username: postgres

Password: postgres

**🔐 Authentication**

This application uses JWT (JSON Web Token) for authentication and authorization.
Upon successful login, a JWT token is generated.
The token is required for accessing secured endpoints.

**Authentication and Signup Endpoints**

POST /api/v1/users - Register a new user

POST /api/v1/auth/generate - Login with email and password

GET /api/v1/users - Retrieve logged-in user details (Requires JWT)

**🔎 Custom Authorization**

The application provides two custom annotations for authorization purposes:

**@Authenticate**

Ensures that the API can only be accessed by authenticated users.
Applied at the controller level to secure endpoints.

**@GroupAuthorize**

Checks if the authenticated user has access to the specified group.
Ensures that users can only perform actions on groups they belong to.
Can be used for endpoints related to group management.



**🧑‍🤝‍🧑 Group Management**

The application allows users to create and manage groups for better expense tracking.

**Group Management Endpoints**

POST /api/v1/groups - Create a new group

PUT /api/v1/groups/{groupId} - Update the members of a group

GET /api/v1/groups - List all groups of a user (Uses JWT)

GET /api/v1/groups/{groupId} - Get group details by group ID

**💰 Expense Management**

Users can create and manage expenses using these APIs. 
Expenses can be split equally, unequally, or based on percentages. Expense authorization is also implemented.

Example Expense Management Endpoints

POST /api/v1/groups/{groupId}/expenses - Create a new expense in a specified group

GET /api/v1/groups/{groupId}/expenses - List all expenses for the user in a specified group

GET /api/v1/groups/{groupId}/expenses/{expenseId} - Get expense details by Id in a specified group (Only if the user is authorized)

**Expense Types**

EQUAL: Split equally among users

UNEQUAL: Split unequally based on specified amounts

PERCENTAGE: Split based on specified percentages

The project uses a split strategy interface, implemented by specific split strategy classes. 
Each class includes custom validation and object creation tailored to its respective split type. 
This design offers flexibility and can be easily extended to support additional split types.

**🤝 Settlement Management**

Users can settle their pending expenses by using the settlement API.

POST /api/v1/groups/{groupId}/settlements - Create a new settlement to clear an individual 
splits or all splits in a specified group 

These APIs are built using a basic settlement service, which is an extension of a core settlement service. 
This structure allows for easy expansion to support new types of settlement functionalities.

**📊 Check Balance**

Users can check their balances with other members of the group.

Check Balance Endpoints

GET /api/v1/groups/{groupId}/settlements - Get all balances for the user within a group

**🧳 DTOs (Data Transfer Objects)**

DTOs are used to transfer data between the client and the server. 
The application follows a structured approach to make the code more reusable and maintainable.

DTOs are divided into three categories:

Spec Objects: These objects carry data from the client to the controller. 
They are reusable across multiple endpoints and are often used in create or update requests.

Data Objects: These represent the core data structure in the response DTOs. 
They include details about the resource being returned.

Info Objects: Info DTOs contain minimal information like resource identifiers (e.g., IDs) 
and are used within data DTOs for relational references.

This division ensures clear separation of concerns, improved code reusability, and consistent data handling.
