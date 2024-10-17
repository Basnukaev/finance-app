package com.basnukaev.finance.app.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractAuditableEntity<T, ID> : AbstractEntity<ID>() {

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    var createdDate: T? = null

    @LastModifiedDate
    @Column(name = "modified_date", nullable = false)
    var modifiedDate: T? = null
}