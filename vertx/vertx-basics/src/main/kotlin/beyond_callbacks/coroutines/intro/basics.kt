package beyond_callbacks.coroutines.intro

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**
 * Suspends for a specified time and then returns a greeting message.
 *
 * This function demonstrates the use of Kotlin coroutines for asynchronous programming.
 * It uses `delay` to mimic a long-running task, such as fetching data from a network or a database.
 * The `suspend` modifier indicates that this function is a suspending function,
 * which can be paused and resumed at a later time without blocking a thread.
 *
 * @return A greeting string after a delay.
 */
suspend fun hello(): String {
    delay(1000) // Delays the coroutine for 1 second
    return "Hello!"
}

fun main() {
    runBlocking {
        // The runBlocking coroutine builder is used here to bridge the non-coroutine world of the main function
        // with the coroutine world. It creates a new coroutine and blocks the current thread until the coroutine completes.
        println(hello()) // Calls the suspending function within a coroutine scope
    }
}
