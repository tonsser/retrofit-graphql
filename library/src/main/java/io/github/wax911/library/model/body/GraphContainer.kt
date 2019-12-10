package io.github.wax911.library.model.body

import io.github.wax911.library.model.attribute.GraphError
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
data class GraphContainer<T>(
        val data: T?,
        val errors: List<GraphError>?
) { fun isEmpty(): Boolean = data == null }

@Polymorphic
@Serializable
abstract class GraphResponse