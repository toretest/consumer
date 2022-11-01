package no.dragerot.consumer

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.scheduler.Schedulers
import java.net.URI
import java.time.LocalDateTime
import java.util.*

@SpringBootApplication
class ConsumerApplication : CommandLineRunner {
    override fun run(vararg args: String?) {
        WebClient
            .create("http://localhost:8080")
            .get()
            .uri("/stream")
            .retrieve()
            .bodyToFlux(HelloMessage::class.java)
           // .buffer(30000)
           /* .subscribe {
                    consumer ->
                println("${consumer.timestamp} ${consumer.id} price ${consumer.value}" )

            }*/
           //.parallel(100)
           // .runOn(Schedulers.parallel())
            .subscribe(
                { message ->
                    println("${Thread.currentThread().name} Price ${message}")
                    Thread.currentThread().name
                },
                { error -> println("Feiler : ${error.message}") }
            )

    }
}

/**
 * Message in the stream
 */
data class HelloMessage(
    val id: String = UUID.randomUUID().toString(),
    val value: Int = (1..10).random(),
    val name: String = "Pris",
    val timestamp: LocalDateTime = LocalDateTime.now()
)

fun main(args: Array<String>) {
    runApplication<ConsumerApplication>(*args)
    /* SpringApplicationBuilder(ConsumerApplication::class.java)
        .web(WebApplicationType.NONE)
        .run(*args)*/


}



