package com.flyerssoft.ams.mapper;

import com.flyerssoft.ams.model.dto.CustomPageDto;
import com.flyerssoft.ams.model.dto.project.ProjectDto;
import com.flyerssoft.ams.model.dto.project.ProjectResponseDto;
import com.flyerssoft.ams.model.entity.Project;
import java.util.List;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

/**
 * The project mapper interface.
 *
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper {

  Project toEntity(ProjectDto projectDto);

  ProjectDto toDto(Project project);

  List<ProjectResponseDto> toDto(List<Project> project);

  /**
   * To return custom page dto.
   *
   * @param page page
   * @return project list
   */
  default CustomPageDto toPageDto(Page<Project> page) {

    List<ProjectResponseDto> dtoList = toDto(page.getContent());
    CustomPageDto pageDto = new CustomPageDto();
    pageDto.setContent(dtoList);
    pageDto.setTotalPages(page.getTotalPages());
    pageDto.setNumberOfElements(page.getNumberOfElements());
    pageDto.setTotalElements(page.getTotalElements());
    pageDto.setPage(page.getNumber());
    pageDto.setOffset(page.getSize());
    return pageDto;
  }

  ProjectResponseDto toResponseDto(Project project);
}
