package io.github.wax911.retgraph.model.container

import androidx.annotation.StringDef
import io.github.wax911.library.model.body.GraphResponse
import io.github.wax911.retgraph.model.parent.Entry
import kotlinx.serialization.Serializable

@Serializable
data class TrendingFeed(val feed: List<Entry>? = null) : GraphResponse() {

    @StringDef(TrendingFeed.HOT, TrendingFeed.NEW, TrendingFeed.TOP)
    internal annotation class FeedType

    companion object {
        // https://api.githunt.com/graphiql feed types, represented as StringDef instead of enums
        const val HOT = "HOT"
        const val NEW = "NEW"
        const val TOP = "TOP"
    }
}
