package com.flyerssoft.ams.model.entity.enums;

import lombok.Getter;

/**
 * The task status class.
 */
@Getter
public enum TaskStatus {

  /**
   * The "TODO" status for a task.
   */
  TODO("todo"),

  /**
   * The "COMPLETED" status for a task.
   */
  COMPLETED("completed"),

  /**
   * The "PROGRESS" status for a task.
   */
  PROGRESS("in-progress");

  /**
   * Holds task status.
   */
  private final String message;

  /**
   * Constructs a new TaskStatus with the specified taskStatus.
   *
   * @param taskStatus The status taskStatus.
   */
  TaskStatus(final String taskStatus) {
    this.message = taskStatus;
  }

  /**
   * Returns the status message of the TaskStatus.
   *
   * @return The status message.
   */
  public String getMessage() {
    return message;
  }
}
