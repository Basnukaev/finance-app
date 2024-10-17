package com.basnukaev.finance.app.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "`user`")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    var telegramUserId: Long? = null,

    var telegramUsername: String? = null,

    var languageCode: String? = null,

    var firstName: String? = null,

    var lastName: String? = null,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_category",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    var categories: MutableList<Category> = ArrayList(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    var expenses: MutableList<Expense> = ArrayList()
) : AbstractAuditableEntity<Instant, Long>()
