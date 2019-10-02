package io.github.wax911.library.converter.request

import android.util.Log
import com.squareup.moshi.Moshi
import io.github.wax911.library.annotation.processor.GraphProcessor
import io.github.wax911.library.converter.GraphConverter
import io.github.wax911.library.model.request.QueryContainer
import io.github.wax911.library.model.request.QueryContainerBuilder
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter

/**
 * GraphQL request body converter and injector, uses method annotation for a given retrofit call
 */
open class GraphRequestConverter(
        protected val methodAnnotations: Array<Annotation>,
        protected val graphProcessor: GraphProcessor,
        protected val moshi: Moshi
) : Converter<QueryContainerBuilder, RequestBody> {

    /**
     * Converter for the request body, gets the GraphQL query from the method annotation
     * and constructs a GraphQL request body to send over the network.
     * <br></br>
     *
     * @param containerBuilder The constructed builder method of your query with variables
     * @return Request body
     */
    override fun convert(containerBuilder: QueryContainerBuilder): RequestBody {
	    val queryContainer = getQueryContainer(containerBuilder)
	    val queryJson = moshi.adapter(QueryContainer::class.java).serializeNulls().toJson(queryContainer)
	    Log.d("GraphRequestConverter", queryJson)
	    return RequestBody.create(MediaType.parse(GraphConverter.MimeType), queryJson)
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