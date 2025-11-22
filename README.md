# SC2002-Project: Internship Management System

## Project Overview

The SC2002-Project is a console-based Internship Management System designed to streamline the process of managing internship opportunities and applications for students, company representatives, and career center staff. This system facilitates the creation, approval, application, and tracking of internships, ensuring a smooth workflow for all stakeholders.

## Features

The system supports the following key functionalities:

-   **User Authentication & Management**: Secure login, password reset, and change password functionalities for all user types. Company Representatives can register and await approval from Career Center Staff.
-   **Student Module**:
    -   View and filter available internships by various criteria (level, company name, major).
    -   Search internships by keywords.
    -   Apply for internships with application limits.
    -   View personal application status.
    -   Accept or withdraw internship offers.
-   **Company Representative Module**:
    -   View their posted internships.
    -   Create new internship listings.
    -   Toggle internship visibility.
    -   View applications for their internships.
    -   Approve/reject student applications.
    -   Edit and delete internship listings (with restrictions).
-   **Career Center Staff Module**:
    -   Manage Company Representative accounts (approve/reject registrations).
    -   Review and approve/reject pending internship postings.
    -   Manage student withdrawal requests.
    -   Generate various reports (by major, level, status, company, student, custom, and summary).
-   **Data Persistence**: All user, internship, and application data is persisted using CSV files.

## Technology Stack

-   **Language**: Java

## Directory Structure

```
└── aryand43-sc2002-project/
    ├── README.md
    ├── Main.java
    ├── Test.java
    ├── assets/
    │   ├── diagrams/
    │   │   ├── SC2002 Sequence Diagram.png
    │   │   └── SC2002 UML Class Diagram.png
    │   └── testcases/
    │       ├── application_list.csv
    │       ├── internship_list.csv
    │       ├── sample_company_representative_list.csv
    │       ├── sample_staff_list.csv
    │       ├── sample_student_list.csv
    └── src/
        ├── boundaries/
        │   ├── CompanyRepBoundary.java
        │   └── MenuBoundary.java
        ├── controllers/
        │   ├── ApplicationManager.java
        │   ├── ApplicationRepository.java
        │   ├── ApplicationSerializer.java
        │   ├── CompanyRepresentativeSerializer.java
        │   ├── DataPersistence.java
        │   ├── FileApplicationPersistence.java
        │   ├── FileHandler.java
        │   ├── FileInternshipPersistence.java
        │   ├── InternshipApprovalService.java
        │   ├── InternshipManager.java
        │   ├── InternshipQueryService.java
        │   ├── InternshipRepository.java
        │   ├── InternshipSerializer.java
        │   ├── ListingPolicy.java
        │   ├── ReportGenerator.java
        │   ├── Serializer.java
        │   ├── StaffSerializer.java
        │   ├── StandardListingPolicy.java
        │   ├── StudentSerializer.java
        │   └── UserManager.java
        ├── models/
        │   ├── Application.java
        │   ├── CareerCenterStaff.java
        │   ├── CompanyRepresentative.java
        │   ├── Internship.java
        │   ├── Student.java
        │   └── User.java
        └── views/
```

## Diagrams

The project's architecture and interactions are further explained through the following diagrams:

-   **UML Class Diagram**: Illustrates the static structure of the system, including classes, their attributes, operations, and relationships.

    ```
    assets/diagrams/SC2002 UML Class Diagram.png
    ```

-   **Sequence Diagram**: Details the dynamic interactions between objects in the system for key use cases, such as the login process or internship application flow.

    ```
    assets/diagrams/SC2002 Sequence Diagram.png
    ```
