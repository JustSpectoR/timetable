package timetable.service;

import timetable.model.Lesson;
import timetable.model.Teacher;
import timetable.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetable.repository.TeacherRepository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final TeacherService teacherService;
    private final TeacherRepository teacherRepository;

    // Получение списка всех занятий
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    // Получение занятия по идентификатору
    public Lesson getLessonById(Integer id) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(id);
        return lessonOptional.orElse(null);
    }

    // Добавление нового занятия со случайными данными
    public Lesson addNewLesson() {
        Random rand = new Random();
        Lesson newLesson = new Lesson();

        Time t;
        Date d;
        Teacher teach;

        // Получаем время занятия
        t = switch (rand.nextInt(3)) {
            case 0 -> Time.valueOf("08:00:00");
            case 1 -> Time.valueOf("09:45:00");
            case 2 -> Time.valueOf("11:30:00");
            default -> Time.valueOf("13:30:00");
        };
        newLesson.setTime(t);

        // Получаем дату занятия
        d = switch (rand.nextInt(4)) {
            case 0 -> Date.valueOf("2024-11-25");
            case 1 -> Date.valueOf("2024-11-26");
            case 2 -> Date.valueOf("2024-11-27");
            case 3 -> Date.valueOf("2024-11-28");
            default -> Date.valueOf("2024-11-29");
        };
        newLesson.setDate(d);

        // Ставим случайного учителя из имеющихся
        teach = teacherService.getTeacherById(rand.nextInt((int) teacherRepository.count()));
        newLesson.setTeacherId(teach);
        return newLesson;
    }

    // Удаление занятия по идентификатору
        public void deleteLessonById(Integer id) {
        if (getLessonById(id) != null) {
            lessonRepository.deleteById(id);
        }
    }
}
