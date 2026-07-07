package com.nokia.Nokia.Employee.Knowledge.Copilot.Service;

import com.nokia.Nokia.Employee.Knowledge.Copilot.DTO.AskRequest;
import com.nokia.Nokia.Employee.Knowledge.Copilot.Entity.EmployeeEntity;
import com.nokia.Nokia.Employee.Knowledge.Copilot.Entity.UploadedFile;
import com.nokia.Nokia.Employee.Knowledge.Copilot.Repository.EmployeeRepository;
import com.nokia.Nokia.Employee.Knowledge.Copilot.Repository.UploadedFileRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final EmployeeRepository employeeRepository;
    private final UploadedFileRepository uploadedFileRepository;


    @Value("classpath:Nokia_Employee_Dataset.xlsx")
    Resource empData;


    public float[] getEmbedding(String text){
        return embeddingModel.embed(text);
    }

    private Document employeeToDocument(EmployeeEntity employee){

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
                        employee.getEmployeeId(),
                        employee.getName(),
                        employee.getDepartment(),
                        employee.getDesignation(),
                        employee.getLocation(),
                        employee.getSkills(),
                        employee.getExperienceYears(),
                        employee.getManager(),
                        employee.getCertification()
                );

        return new Document(
                content,
                Map.of(
                        "employeeId", employee.getEmployeeId(),
                        "department", employee.getDepartment(),
                        "location", employee.getLocation()
                )
        );
    }

    @PostConstruct
    public void loadEmployees() throws Exception {
        String fileName = empData.getFilename();

        List<EmployeeEntity> employees = excelService.readEmployees(empData);

//        save if employee table is empty
        if (employeeRepository.count() == 0) {
            employeeRepository.saveAll(employees);
            System.out.println("Employees saved to database");
        }

        if (uploadedFileRepository.existsByFileName(fileName)) {

            System.out.println(
                    fileName + " already processed. Skipping ingestion."
            );

            return;
        }


        List<Document> documents = employees.stream()
                        .map(this::employeeToDocument)
                        .toList();

        vectorStore.add(documents);

        uploadedFileRepository.save(
                UploadedFile.builder()
                        .fileName(fileName)
                        .uploadedAt(LocalDateTime.now())
                        .build()
        );

        System.out.println("Loaded "
                + documents.size()
                + " employees into Vector Store");
    }


    public ResponseEntity<String> ask(AskRequest req) {
        EmployeeEntity employee =
                employeeRepository.findById(String.valueOf(req.employeeId())).orElseThrow(
                        ()-> new RuntimeException("Employee not exist with id "+req.employeeId()));

        SearchRequest searchRequest = SearchRequest.builder()
                .query(req.question())
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
                    """.formatted(context, req.question()))
                .advisors(
                        SafeGuardAdvisor.builder().sensitiveWords(List.of(
                                "Salary"
                        )).build(),

                        MessageChatMemoryAdvisor.builder(chatMemory).build(),  // for short term msg storage

                        VectorStoreChatMemoryAdvisor.builder(vectorStore)  // for long term msg storage
                                .defaultTopK(4)
                                .build(),

                        QuestionAnswerAdvisor.builder(vectorStore)  // for question and answer purpose from given data source
                                .searchRequest(SearchRequest.builder()
                                        .filterExpression("file_name == 'Nokia_Employee_Dataset.xlsx'")
                                        .topK(4)
                                        .build())
                                .build()

                )
                .advisors(a -> a
                        .param(ChatMemory.CONVERSATION_ID,
                                String.valueOf(employee.getEmployeeId()))
                )
                .call()
                .content();

        return ResponseEntity.ok(c1);
    }



    private void saveEmployeeToEmployeeTable(List<EmployeeEntity> employeeDTOs) {
        List<EmployeeEntity> emp1= employeeDTOs.stream()
                .map(dto -> {
                    EmployeeEntity employee = new EmployeeEntity();

                    employee.setEmployeeId(dto.getEmployeeId());
                    employee.setName(dto.getName());
                    employee.setEmail(dto.getEmail());
                    employee.setPhone(dto.getPhone());
                    employee.setDepartment(dto.getDepartment());
                    employee.setDesignation(dto.getDesignation());
                    employee.setLocation(dto.getLocation());
                    employee.setSkills(dto.getSkills());
                    employee.setExperienceYears(dto.getExperienceYears());
                    employee.setManager(dto.getManager());
                    employee.setJoiningDate(dto.getJoiningDate());
                    employee.setEmploymentType(dto.getEmploymentType());
                    employee.setSalary(dto.getSalary());
                    employee.setCertification(dto.getCertification());

                    return employee;
                })
                .toList();

        employeeRepository.saveAll(emp1);
    }



}
