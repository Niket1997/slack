package org.niket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.niket.base.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "users")
@Table(indexes = {@Index(name = "idx_email_id", columnList = "emailId", unique = true)})
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String emailId;
    private String bio;
}
