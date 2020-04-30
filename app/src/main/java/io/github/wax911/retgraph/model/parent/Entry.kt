package io.github.wax911.retgraph.model.parent

import io.github.wax911.retgraph.model.Repository
import io.github.wax911.retgraph.model.User
import io.github.wax911.retgraph.model.Vote
import kotlinx.serialization.Serializable

@Serializable
data class Entry(
        val id: Long,
        val vote: Vote? = null,
        val score: Double? = null,
        val postedBy: User? = null,
        val hotScore: Double,
        val repository: Repository? = null)
