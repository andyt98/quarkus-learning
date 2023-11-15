package beyond_callbacks.coroutines

import com.andy.p5_beyond_callbacks.shared.HeatSensor
import com.andy.p5_beyond_callbacks.shared.SnapshotService
import io.vertx.core.Vertx
import io.vertx.kotlin.core.deploymentOptionsOf
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

fun main() {
    val vertx = Vertx.vertx()

    vertx.deployVerticle(
        HeatSensor::class.java.name,
        deploymentOptionsOf(config = json {
            obj("http.port" to 3000)
        })
    )

    vertx.deployVerticle(
        HeatSensor::class.java.name,
        deploymentOptionsOf(config = json {
            obj("http.port" to 3001)
        })
    )

    vertx.deployVerticle(
        HeatSensor::class.java.name,
        deploymentOptionsOf(config = json {
            obj("http.port" to 3002)
        })
    )

    vertx.deployVerticle(SnapshotService::class.java.name)
    vertx.deployVerticle(CollectorService::class.qualifiedName)
}
