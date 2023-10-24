package org.niket.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.niket.base.BaseEntity;
import org.niket.enums.ChannelType;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "channels")
public class Channel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private ChannelType channelType;
}
