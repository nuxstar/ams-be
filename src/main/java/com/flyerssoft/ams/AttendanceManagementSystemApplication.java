package com.flyerssoft.ams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * The main application class for Attendance Management System.
 */
@SpringBootApplication
@EnableFeignClients
public class AttendanceManagementSystemApplication {

  /**
   * The entry point of the application.
   *
   * @param args The command line arguments.
   */
  public static void main(final String[] args) {
    SpringApplication.run(AttendanceManagementSystemApplication.class, args);
  }
}
