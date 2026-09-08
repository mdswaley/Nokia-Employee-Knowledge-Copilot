# 🤖 Nokia Employee Knowledge Copilot

> 🚧 **Proof of Concept (POC)**

An AI-powered employee knowledge assistant built with **Spring Boot, Spring AI, PostgreSQL, pgvector, Ollama, and Redis**. The POC demonstrates how employees can interact with organizational employee data using natural language instead of manually searching through Excel files.

## 📌 Project Overview

In many organizations, employee information is maintained in Excel files by HR teams and managers. Finding specific information can become time-consuming when users have to manually open spreadsheets, filter rows, search for skills, and combine information from multiple files.

For example:

* 🔎 Who has experience with Spring Boot and Kafka?
* 👨‍💻 Find Java developers with more than 5 years of experience.
* 📍 Which employees are located in Bangalore?
* ☁️ Who has AWS certifications?
* 👥 Who works under a particular manager?

The **Nokia Employee Knowledge Copilot** POC demonstrates how Generative AI and Retrieval-Augmented Generation (RAG) can simplify this process.

## 🎯 Problem Statement

Traditional Excel-based employee searches depend heavily on manual filtering and keyword matching. As the number of employees and Excel files increases, finding relevant information becomes increasingly difficult and time-consuming.

This POC aims to provide a **natural-language interface** for querying employee information.

Instead of manually searching Excel:

```text
Excel → Filter → Search → Combine Results
```

Users can simply ask:

```text
"Give me employees having more than 5 years of experience with Java skills."
```

The system retrieves relevant employee information and generates an AI-powered response.

## 💡 Proposed Solution

The application follows a **Retrieval-Augmented Generation (RAG)** architecture.

1. 📄 Employee data is uploaded through Excel files.
2. 📊 Apache POI reads and processes the Excel data.
3. 🧩 Employee records are converted into documents.
4. 🧠 Spring AI generates vector embeddings.
5. 🗄️ Embeddings are stored in PostgreSQL using pgvector.
6. 💬 Users ask questions using natural language.
7. 🔎 Relevant employee information is retrieved using vector similarity search.
8. 🤖 Ollama generates the final response using the retrieved context.
9. ⚡ Redis caches frequently repeated questions to avoid unnecessary LLM calls.

## 🏗️ Architecture

```text
                         👤 User
                           │
                           ▼
                    ┌───────────────┐
                    │ REST API      │
                    │ AIController  │
                    └───────┬───────┘
                            │
                            ▼
                    ┌───────────────┐
                    │  RAGService   │
                    └───────┬───────┘
                            │
                  ┌─────────┴─────────┐
                  │                   │
                  ▼                   ▼
             ⚡ Redis             🗄️ pgvector
             Cache                Vector Search
                  │                   │
             Cache Hit?               ▼
                  │             Relevant Context
                  │                   │
                  │                   ▼
                  │             🧠 Spring AI
                  │                   │
                  │                   ▼
                  │                🤖 Ollama
                  │                   │
                  │                   ▼
                  │              AI Response
                  │                   │
                  └─────────┬─────────┘
                            ▼
                         👤 User
```

## 📄 Excel Data Ingestion Flow

```text
Excel File
    │
    ▼
ExcelController
    │
    ▼
ExcelService
    │
    │ Apache POI
    ▼
EmployeeDTO
    │
    ▼
Spring AI EmbeddingModel
    │
    ▼
Ollama
(nomic-embed-text)
    │
    ▼
PostgreSQL + pgvector
```

## 💬 Question Answering Flow

```text
User Question
      │
      ▼
AIController
      │
      ▼
RAGService
      │
      ▼
Check Redis
   │       │
   │       └── Cache HIT ──► Return cached response
   │
   └── Cache MISS
           │
           ▼
      pgvector Search
           │
           ▼
    Relevant Employee Data
           │
           ▼
      Build RAG Prompt
           │
           ▼
         Ollama
           │
           ▼
      Generated Answer
           │
           ▼
       Store in Redis
           │
           ▼
         Response
```

## 🛠️ Tech Stack

| Technology     | Purpose                              |
| -------------- | ------------------------------------ |
| ☕ Java         | Application development              |
| 🌱 Spring Boot | Backend framework                    |
| 🧠 Spring AI   | AI/RAG integration                   |
| 🤖 Ollama      | Local LLM and embedding model        |
| 🗄️ PostgreSQL | Database                             |
| 🔎 pgvector    | Vector storage and similarity search |
| ⚡ Redis        | AI response caching                  |
| 📊 Apache POI  | Excel file processing                |
| 🌐 REST API    | Client-server communication          |
| 📦 Maven       | Dependency management                |

## ✨ Key Features

* 📄 Excel employee data ingestion
* 🔎 Semantic employee search
* 💬 Natural-language question answering
* 🧠 Retrieval-Augmented Generation (RAG)
* 🗄️ Vector storage using PostgreSQL + pgvector
* 🤖 Local LLM inference using Ollama
* ⚡ Redis caching for repeated questions
* ♻️ Duplicate Excel ingestion prevention
* 🧾 Chat memory support
* 🔐 Designed as a foundation for future enterprise features

## ⚡ Redis Caching

To reduce unnecessary LLM calls, the POC uses Redis to cache AI responses.

For example:

```text
First Request:

"Who knows Java?"
        │
        ▼
     Redis MISS
        │
        ▼
     Ollama
        │
        ▼
    AI Response
        │
        ▼
      Redis
```

When the same question is asked again:

```text
"Who knows Java?"
        │
        ▼
     Redis HIT
        │
        ▼
 Cached Response
```

This reduces response time and avoids unnecessary model inference.

## 🗂️ Project Structure

```text
src/main/java
│
└── com.nokia.Nokia.Employee.Knowledge.Copilot
    │
    ├── Config
    │   └── AIConfig.java
    │
    ├── Controller
    │   ├── AIController.java
    │   └── ExcelController.java
    │
    ├── DTO
    │   ├── EmployeeDTO.java
    │   └── AskRequest.java
    │
    └── Service
        ├── RAGService.java
        └── ExcelService.java
```

## 🚀 Future Enhancements

This project is currently a **POC**. The architecture can be extended into a production-ready enterprise platform.

Potential improvements include:

* 🧩 Microservices architecture
* 🔐 Role-based access control
* 👥 HR / Manager / Employee-specific access
* 📊 Workforce analytics dashboard
* 📁 Support for multiple employee data sources
* 🔎 Hybrid search using SQL + vector search
* 📈 Advanced employee analytics
* ☁️ Cloud deployment
* 🔄 Event-driven Excel ingestion
* 🛡️ Enterprise security and audit logging

## 🎓 Purpose of the POC

The main purpose of this POC is to demonstrate the practical implementation of:

* Spring AI
* Retrieval-Augmented Generation (RAG)
* Vector databases
* Embeddings
* Local LLM integration
* Redis-based AI response caching
* Excel data ingestion
* Natural-language querying

The current implementation focuses on validating the **AI workflow and architecture** before evolving the solution into a production-ready enterprise application.

## 👨‍💻 Author

**MD Swaley**

🔗 [Nokia Employee Knowledge Copilot](https://github.com/mdswaley/Nokia-Employee-Knowledge-Copilot/tree/main)
