package io.github.wax911.retgraph.model

import kotlinx.serialization.Serializable

@Serializable
data class Repository(
        val full_name: String? = null,
        val name: String? = null,
        val owner: User? = null,
        val stargazers_count: Int)
