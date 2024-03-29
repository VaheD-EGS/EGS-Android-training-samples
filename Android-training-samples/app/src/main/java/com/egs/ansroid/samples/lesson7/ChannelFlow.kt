package com.egs.ansroid.samples.lesson7

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

fun getMessageRealFlow(): Flow<Lesson7.Message> = channelFlow {
    launch(Dispatchers.IO) {
        while (!isClosedForSend) {
            val messages = getLastMessages()
            messages.forEach {
                send(it)
            }
            delay(100)
        }
    }
}

private suspend fun getLastMessages(): List<Lesson7.Message> {
    /* Simulate network query */
    delay(150)
    return listOf() // a real app would return a non-empty list
}