package timetable.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "l_id", nullable = false)
    private Integer id;

    @Column(name = "l_date")
    private Date date;

    @Column(name = "l_time")
    private Time time;

    @OneToOne
    @JoinColumn(name = "l_teacher_id", nullable = false, referencedColumnName = "t_id")
    private Teacher teacherId;
}
