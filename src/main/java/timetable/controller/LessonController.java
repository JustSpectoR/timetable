package timetable.controller;

import timetable.model.Lesson;
import timetable.service.LessonService;
import timetable.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final LessonRepository lessonRepository;

    @PostMapping("/create")
    public String createLesson() {
        lessonRepository.save(lessonService.addNewLesson());
        return "Lessons are created";
    }

    @GetMapping
    public List<Lesson> getAllLessons() {
        return lessonService.getAllLessons();
    }

    @GetMapping("/{id}")
    public Lesson getLessonById(@PathVariable Integer id) {
        return lessonService.getLessonById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteLesson(@PathVariable Integer id) {
        if (lessonService.getLessonById(id) != null) {
            lessonService.deleteLessonById(id);
            return "Lesson is deleted";
        }
        return "Lesson not found";
    }
}
