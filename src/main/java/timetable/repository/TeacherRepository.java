package timetable.repository;

import timetable.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Integer> {

    @Query(value = """
            SELECT t.t_surname AS surname, t.t_name AS name, t.t_patronymic AS patronymic
            FROM teachers t
            JOIN lessons l ON t.t_id = l.l_teacher_id
            WHERE (l.l_date = :day)""",
            nativeQuery = true)
    List<Map<String, Object>> findTeachersByDay(
            @Param("day") Date day
    );
}
