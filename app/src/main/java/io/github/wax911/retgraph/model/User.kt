package io.github.wax911.retgraph.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
        val avatar_url: String? = null,
        val html_url: String? = null,
        val login: String? = null)
