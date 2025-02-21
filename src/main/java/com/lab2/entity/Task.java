package com.lab2.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "task", schema = "public")
@NamedNativeQueries({
    @NamedNativeQuery(name = "Task.update",
    query = "UPDATE task SET t_name=:name, t_descr=:descr, t_completed=:completed, t_end_date=endDate " + //
                        "WHERE t_p_id=:projectId, t_id=:taskId;"),

    @NamedNativeQuery(name = "Task.deleteCompleted",
    query = "DELETE FROM task WHERE t_p_id=:projectId AND t_completed=true"),

    @NamedNativeQuery(name = "Task.deleteAllByProjectId",
    query = "DELETE FROM task WHERE t_p_id=:projectId")
})
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "t_id", nullable = false)
    private int id;

    @Column(name = "t_p_id", nullable = false)
    private int projectId;

    @Column(name = "t_name", columnDefinition = "varchar", length = 50,
        nullable = false, unique = true)
    private String name;

    @Column(name = "t_descr", columnDefinition = "varchar", length = 100)
    private String descr;

    @Column(name = "t_completed", columnDefinition = "boolean")
    private boolean completed;

    @Column(name = "t_end_date", columnDefinition = "date")
    private LocalDate endDate;
}