package io.github.wax911.library.util

import android.util.Log
import com.squareup.moshi.Moshi
import io.github.wax911.library.model.attribute.GraphError
import io.github.wax911.library.model.body.GraphContainer
import retrofit2.Response

/**
 * Converts the response error response into an object.
 *
 * @return The error object, or null if an exception was encountered
 * @see Error
 */
fun Response<*>?.getError(): List<GraphError>? {
    try {
        if (this != null) {
            val responseBody = errorBody()
            val message = responseBody?.string()
            if (responseBody != null && !message.isNullOrBlank()) {
                val graphErrors= message.getGraphQLError()
                if (graphErrors != null)
                    return graphErrors
            }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return null
}

private fun String.getGraphQLError(): List<GraphError>? {
    Log.e("GraphErrorUtil", this)
    val graphContainer = Moshi.Builder().build().adapter(GraphContainer::class.java).fromJson(this)
    return graphContainer?.errors
}

