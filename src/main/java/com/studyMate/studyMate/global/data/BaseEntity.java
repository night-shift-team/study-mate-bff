package com.studyMate.studyMate.global.data;

import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

public class BaseEntity extends BaseEntityDate {
    @CreatedBy
    @Column(updatable = false)
    private long createdBy;

    @LastModifiedBy
    private long modifiedBy;
}
