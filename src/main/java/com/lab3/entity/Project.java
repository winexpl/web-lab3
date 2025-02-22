package com.lab3.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

import com.lab3.dto.ActiveTasksCount;


@Entity
@Table(name  =  "project",  schema  =  "public",  uniqueConstraints  =  {
    @UniqueConstraint(columnNames = "p_name") })
@NamedNativeQueries({
    @NamedNativeQuery(name = "Project.update",
    query = "UPDATE project SET p_name=:name, p_descr=:descr, p_begin_date=:beginDate, p_end_date=:endDate " + //
                        "WHERE p_id=:id ; "),

    @NamedNativeQuery(name = "Project.findByRangeOfDates",
    query = "SELECT * FROM project WHERE " +
            "(p_begin_date BETWEEN :beginDate AND :endDate) " +
            "AND (p_end_date BETWEEN :beginDate AND :endDate)",
    resultClass = Project.class),

    @NamedNativeQuery(name = "Project.findByString",
    query = "SELECT * FROM project " +
        "WHERE p_name ILIKE '%' || :sample || '%' " +
        "OR p_descr ILIKE '%' || :sample || '%';",
    resultClass = Project.class),

    @NamedNativeQuery(name = "Project.getActiveTasksCountByProject",
    query = "SELECT t_p_id as projectId, count(*) as activeTasksCount FROM task WHERE t_completed = false GROUP BY t_p_id",
    resultSetMapping = "activeTasksCountMapping")
})
@SqlResultSetMapping(
    name = "activeTasksCountMapping",
    classes = @ConstructorResult(
        targetClass = ActiveTasksCount.class,
        columns = {
            @ColumnResult(name = "projectId", type = Integer.class),
            @ColumnResult(name = "activeTasksCount", type = Integer.class)
        }
    )
)
@Data
public class Project {
    @Id
    @Column(name = "p_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "p_name", columnDefinition = "varchar", length = 50,
        nullable = false, unique = true)
    private String name;

    @Column(name = "p_descr", columnDefinition = "varchar", length = 100)
    private String descr;

    @Column(name = "p_begin_date", columnDefinition = "date", nullable = false)
    private LocalDate beginDate;

    @Column(name = "p_end_date", columnDefinition = "date")
    private LocalDate endDate;
}