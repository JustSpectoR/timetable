package timetable.controller;

import timetable.model.Teacher;
import timetable.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    public String addTeacher(@RequestBody Teacher teacher) {
        teacherService.saveTeacher(teacher);
        return "Teacher is created";
    }

    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file) {
        try {
            teacherService.saveTeachersFromExcel(file);
            return "Teachers are uploaded";
        } catch (IOException e) {
            return "Error occurred";
        }
    }

    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{id}")
    public Teacher getTeacherById(@PathVariable Integer id) {
        return teacherService.getTeacherById(id);
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> getTeacherReport() {
        try {
            byte[] report = teacherService.generateTeacherReport();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=Teacher_Report.docx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                    .body(report);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @DeleteMapping("/{id}")
    public String deleteTeacher(@PathVariable Integer id) {
        if (teacherService.getTeacherById(id) != null) {
            teacherService.deleteTeacherById(id);
            return "Teacher is deleted";
        }
        return "Teacher not found";
    }
}
