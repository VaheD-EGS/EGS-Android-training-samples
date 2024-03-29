package com.egs.ansroid.samples.lesson8.flows.errorhandling

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException


fun materializeImpl() = runBlocking {
    val urlFlow = flowOf("url-1", "url-2", "url-retry")

    val resultFlow = urlFlow
        .map { url -> fetchResult(url) }

//    val resultFlowWithRetry = mapWithRetry(
//        { url -> fetchResult(url) },
//        { value, attempt -> value is Error && attempt < 3L }
//    )

//    val results = resultFlowWithRetry.toList()
//    println("Results: $results")
}

data class Image(val url: String)

suspend fun fetchImage(url: String): Image {
    // Simulate some remote call
    delay(10)

    // Simulate an exception thrown by the server or API
    if (url.contains("retry")) {
        throw IOException("Server returned HTTP response code 503")
    }

    return Image(url)
}

sealed class Result
data class Success(val image: Image) : com.egs.ansroid.samples.lesson8.flows.errorhandling.Result()
data class Error(val url: String) : com.egs.ansroid.samples.lesson8.flows.errorhandling.Result()



private fun <T, R : Any> Flow<T>.mapWithRetry(
    action: suspend (T) -> R,
    predicate: suspend (R, attempt: Long) -> Boolean
) = map { data ->
    var attempt = 0L
    var shallRetry: Boolean
    var lastValue: R? = null
    do {
        val tr = action(data)
        shallRetry = predicate(tr, ++attempt)
        if (!shallRetry) lastValue = tr
    } while (shallRetry)
    return@map lastValue
}


suspend fun fetchResult(url: String): Result {
    println("Fetching $url..")
    return try {
        val image = fetchImage(url)
        Success(image)
    } catch (e: IOException) {
        Error(url)
    }
}


