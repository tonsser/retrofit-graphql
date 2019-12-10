package io.github.wax911.retgraph.model

import kotlinx.serialization.Serializable

@Serializable
data class Repository(
        val full_name: String?,
        val name: String?,
        val owner: User?,
        val stargazers_count: Int)
