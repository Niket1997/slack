package org.niket.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;
import org.niket.enums.ChannelType;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "channels")
@Where(clause = "deleted_at IS NULL")
public class Channel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private ChannelType channelType;
}
