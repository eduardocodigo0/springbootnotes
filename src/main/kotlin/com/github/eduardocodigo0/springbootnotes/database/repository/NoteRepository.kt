package com.github.eduardocodigo0.springbootnotes.database.repository

import com.github.eduardocodigo0.springbootnotes.database.model.Note
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface NoteRepository: MongoRepository<Note, ObjectId> {
    fun findByOwnerId(ownerId: ObjectId): List<Note>
}