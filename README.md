# Trustworthy Product Reviews
## *SYSC4806A - Project 20*


### Description
A web application developed for SYSC4806 that allows users to share their reviews of various products with others.

---

### Website: [Link to deployed website](https://sysc4806-project20-cuamdwe2e8e2hfbw.eastus2-01.azurewebsites.net/)

---

### CI/CD:
[![Java CI with Maven](https://github.com/zurilg/Trustworthy-Product-Reviews/actions/workflows/maven.yml/badge.svg)](https://github.com/zurilg/Trustworthy-Product-Reviews/actions/workflows/maven.yml)
[![Build and deploy JAR app to Azure](https://github.com/zurilg/Trustworthy-Product-Reviews/actions/workflows/main_sysc4806-project20.yml/badge.svg)](https://github.com/zurilg/Trustworthy-Product-Reviews/actions/workflows/main_sysc4806-project20.yml)

---

### Kanban: [Link to Kanban](https://github.com/users/zurilg/projects/1)

---

### State of the Project:
The project is currently in the alpha release stage. It is somewhat use-able, providing users with multiple functionalities. Although, it is not yet feature-complete. The current state of the project attempts to best adhere to the expectations of *Milestone 2: Alpha Release*.
The previous milestone has been successfully completed.

**Work products delivered for *Milestone 1*:**
- **GitHub Repository Setup:** [Link to Repository](https://github.com/zurilg/Trustworthy-Product-Reviews)
- **Continuous Integration/Deployment (CI/CD):** Workflow is automated via GiHub Actions. Running Java CI with Maven. CD to Azure with each push to the main branch.
- **Ready to Run:** `pom.xml` configured to package and run the Spring Boot application.
- **Kanban Board:** Created using GitHub Projects to track tasks and progress.
- **Scrum Meetings**: Weekly meetings held to discuss progress and next steps. Issues on GitHub used to communicate scrums.
- **UML Class Diagram:** Created to represent core entities and relationships. [Milestone_1-UML_Class_Diagram.png](https://github.com/zurilg/Trustworthy-Product-Reviews/tree/main/documentation/UML/Milestone_1-UML_Class_Diagram.png)
- **Key Relationships:**

  | Entity | Relationship | Type        | Multiplicity                           |
     |:-------|:-------------|:------------|:---------------------------------------|
  | Review | Product      | Many-to-One | 0..* reviews associated with 1 product |
  | Review | User         | Many-to-One | 0..* reviews written by 1 user          |
- **Key use cases for *Milestone 1*:**
    - Users can view a list of all products from the home page (root path `/`).
    - Users can select a product to view its details, category, and associated reviews on its dedicated product page (`/product/{UUID}`).
    - The product page successfully displays the product's average rating and number of reviews.

**Work products delivered for *Milestone 2*:**
- **UML Class Diagram:** Updated to reflect any changes in core entities and relationships. [Milestone_2-UML_Class_Diagram.png](https://github.com/zurilg/Trustworthy-Product-Reviews/tree/main/documentation/UML/Milestone_2-UML_Class_Diagram.png)
- **Key Relationships:**

  | Entity  | Relationship | Type        | Multiplicity                                   |
     |:--------|:-------------|:------------|:-----------------------------------------------|
  | Review  | Product      | Many-to-One | 0..* reviews associated with 1 product         |
  | Review  | User         | Many-to-One | 0..* reviews written by 1 user                 |
  | Product | Category     | Many-to-One | 0..* products associated with 1 category       |
    | User    | Follow       | One-to-Many | 1 user can have 0..* followers or 0..* follows |
- **Features Implemented for *Milestone 2*:**
    - User login functionality allowing users to log in using their username.
    - Following system enabling users to follow other users.
    - Posting reviews functionality for logged-in users.
    - Product addition feature allowing logged-in users to add new products.
    - Category browsing feature enabling users to view products within specific categories.
    - Jaccard distance calculation between users.
    - Degree of separation calculation between users.
    - User searching features based on Jaccard distance and most-followed users.
    - Advanced sorting of reviews tailored to logged-in users on both product browsing and product pages.
    - Product search functionality.
    - AOP logging, caching, exception handling, and validation have been integrated throughout the application to enhance performance and reliability.
---

### UML Class Diagram:
This diagram represents the core entities (`Product`, `Review`, `User`, `Category`) and their relationships.

![alt text](https://github.com/zurilg/Trustworthy-Product-Reviews/blob/main/documentation/UML/Milestone_2-UML_Class_Diagram.png?raw=true "Class Diagram")



---

### Brainstorming Goals for Next Sprint (*Milestone 3*)
- Allow users to register a new account.
- Have jaccard distance show next to usernames.
- Have user's jaccard distance and dos display reactively after following.
- Improve the look of the home page.
- Improve the look of the similar users + most followed users pages.

---

### Team Members:
| Name | Student Number |
|:-----|:--|
| Damon Ricci | 101229913 |
| Elias Pantazopoulos | 101273477 |
| Jacob Wilde | 101188310 |
| Mehedi Hasan Rafid | 101180076 |
| Zuri Lane-Griffore | 101241678 |
