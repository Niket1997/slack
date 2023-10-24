package org.niket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "users")
@Table(indexes = {@Index(name = "idx_users_email_id", columnList = "emailId", unique = true)}) // get user with email id
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String emailId;
    private String bio;
}
