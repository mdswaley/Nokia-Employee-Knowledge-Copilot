package com.nokia.Nokia.Employee.Knowledge.Copilot.Service;

import com.nokia.Nokia.Employee.Knowledge.Copilot.DTO.EmployeeDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RAGService {
    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;
    private final ExcelService excelService;


    @Value("classpath:Nokia_Employee_Dataset.xlsx")
    Resource empData;


    public float[] getEmbedding(String text){
        return embeddingModel.embed(text);
    }

    private Document employeeToDocument(EmployeeDTO employee){

        String content = """
            Employee ID: %s
            Name: %s
            Department: %s
            Designation: %s
            Location: %s
            Skills: %s
            Experience: %d years
            Manager: %s
            Certification: %s
            """
                .formatted(
                        employee.employeeId(),
                        employee.name(),
                        employee.department(),
                        employee.designation(),
                        employee.location(),
                        employee.skills(),
                        employee.experienceYears(),
                        employee.manager(),
                        employee.certification()
                );

        return new Document(
                content,
                Map.of(
                        "employeeId", employee.employeeId(),
                        "department", employee.department(),
                        "location", employee.location()
                )
        );
    }

    @PostConstruct
    public void loadEmployees() throws Exception {

        List<EmployeeDTO> employees = excelService.readEmployees(empData);

        List<Document> documents = employees.stream()
                        .map(this::employeeToDocument)
                        .toList();

        vectorStore.add(documents);

        System.out.println("Loaded "
                + documents.size()
                + " employees into Vector Store");
    }


    public ResponseEntity<String> ask(String question) {

        SearchRequest searchRequest = SearchRequest.builder()
                .query(question)
                .topK(50)
                .build();

        List<Document> documents =
                vectorStore.similaritySearch(searchRequest);

        String context = documents.stream()
                        .map(Document::getText)
                        .collect(Collectors.joining("\n"));

        String c1 = chatClient.prompt()
                .system("""
                    You are Nokia Employee Knowledge Copilot.

                    Answer only using the provided employee data.

                    If information is unavailable,
                    say "I could not find that information."
                    """)
                .user("""
                    Context:
                    %s

                    Question:
                    %s
                    """.formatted(context, question))
                .call()
                .content();

        return ResponseEntity.ok(c1);
    }




}
