package com.flyerssoft.ams.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * The attendance entity.
 */
@Getter
@Setter
@Entity
public class Attendance {

  /**
   * primary key of attendance entity.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
    private int attendanceId;

  /**
   * Holds true or false if the user is leaving the office by half a day
   * and vice versa.
   */
  @Column(name = "is_half_day")
    private Boolean isHalfDay;

  /**
   * employee check in timestamp.
   */
  @Column(name = "check_in")
    private long checkIn;

  /**
   * employee check out timestamp.
   */
  @Column(name = "check_out")
    private long checkOut;

  /**
   * holds data where employee is working on that day.
   */
  @Column(name = "place_of_work")
    private String placeOfWork;

  /**
   * hold where employee is working.
   */
  @Column(name = "self_checkout")
    private Boolean isSelfCheckOut;

  /**
   * hold where employee is working.
   */
  @ManyToOne
  @JoinColumn(name = "employee_id")
    private Employee employee;
}
