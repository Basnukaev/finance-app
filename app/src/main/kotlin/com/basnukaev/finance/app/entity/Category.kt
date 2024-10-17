package com.basnukaev.finance.app.entity

import jakarta.persistence.*

@Entity
class Category(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    var name: String,

    @ManyToOne
    @JoinColumn(name = "parent_id")
    var parent: Category? = null
) : AbstractEntity<Long>()