package com.flyerssoft.ams.model.entity;

import com.flyerssoft.ams.model.entity.enums.LeaveStatus;
import com.flyerssoft.ams.model.entity.enums.LeaveType;
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
 * Represents a Leave entity in the Attendance Management System.
 */
@Getter
@Setter
@Entity
public class Leave {

  /**
   * The unique identifier for the leave.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int leaveId;

  /**
   * The reason for the leave.
   */
  @Column(name = "reason")
  private String leaveReason;

  /**
   * The status of the leave.
   */
  @Column(name = "status")
  private LeaveStatus leaveStatus;

  /**
   * The type of the leave.
   */
  @Column(name = "type")
  private LeaveType leaveType;

  /**
   * The starting date of the leave.
   */
  @Column(name = "fromDate")
  private long leaveFrom;

  /**
   * The ending date of the leave.
   */
  @Column(name = "toDate")
  private long leaveTo;

  /**
   * The employee associated with the leave.
   */
  @ManyToOne
  @JoinColumn(name = "employee_id")
  private Employee employee;

  /**
   * The creation timestamp of the leave.
   */
  @Column(name = "createdAt")
  @CreationTimestamp
  private Date leaveCreatedAt;

  /**
   * The modification timestamp of the leave.
   */
  @Column(name = "modifiedAt")
  @UpdateTimestamp
  private Date leaveModifiedAt;
}
