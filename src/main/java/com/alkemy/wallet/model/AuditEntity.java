package com.alkemy.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
/*
@EntityListeners( AuditingEntityListener.class )
    TODO: Enable Audit JPA
 */
@JsonIgnoreProperties( value = { "createdAt", "createdBy", "updatedAt", "updatedBy" } )
public class AuditEntity {

    @CreatedDate
    @Column( updatable = false )
    private LocalDateTime createdAt;

    @CreatedBy
    @Column( updatable = false )
    private String createdBy;

    @LastModifiedDate
    @Column( insertable = false )
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column( insertable = false )
    private String updatedBy;
}
