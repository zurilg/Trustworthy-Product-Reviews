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
The project is currently in the early prototype stage, with several key use cases implemented. The current state of the project attempts to best adhere to the expectations of *Milestone 1: Early prototype*:
- **GitHub Repository Setup:** [Link to Repository](https://github.com/zurilg/Trustworthy-Product-Reviews)
- **Continuous Integration/Deployment (CI/CD):** Workflow is automated via GiHub Actions. Running Java CI with Maven. CD to Azure with each push to the main branch.
- **Ready to Run:** `pom.xml` configured to package and run the Spring Boot application.
- **Kanban Board:** Created using GitHub Projects to track tasks and progress.
- **Scrum Meetings**: Weekly meetings held to discuss progress and next steps. Issues on GitHub used to communicate scrums.
- **UML Class Diagram:** Created to represent core entities and relationships.

Key use cases for *Milestone 1*:
- Users can view a list of all products from the home page (root path `/`).
- Users can select a product to view its details, category, and associated reviews on its dedicated product page (`/product/{UUID}`).
- The product page successfully displays the product's average rating and number of reviews.

---

### UML Class Diagram:
This diagram represents the core entities (`Product`, `Review`, `User`) and their relationships.

![alt text](https://github.com/zurilg/Trustworthy-Product-Reviews/blob/testing-documentation/documentation/UML/Milestone_1-UML_Class_Diagram.png "Class Diagram")

**Key Relationships:**

| Entity | Relationship | Type        | Multiplicity                           |
|:-------|:-------------|:------------|:---------------------------------------|
| Review | Product      | Many-to-One | 0..* reviews associated with 1 product |
| Review | User         | Many-to-One | 0..* reviews written by 1 user          |

**View Full Diagram (PNG):** [Milestone_1-UML_Class_Diagram.png](https://github.com/zurilg/Trustworthy-Product-Reviews/blob/testing-documentation/documentation/UML/Milestone_1-UML_Class_Diagram.png)

**Download Source (Draw.io):** [Milestone_1-UML_Class_Diagram.drawio](https://github.com/zurilg/Trustworthy-Product-Reviews/blob/testing-documentation/documentation/UML/Milestone_1-UML_Class_Diagram.drawio)

---

### Team Members:
| Name | Student Number |
|:-----|:--|
| Damon Ricci | 101229913 |
| Elias Pantazopoulos | 101273477 |
| Jacob Wilde | 101188310 |
| Mehedi Hasan Rafid | 0 |
| Zuri Lane-Griffore | 101241678 |
