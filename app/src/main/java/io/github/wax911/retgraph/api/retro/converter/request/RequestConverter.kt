package io.github.wax911.retgraph.api.retro.converter.request

import com.squareup.moshi.Moshi
import io.github.wax911.library.annotation.processor.GraphProcessor
import io.github.wax911.library.converter.GraphConverter
import io.github.wax911.library.converter.request.GraphRequestConverter
import io.github.wax911.library.model.request.QueryContainer
import io.github.wax911.library.model.request.QueryContainerBuilder
import okhttp3.MediaType
import okhttp3.RequestBody

class RequestConverter(methodAnnotations: Array<Annotation>,
                       graphProcessor: GraphProcessor,
                       moshi: Moshi) : GraphRequestConverter(methodAnnotations, graphProcessor, moshi) {
    /**
     * Converter for the request body, gets the GraphQL query from the method annotation
     * and constructs a GraphQL request body to send over the network.
     * <br></br>
     *
     * @param containerBuilder The constructed builder method of your query with variables
     * @return Request body
     */
    override fun convert(containerBuilder: QueryContainerBuilder): RequestBody {
        val queryContainer = containerBuilder
                .setQuery(graphProcessor.getQuery(methodAnnotations))
                .build()

        // Extending and customizing your requests
        if(REMOVE_NULLS) {
            queryContainer.apply {
                variables.filter { entry: Map.Entry<String, Any?> -> entry.value != null }
            }
        }

        val queryJson = moshi.adapter(QueryContainer::class.java).serializeNulls().toJson(queryContainer)
        return RequestBody.create(MediaType.parse(GraphConverter.MimeType), queryJson)
    }

    companion object {
        private const val REMOVE_NULLS = false
    }
}