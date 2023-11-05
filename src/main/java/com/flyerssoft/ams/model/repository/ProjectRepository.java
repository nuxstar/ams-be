package com.flyerssoft.ams.model.repository;

import com.flyerssoft.ams.model.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The project repository.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

  @Query(value = "SELECT * FROM project p JOIN employee_project "
      + " e ON p.id= e.project_id  WHERE e.employee_id =:employee_id", nativeQuery = true)
  Page<Project> findAllByEmployeeId(@Param("employee_id") int id, Pageable pageable);
}
