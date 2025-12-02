package com.Biodex.interfaces.routes

import com.Biodex.application.services.LocationService
import com.Biodex.domain.models.renewLocation
import com.Biodex.interfaces.controllers.toResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.locationRoutes(locationService: LocationService) {
    route("/locations") {
        get("/search") {
            val country = call.request.queryParameters["country"]
            val state = call.request.queryParameters["state"]
            val municipality = call.request.queryParameters["municipality"]
            val locality = call.request.queryParameters["locality"]
            val latitude_degrees = call.request.queryParameters["latitude_degrees"]?.toIntOrNull()
            val latitude_minutes = call.request.queryParameters["latitude_minutes"]?.toIntOrNull()
            val latitude_seconds = call.request.queryParameters["latitude_seconds"]?.toDoubleOrNull()
            val longitude_degrees = call.request.queryParameters["longitude_degrees"]?.toIntOrNull()
            val longitude_minutes = call.request.queryParameters["longitude_minutes"]?.toIntOrNull()
            val longitude_seconds = call.request.queryParameters["longitude_seconds"]?.toDoubleOrNull()
            val altitude = call.request.queryParameters["altitude"]?.toDoubleOrNull()

            if (country == null || state == null || municipality == null || locality == null ||
                latitude_degrees == null || latitude_minutes == null || latitude_seconds == null ||
                longitude_degrees == null || longitude_minutes == null || longitude_seconds == null ||
                altitude == null) {
                call.respond(HttpStatusCode.BadRequest, "Faltan parámetros de búsqueda para la ubicación.")
                return@get
            }
            val location = locationService.getLocationByAttributes(
                country, state, municipality, locality,
                latitude_degrees, latitude_minutes, latitude_seconds,
                longitude_degrees, longitude_minutes, longitude_seconds,
                altitude
            )
            if (location != null) {
                call.respond(HttpStatusCode.OK, location)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID  inválido.")
                return@get
            }
            val location = locationService.getLocation(id) ?: return@get

            if (location != null) {
                call.respond(HttpStatusCode.OK, location)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        post {
            try {
                val locationData = call.receive<renewLocation>()
                val newLocation = locationService.saveLocation(locationData)
                call.respond(HttpStatusCode.Created, "Locacion  Guarda: ${newLocation?.id}")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Datos invaldos: ${e.localizedMessage}")
            }
        }
        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            println("▶️ INTENTANDO ACTUALIZAR. ID RECIBIDO: $id")
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Id invalido")
                return@put
            }
            try {
                val locationData = call.receive<renewLocation>()
                val updateLocation = locationService.updateLocation(id, locationData)

                println("◀️ RESULTADO DEL REPOSITORIO: $updateLocation")
                if (updateLocation != null) {
                    call.respond(HttpStatusCode.OK, updateLocation.toResponse())
                } else {
                    println("❌ NO SE ENCONTRÓ EL ID $id. ENVIANDO 404.")
                    call.respond(HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Datos invaldos: ${e.localizedMessage}")
            }
        }
    }
}