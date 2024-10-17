package com.basnukaev.finance.app.entity

import jakarta.persistence.MappedSuperclass
import org.hibernate.proxy.HibernateProxy

@MappedSuperclass
abstract class AbstractEntity<ID> {

    abstract val id: ID?

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false

        val otherClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass
            else other.javaClass

        val thisClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass
            else this.javaClass

        if (thisClass != otherClass) return false

        other as AbstractEntity<*>

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode()
        else javaClass.hashCode()
}