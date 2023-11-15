package beyond_callbacks.coroutines

import io.vertx.core.http.HttpServerRequest
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.predicate.ResponsePredicate
import io.vertx.ext.web.codec.BodyCodec
import io.vertx.kotlin.core.json.Json
import io.vertx.kotlin.core.json.array
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory


/**
 * CollectorService extends CoroutineVerticle, demonstrating Kotlin Coroutines with Vert.x.
 * This service collects temperature data from multiple sensor services and sends a consolidated
 * response to a snapshot service. Coroutines are used to handle asynchronous operations
 * in a more straightforward and sequential-looking manner compared to traditional callbacks.
 */
class CollectorService : CoroutineVerticle() {

    private val logger = LoggerFactory.getLogger(CollectorService::class.java)

    private lateinit var webClient: WebClient

    /**
     * Starts the CollectorService verticle. This method sets up the WebClient and the HTTP server.
     * The server listens on port 8080 and handles requests using the handleRequest function.
     * The use of 'suspend' indicates that this function is a coroutine and can be suspended
     * without blocking the thread.
     */
    override suspend fun start() {
        webClient = WebClient.create(vertx)
        vertx.createHttpServer()
            .requestHandler(this::handleRequest)
            .listen(8080).await()
    }

    /**
     * Handles incoming HTTP requests. It launches a coroutine for each request to process them
     * asynchronously. Inside the coroutine, it fetches temperatures from sensor services and
     * sends the aggregated data to the snapshot service.
     *
     * @param request The incoming HTTP server request.
     */
    private fun handleRequest(request: HttpServerRequest) {
        launch {
            try {
                val t1 = async { fetchTemperature(3000) }
                val t2 = async { fetchTemperature(3001) }
                val t3 = async { fetchTemperature(3002) }

                val array = Json.array(t1.await(), t2.await(), t3.await())
                val json = json { obj("data" to array) }

                sendToSnapshot(json)
                request.response()
                    .putHeader("Content-Type", "application/json")
                    .end(json.encode())

            } catch (err: Throwable) {
                logger.error("Something went wrong", err)
                request.response().setStatusCode(500).end()
            }
        }
    }

    /**
     * Fetches temperature data from a sensor service asynchronously. This method demonstrates
     * how to use coroutines to manage asynchronous HTTP requests in a sequential manner.
     *
     * @param port The port number of the sensor service.
     * @return A JsonObject containing the temperature data.
     */
    private suspend fun fetchTemperature(port: Int): JsonObject {
        return webClient
            .get(port, "localhost", "/")
            .expect(ResponsePredicate.SC_SUCCESS)
            .`as`(BodyCodec.jsonObject())
            .send().await()
            .body()
    }

    /**
     * Sends the collected temperature data to the snapshot service. This method is a suspend function,
     * showcasing how coroutines can be used to handle asynchronous operations in a sequential and
     * non-blocking way.
     *
     * @param json The JsonObject containing aggregated temperature data.
     */
    private suspend fun sendToSnapshot(json: JsonObject) {
        webClient
            .post(4000, "localhost", "/")
            .expect(ResponsePredicate.SC_SUCCESS)
            .sendJson(json).await()
    }
}
