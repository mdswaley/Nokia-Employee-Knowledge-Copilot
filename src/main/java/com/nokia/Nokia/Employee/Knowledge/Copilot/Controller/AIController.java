package com.nokia.Nokia.Employee.Knowledge.Copilot.Controller;

import com.nokia.Nokia.Employee.Knowledge.Copilot.Service.RAGService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AIController {
    private final RAGService ragService;

    @GetMapping("/ask")
    public ResponseEntity<String> getAsk(@RequestParam String question){
        return ragService.ask(question);
    }

}
