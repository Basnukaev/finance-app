package com.basnukaev.finance.app.redis.state

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash
class ExpenseState(
    @Id
    override var userId: Long,
    var description: String? = null,
    var amountStr: String? = null,
    var comment: String? = null,
    var categoryName: String? = null,
    var isMandatory: Boolean? = null
) : UserIdKey()