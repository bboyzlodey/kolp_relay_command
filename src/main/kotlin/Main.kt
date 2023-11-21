import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

val releCommandFlow = MutableStateFlow(0)
suspend fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
    withContext(Dispatchers.IO) {
        launch {
            startServer()
        }
        launch {
            return@launch
            while (true) {
                val nextCommand = Scanner(System.`in`).nextInt()
                releCommandFlow.value = nextCommand
                delay(200)
            }
        }
    }
}

private suspend fun startServer() {
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/rele_commands") {
                println("got request")
                call.respondText(
                    releCommandFlow.value.toString(),
                    contentType = ContentType.Text.Plain,
                    status = HttpStatusCode.OK
                )
            }
        }
    }.start(true)
}