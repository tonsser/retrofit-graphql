package io.github.wax911.library.model.attribute

import kotlinx.serialization.Serializable

@Serializable
data class GraphError(
        val message: String?,
        val status: Int,
        val locations: List<Map<String, Int>>?
) {

    override fun toString(): String {
        return "GraphError{" +
                "message='" + message + '\''.toString() +
                ", status=" + status +
                ", locations=" + locations +
                '}'.toString()
    }
}
