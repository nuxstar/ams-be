package com.flyerssoft.ams.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The project entity.
 */
@Getter
@Setter
@Entity
public class Project {

  /**
   * primary key id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int projectId;

  /**
   * The name of the project.
   */
  @Column(name = "project_name", unique = true)
  private String projectName;

  /**
   * The number of employees associated with the project.
   */
  @Column(name = "employees_count")
  private short employeesCount;

  /**
   * List of Employees under that project.
   */
  @ManyToMany(
      cascade = {
          CascadeType.PERSIST,
          CascadeType.MERGE,
          CascadeType.REFRESH,
          CascadeType.DETACH
      }
  )
  @JoinTable(
      name = "employee_project",
      joinColumns = @JoinColumn(
          name = "project_id"
      ),
      inverseJoinColumns = @JoinColumn(
          name = "employee_id"
      )
  )
  private List<Employee> employees;

  /**
   * Non duplicated list of tasks under that project.
   */
  @OneToMany(mappedBy = "project")
  private Set<Task> task;

  /**
   * The date when the project was created.
   */
  @Column(name = "createdAt")
  @CreationTimestamp
  private Date projectCreatedDate;

  /**
   * The date when the project was last modified.
   */
  @Column(name = "modifiedAt")
  @UpdateTimestamp
  private Date projectModifiedDate;

}
