package com.nokia.Nokia.Employee.Knowledge.Copilot.Controller;

import com.nokia.Nokia.Employee.Knowledge.Copilot.DTO.AskRequest;
import com.nokia.Nokia.Employee.Knowledge.Copilot.Service.RAGService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AIController {
    private final RAGService ragService;

    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestBody AskRequest request) {
        return ragService.ask(request);
    }

}
