package io.github.wax911.retgraph.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
        val avatar_url: String?,
        val html_url: String?,
        val login: String?)
