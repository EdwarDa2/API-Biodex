package com.Biodex.interfaces

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/hola"){
            call.respondText("Esto es el inicio de mi API con Ktor")
        }
    }
}


