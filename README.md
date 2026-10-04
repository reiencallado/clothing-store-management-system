# Clothing Store Management System

A database-driven Java Swing application for managing a clothing retail business. This project demonstrates practical database design, CRUD operations, reporting, and a modular desktop interface built around a MySQL back end.

This repository is designed to showcase strong fundamentals in database management, Java application development, and business process modeling—making it a strong portfolio project for software development and data-driven application work.

## Project Overview

The Clothing Store Management System helps manage core retail operations such as:

- Product inventory and stock levels
- Customer records and membership status
- Sales representative assignments
- Branch records and locations
- Sales transactions
- Business reports and summaries

The application presents these functions in a desktop GUI, with MySQL serving as the source of truth for all data.

## Why This Project

This project is a strong example of:

- Relational database design
- Java Swing GUI development
- JDBC integration with MySQL
- CRUD workflows in a business context
- Data consistency and reporting across multiple entities

It reflects a realistic retail scenario where business data must be organized, updated, and reported from a central system.

## Features

- Product management for clothing items, categories, sizes, colors, and prices
- Customer registration and membership tracking
- Sales rep management by branch
- Branch management with location and contact details
- Sales transaction entry and processing
- Report viewing for operational insights
- MySQL-backed persistence with schema-driven data models

## Tech Stack

- Java SE
- Java Swing (desktop GUI)
- MySQL
- JDBC (MySQL Connector/J)
- SQL schema and sample data

## Project Structure

```text
.
├── src/                              # Java source files
│   ├── ClothingStoreApp.java         # Main application entry and panel navigation
│   ├── DBConnection.java             # MySQL connection setup
│   ├── MainMenuPanel.java            # Navigation menu
│   ├── ProductPanel.java             # Product management UI
│   ├── CustomerPanel.java            # Customer management UI
│   ├── SalesRepPanel.java            # Sales rep management UI
│   ├── BranchPanel.java              # Branch management UI
│   ├── SalesTransactionPanel.java    # Sales transaction UI
│   ├── ReportsPanel.java             # Reporting view
│   └── ...
├── lib/                              # Project dependencies
│   ├── DBclothing.sql                # MySQL schema and seed data
│   └── mysql-connector-j-9.3.0.jar  # JDBC driver
├── SQL-related-files/                # Additional SQL-related assets
├── ClothingStoreManagement_schemaGuide.pdf
├── run.bat                          # Windows launch script
├── run.sh                           # macOS/Linux launch script
└── README.md                        # Project documentation
```

## Database Design

The application uses a relational database modeled around a clothing retail business. Core entities include:

- Branch
- Product
- Customer
- Member
- SalesRep
- SalesTransaction

The database schema is included in [lib/DBclothing.sql](lib/DBclothing.sql), along with sample data for development and testing.

## Prerequisites

Before running the project, ensure you have:

- Java JDK 8+ (recommended: JDK 17+)
- MySQL Server installed and running
- MySQL Connector/J available in the project
- A database named `DBclothing`

## Setup and Run

### 1. Import the database schema

Import the SQL script from [lib/DBclothing.sql](lib/DBclothing.sql) into MySQL.

If your local MySQL credentials differ from the defaults in [src/DBConnection.java](src/DBConnection.java), update the connection settings before running the app.

### 2. Run the application

#### Windows

```bat
run.bat
```

#### macOS/Linux

```bash
chmod +x run.sh
./run.sh
```

### 3. Enter database password when prompted

The app is configured to connect to MySQL using the default username `root`, and it will prompt for the database password if needed.

## Example Use Cases

- Add and update clothing products in inventory
- Track active customers and membership records
- Assign sales reps to branches
- Record customer purchases and new sales transactions
- Review inventory and transaction summaries in reports
