package com.flyerssoft.ams.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * The Entitlement class represents an
 * entitlement of an api in the system.
 */
@Data
@Entity
public class Entitlement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  @Column(name = "allowed_method")
  private String allowedMethod;

  @Column(name = "path_pattern")
  private String pathPattern;

}
