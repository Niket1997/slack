package org.niket.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "created_at", columnDefinition = "BIGINT", updatable = false)
    private Long createdAt = Instant.now().toEpochMilli();

    @Column(name = "updated_at", columnDefinition = "BIGINT")
    private Long updatedAt = Instant.now().toEpochMilli();

    @Column(name = "deleted_at", columnDefinition = "BIGINT")
    private Long deletedAt;

    public void markUpdated() {
        this.updatedAt = Instant.now().toEpochMilli();
    }

    public void markDeleted() {
        this.deletedAt = Instant.now().toEpochMilli();
    }
}