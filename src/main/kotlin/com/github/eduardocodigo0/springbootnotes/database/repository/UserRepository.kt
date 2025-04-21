package com.github.eduardocodigo0.springbootnotes.database.repository

import com.github.eduardocodigo0.springbootnotes.database.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, ObjectId> {
    fun findByEmail(email: String): User?
}