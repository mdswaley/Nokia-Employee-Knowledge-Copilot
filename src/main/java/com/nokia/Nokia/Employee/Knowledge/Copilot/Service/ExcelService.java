package com.nokia.Nokia.Employee.Knowledge.Copilot.Service;

import com.nokia.Nokia.Employee.Knowledge.Copilot.Entity.EmployeeEntity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {


        public List<EmployeeEntity> readEmployees(Resource resource) throws IOException {

            List<EmployeeEntity> employees = new ArrayList<>();

            try (Workbook workbook = new XSSFWorkbook(resource.getInputStream())) {

                Sheet sheet = workbook.getSheetAt(0);

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                    Row row = sheet.getRow(i);

                    EmployeeEntity employee = new EmployeeEntity(
                            row.getCell(0).getStringCellValue(),
                            row.getCell(1).getStringCellValue(),
                            row.getCell(2).getStringCellValue(),
                            row.getCell(3).getStringCellValue(),
                            row.getCell(4).getStringCellValue(),
                            row.getCell(5).getStringCellValue(),
                            row.getCell(6).getStringCellValue(),
                            row.getCell(7).getStringCellValue(),
                            (int) row.getCell(8).getNumericCellValue(),
                            row.getCell(9).getStringCellValue(),
                            LocalDate.parse(
                                    row.getCell(10).getStringCellValue()
                            ),
                            row.getCell(11).getStringCellValue(),
                            row.getCell(12).getNumericCellValue(),
                            row.getCell(13).getStringCellValue()
                    );

                    employees.add(employee);
                }
            }

            return employees;
        }
}
