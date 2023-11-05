package com.flyerssoft.ams.model.entity;

import com.flyerssoft.ams.model.entity.enums.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Represents a task in the system.
 */
@Getter
@Setter
@Entity
public class Task {

  /**
   * The unique ID of the task.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int taskId;

  /**
   * The name of the task.
   */
  @Column(name = "name")
  private String taskName;

  /**
   * The description of the task.
   */
  @Column(name = "description")
  private String taskDescription;

  /**
   * The duration of the task in hours.
   */
  @Column(name = "duration")
  private short taskDuration;

  /**
   * The status of the task.
   */
  @Column(name = "status")
  private TaskStatus taskStatus;

  /**
   * The employee assigned to the task.
   */
  @ManyToOne
  @JoinColumn(name = "employee_id", nullable = false)
  private Employee employee;

  /**
   * The project associated with the task.
   */
  @ManyToOne
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  /**
   * The date and time when the task was created.
   */
  @Column(name = "created_at")
  @CreationTimestamp
  private Date taskCreatedDate;

  /**
   * The date and time when the task was last modified.
   */
  @Column(name = "modified_at")
  @UpdateTimestamp
  private Date taskModifiedDate;

}
