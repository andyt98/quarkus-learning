package beyond_callbacks.coroutines.intro

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    // Launching a new coroutine that executes in parallel to the rest of the code.
    // This coroutine simply introduces a delay of 500 milliseconds.
    val job1 = launch {
        delay(500) // Non-blocking delay for 500ms
    }

    // A function to compute the nth Fibonacci number. This is a computationally intensive task.
    fun fib(n: Long): Long = if (n < 2) n else fib(n - 1) + fib(n - 2)

    // Launching another coroutine using `async`. This starts a background computation
    // for the 42nd Fibonacci number and returns a Deferred result.
    val job2 = async {
        fib(42)
    }

    // Waiting for the first coroutine to complete its execution.
    job1.join()
    println("job1 has completed")

    // Awaiting the result of the second coroutine (the computation of the Fibonacci number)
    // and printing it. The `await` method suspends until the result is ready.
    println("job2 fib(42) = ${job2.await()}")
}
