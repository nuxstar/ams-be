package com.flyerssoft.ams.model.repository;

import com.flyerssoft.ams.model.entity.Entitlement;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Entitlement table - Repository interface.
 */
public interface EntitlementRepository extends JpaRepository<Entitlement, Long> {
}
