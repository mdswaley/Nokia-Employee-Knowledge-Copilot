package com.nokia.Nokia.Employee.Knowledge.Copilot.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "uploaded_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String fileName;

    private LocalDateTime uploadedAt;
}
