package com.nokia.Nokia.Employee.Knowledge.Copilot.Repository;

import com.nokia.Nokia.Employee.Knowledge.Copilot.Entity.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
    boolean existsByFileName(String file);
}
