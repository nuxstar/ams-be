package com.flyerssoft.ams.model.repository;

import com.flyerssoft.ams.model.entity.UserGroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserGroupPermission table - Repository interface.
 */
public interface UserGroupPermissionRepository extends JpaRepository<UserGroupPermission, Long> {
}
