package com.basnukaev.finance.app.repository

import com.basnukaev.finance.app.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CategoryRepository : JpaRepository<Category, Long> {

    fun findByName(categoryName: String): Category?

    @Query("""
    SELECT c
    FROM Category c
    WHERE c.id < 11
    """)
    fun getBaseCategories(): List<Category>

}