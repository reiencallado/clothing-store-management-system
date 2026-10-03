# CCINFOM Database Application  
## 👚 Clothing Store Inventory & Sales Management System

![Course](https://img.shields.io/badge/Course-CCINFOM-blue)
![Institution](https://img.shields.io/badge/Institution-De%20La%20Salle%20University-green)
![Project Type](https://img.shields.io/badge/Project-Type%3A%20Database%20Application-lightgrey)
![Technologies](https://img.shields.io/badge/Technologies-Java%2C%20SQL-blueviolet)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📌 Project Overview

The **Clothing Store Inventory & Sales Management System** is a database-driven application developed to replace manual inventory tracking and sales recording processes—typically handled via Excel—within retail environments. This system is intended as a **comprehensive solution** for managing product inventory, customer records, sales transactions, stock updates, and analytical reporting accurately and efficiently.

By centralizing business data into a structured database model and integrating transactional workflows, this system enhances **operational efficiency**, **data reliability**, and **decision support** for store personnel and managers.

---

## 🎯 Academic & Practical Objectives

The system aims to:

- Provide a **centralized database solution** for managing products, customers, sales reps, and branches  
- Support **real-time transaction recording** and **inventory updates**  
- Generate meaningful **sales and performance reports**  
- Demonstrate effective **database-driven application design** as part of academic requirements  

---

## 🧩 System Architecture

The CCINFOM system is structured around core entities essential to retail operations:

### 🗂️ Major Components

- **Product:** Tracks inventory levels, attributes, categories, and pricing  
- **Customer:** Manages customer details and purchase history  
- **Sales Representative:** Records sales rep assignments and performance  
- **Branch:** Handles store locations and associated staff  
- **Transactions:** Includes sales, returns, restocking, and inventory transfers  

Each module interacts with the underlying relational database to ensure **data integrity**, **consistency**, and **traceability** of business activities.

---

## ✨ Key Features

### 📦 Inventory & Product Management

- Add/update/delete products with attributes like category, size, color, and stock level  
- Automated stock adjustments following sales or restock entries  
- Track discontinued products and inventory thresholds

### 🧍 Customer & Sales Rep Management

- Maintain customer profiles with linked purchase histories  
- Record sales rep details and associate them with branch performance  
- Support analytical breakdowns per representative

### 🧾 Transaction Workflows

- Sell clothing items with validation and stock decrement  
- Process product returns and update inventory accordingly  
- Facilitate stock transfers between branches

### 📊 Reporting

- **Monthly Sales Summary**
- **Product Performance**
- **Sales Representative Performance**
- **Branch Revenue Reports**

These reports support business insights and informed decision making.

---

## ⚙️ Technologies Used

- **Java** – Core application logic  
- **MySQL / SQL Database** – Data persistence and queries  
- **SQL Scripts** – Database schema and relationships  
- **Batch Scripts (`run.bat`, `run.sh`)** – Easy environment initialization

---

## 🛠️ Setup & Installation

### Prerequisites

- Java JDK (version 8 or above)
- MySQL Server
- Terminal / Command Prompt
- Git
- IDE (optional): VSCode, Eclipse or IntelliJ IDEA

### Installation Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/henryagunanne/CCINFOM-DB-APP.git
   ```
2. Navigate to the project directory
   ```bash
   cd CCINFOM-DB-APP
   ```
3. Set up the database
   - Open your MySQL client (MySQL Workbench, CLI, etc.)
   - Import or execute the provided SQL schema files
   - Ensure the database is running
   - Update database credentials in the source code if required


### 📁 Project Structure Reference

```text
CCINFOM-DB-APP/
├── lib/
│   └── mysql-connector-j-9.3.0.jar
├── src/
│   ├── Driver.java
│   └── other source files
├── run.sh
├── run.bat
└── README.md
```

---

## ▶️ Running the Application (With External Libraries)

This application requires the **MySQL Connector/J** library, which is included in the `lib/` folder.  
You may run the system using either the provided scripts or by compiling and executing the program manually.


### Option 1: Run Using the Provided Scripts (Recommended)

For convenience, platform-specific run scripts are included.

#### macOS / Linux
```bash
./run.sh
```

### Windows
```bat
run.bat
```


💡 Ensure the script has execution permission on macOS/Linux:
```bash
chmod +x run.sh
```
or 
```
chmod 744 run.sh
```

---
### Option 2: Compile and Run Manually

Use this option if you prefer full control over the build process or are running in a restricted environment.
Make sure you are in the source directory where Driver.java and the other .java files are located.

### macOS / Linux
Compile:
```bash
javac -cp "../lib/mysql-connector-j-9.3.0.jar:." *.java
```
or 
```bash
javac -cp ".:/absolute/path/to/CCINFOM-DB-APP/lib/mysql-connector-j-9.3.0.jar:." Driver.java
```

Run:
```bash
java -cp "lib/mysql-connector-j-9.3.0.jar:src" Driver
```
or 
```bash
java -cp ".:/absolute/path/to/CCINFOM-DB-APP/libs/*" Driver
```

### Windows
Compile:
```bat
javac -cp "..\lib\mysql-connector-j-9.3.0.jar;." *.java
```
or
```bat
java -cp ".;C:\absolute\path\to\CCINFOM-DB-APP\lib/mysql-connector-j-9.3.0.jar:." Driver.java
```

Run:
```bat
java -cp "lib\mysql-connector-j-9.3.0.jar;src" Driver
```
or 
```bat
java -cp ".;C:\absolute\path\to\CCINFOM-DB-APP\libs\*" Driver
```

### 🔧 Example (macOS)
```bash
javac -cp ".:/Users/yourname/Projects/CCINFOM-DB-APP/libs/*" Driver.java \
&& java -cp ".:/Users/yourname/Projects/CCINFOM-DB-APP/libs/*" Driver
```

### ⚠️ Notes
- The mysql-connector-j-9.3.0.jar file must remain inside the lib/ folder
- Classpath separators differ by OS:
    - `:` for macOS/Linux
    - `;` for Windows
- Ensure MySQL Server is running before launching the application
- Driver is the main entry point of the program

--- 

### 👥 Development Team

Developed collaboratively by:
- Agunanne, Henry
- Adriano, Mark Luis
- Encallado, Edlynn Rei
- Manatad, Francinne
  
All members contributed to system design, implementation, testing, documentation, and integration.

--- 

### 📄 License

This project is licensed under the **MIT License**.  
See the [LICENSE](./LICENSE) file for full details.

---

### ⭐ Acknowledgements

Thanks to the instructors and academic advisors of the CCINFOM program for guidance and support.

---

## 📄 AI Assistance Disclosure

This README file was generated with the assistance of ChatGPT and was reviewed, edited, and verified by the project authors.  
All source code, system design, and implementation were fully developed by the authors. 
