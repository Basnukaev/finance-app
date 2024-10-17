package com.basnukaev.finance.app.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
class Expense(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    var description: String,

    var amount: Double,

    var comment: String? = null,

    var isMandatory: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "category_id")
    var category: Category,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User,

    var timestamp: Instant = Instant.now()
) : AbstractEntity<Long>()
