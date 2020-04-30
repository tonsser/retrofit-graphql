package io.github.wax911.library.model.request

import com.squareup.moshi.Moshi

/**
 * Query & Variable builder for URL parameter based GET requests
 */
class PersistedQueryUrlParameterBuilder(
	private val queryContainer: QueryContainer = QueryContainer(),
	private val moshi: Moshi) {

    fun build(): PersistedQueryUrlParameters {
        return PersistedQueryUrlParameters(
                extensions = moshi.adapter<MutableMap<String, Any?>>(queryContainer.extensions::class.java).toJson(queryContainer.extensions),
                operationName = queryContainer.operationName.orEmpty(),
                variables = moshi.adapter<MutableMap<String, Any?>>(queryContainer.extensions::class.java).toJson(queryContainer.variables)
        )
    }

    companion object {
        const val EXTENSION_KEY_APQ = "persistedQuery"
    }
}