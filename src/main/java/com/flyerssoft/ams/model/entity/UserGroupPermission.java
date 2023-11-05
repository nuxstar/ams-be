package com.flyerssoft.ams.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The userGroupPermission entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_group_permission")
public class UserGroupPermission {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "group_name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "is_default")
  private Boolean isDefault;

  @ManyToMany(cascade = {
    CascadeType.DETACH,
    CascadeType.MERGE,
    CascadeType.PERSIST,
    CascadeType.REFRESH
  })
  @JoinTable(
      joinColumns = @JoinColumn(name = "user_group_permission_id"),
      inverseJoinColumns = @JoinColumn(name = "entitlement_id")
  )
  private List<Entitlement> entitlements;
}
