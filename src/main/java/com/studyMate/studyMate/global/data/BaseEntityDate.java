package com.studyMate.studyMate.global.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@SQLRestriction("removed_dt is NULL")
@EntityListeners(AuditingEntityListener.class)
public class BaseEntityDate {
    @CreatedDate
    @Column(updatable = false)
    @Schema(description = "생성일", example = "2024-09-28T16:23:00.00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDt;

    @LastModifiedDate
    @Schema(description = "수정일", example = "2024-09-28T16:23:00.00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastModifiedDt;
}
