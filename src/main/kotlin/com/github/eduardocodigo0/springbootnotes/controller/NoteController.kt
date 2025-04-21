package com.github.eduardocodigo0.springbootnotes.controller

import com.github.eduardocodigo0.springbootnotes.controller.NoteController.NoteResponse
import com.github.eduardocodigo0.springbootnotes.database.model.Note
import com.github.eduardocodigo0.springbootnotes.database.repository.NoteRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/notes")
class NoteController(
    val repository: NoteRepository
) {

    data class NoteRequest(
        val id: String?,
        @field:NotBlank(message = "Title for the note is required")
        val title: String,
        val content: String,
        val color: Long,
    )

    data class NoteResponse(
        val id: String?,
        val title: String,
        val content: String,
        val color: Long,
        val createdAt: Instant
    )

    @PostMapping
    fun save(
        @Valid @RequestBody body: NoteRequest
    ): NoteResponse {
        val ownerId: String = SecurityContextHolder.getContext().authentication.principal as String
        val note = repository.save(
            Note(
                id = body.id?.let { ObjectId(it) } ?: ObjectId.get(),
                title = body.title,
                content = body.content,
                color = body.color,
                createAt = Instant.now(),
                ownerId = ObjectId(ownerId)
            )
        )

        return note.toResponse()
    }

    @GetMapping
    fun findByOwnerId(): List<NoteResponse> {
        val ownerId: String = SecurityContextHolder.getContext().authentication.principal as String

        return repository.findByOwnerId(ObjectId(ownerId)).map {
            it.toResponse()
        }
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById(@PathVariable id: String) {
        val note = repository.findById(ObjectId(id)).orElseThrow {
            throw IllegalArgumentException("Invalid note")
        }
        val ownerId: String = SecurityContextHolder.getContext().authentication.principal as String

        if (ownerId != note.ownerId.toHexString()) {
            throw IllegalArgumentException("You are not authorized to change  this note")
        }

        repository.deleteById(ObjectId(id))
    }
}

private fun Note.toResponse(): NoteResponse {
    return NoteResponse(
        id = id.toHexString(),
        title = title,
        content = content,
        color = color,
        createdAt = createAt
    )
}