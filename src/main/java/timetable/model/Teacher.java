package timetable.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "t_id", nullable = false)
    private Integer id;

    @Column(name = "t_surname", length = 30)
    private String Surname;

    @Column(name = "t_name", length = 30)
    private String Name;

    @Column(name = "t_patronymic", length = 30)
    private String Patronymic;
}
