package com.flyerssoft.ams.model.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * The project dto.
 */
@Getter
@Setter
public class ProjectDto {

  private int projectId;

  @NotBlank(message = "Project name is mandatory")
  @Pattern(regexp = "^[A-Za-z0-9 ]+$",
      message = "Project name must not contain special characters & numerics")
  @Size(min = 3, max = 30, message = "Project name size must be between 3 to 30")
  private String projectName;

}
