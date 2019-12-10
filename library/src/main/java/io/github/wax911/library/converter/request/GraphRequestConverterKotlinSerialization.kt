package io.github.wax911.library.converter.request

import android.util.Log
import com.squareup.moshi.Moshi
import io.github.wax911.library.annotation.processor.GraphProcessor
import io.github.wax911.library.converter.GraphConverterKotlinxSerialization
import io.github.wax911.library.converter.GraphConverterMoshi
import io.github.wax911.library.model.request.QueryContainer
import io.github.wax911.library.model.request.QueryContainerBuilder
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter

/**
 * GraphQL request body converter and injector, uses method annotation for a given retrofit call
 */
open class GraphRequestConverterKotlinSerialization(
        protected val methodAnnotations: Array<Annotation>,
        protected val graphProcessor: GraphProcessor
) : Converter<QueryContainerBuilder, RequestBody> {

    /**
     * Converter for the request body, gets the GraphQL query from the method annotation
     * and constructs a GraphQL request body to send over the network.
     * <br></br>
     *
     * @param containerBuilder The constructed builder method of your query with variables
     * @return Request body
     */
    @UnstableDefault
	override fun convert(containerBuilder: QueryContainerBuilder): RequestBody {
	    val queryContainer = getQueryContainer(containerBuilder)
		val queryJson = Json.stringify(QueryContainer.serializer(), queryContainer)
	    return RequestBody.create(MediaType.parse(GraphConverterKotlinxSerialization.MimeType), queryJson)
    }

	protected fun getQueryContainer(containerBuilder: QueryContainerBuilder) : QueryContainer{
		return graphProcessor.getQuery(methodAnnotations)
				?.let { query ->
					containerBuilder
							.setQuery(query)
							.apply {
								// if no operation name was set then set the one in the query
								if (!hasOperationName()) {
									val operationName = GraphProcessor.getOperationName(query)
									setOperationName(operationName)
								}
							}
							.build()
				} ?: containerBuilder.build()
	}
}