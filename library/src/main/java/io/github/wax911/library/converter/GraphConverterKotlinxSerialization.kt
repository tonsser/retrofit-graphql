package io.github.wax911.library.converter

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.wax911.library.annotation.processor.GraphProcessor
import io.github.wax911.library.converter.request.GraphRequestConverter
import io.github.wax911.library.converter.response.GraphResponseConverter
import io.github.wax911.library.model.request.QueryContainerBuilder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Created by max on 2017/10/22.
 * Body for GraphQL requests and responses, closed for modification
 * but open for extension.
 *
 * Protected constructor because we want to make use of the
 * Factory Pattern to create our converter
 * </br></br>
 *
 * @param context Any valid application context
 */

open class GraphConverterKotlinxSerialization protected constructor(context: Context?) : Converter.Factory() {

    protected val graphProcessor: GraphProcessor by lazy {
        GraphProcessor.getInstance(context?.assets)
    }

    protected lateinit var moshi: Moshi
    protected lateinit var converterFactory: Converter.Factory

    /**
     * Response body converter delegates logic processing to a child class that handles
     * wrapping and deserialization of the json response data.
     * @see GraphResponseConverter
     * <br></br>
     *
     *
     * @param annotations All the annotation applied to the requesting Call method
     * @see retrofit2.Call
     *
     * @param retrofit The retrofit object representing the response
     * @param type The generic type declared on the Call method
     */
    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        return when (type) {
            is ResponseBody -> super.responseBodyConverter(type, annotations, retrofit)
            else -> converterFactory.responseBodyConverter(type, annotations, retrofit)
        }
    }

    /**
     * Response body converter delegates logic processing to a child class that handles
     * wrapping and deserialization of the json response data.
     * @see GraphRequestConverter
     * <br></br>
     *
     *
     * @param parameterAnnotations All the annotation applied to request parameters
     * @param methodAnnotations All the annotation applied to the requesting method
     * @param retrofit The retrofit object representing the response
     * @param type The type of the parameter of the request
     */
    override fun requestBodyConverter(
            type: Type?,
            parameterAnnotations: Array<Annotation>,
            methodAnnotations: Array<Annotation>,
            retrofit: Retrofit?): Converter<QueryContainerBuilder, RequestBody>? {
        return GraphRequestConverter(methodAnnotations, graphProcessor, moshi)
    }


    companion object {

        const val MimeType = "application/graphql"
        val responseContentType: MediaType = MediaType.get("application/json")

        /**
         * Allows you to provide your own Gson configuration which will be used when serialize or
         * deserialize response and request bodies.
         *
         * @param context any valid application context
         * @param gson custom gson implementation
         */
        fun create(context: Context?, moshi: Moshi? = null, converterFactory: Converter.Factory? = null): GraphConverterKotlinxSerialization {
            return GraphConverterKotlinxSerialization(context).apply {
                this.moshi = moshi ?: Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()

                this.converterFactory = converterFactory ?: Json(
                        JsonConfiguration(
                                strictMode = false,
                                prettyPrint = true
                        )
                ).asConverterFactory(responseContentType)
            }
        }
    }
}