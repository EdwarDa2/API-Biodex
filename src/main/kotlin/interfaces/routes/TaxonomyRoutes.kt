package com.Biodex.interfaces.routes

import com.Biodex.application.services.TaxonomyService
import com.Biodex.domain.models.NewTaxonomyData
import com.Biodex.interfaces.controllers.toResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlin.text.toIntOrNull


fun Route.taxonomyRoutes(taxonomyService: TaxonomyService) {
    route("/taxonomy") {
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if(id == null){
                call.respond(HttpStatusCode.BadRequest,"Id invalido")
                return@get
            }
            val taxonomy = taxonomyService.getTaxonomy(id) ?: return@get
            if(taxonomy != null){
                call.respond(HttpStatusCode.OK, taxonomy)
            }else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        post {
            try{
                val taxonomyData = call.receive<NewTaxonomyData>()
                val newTaxonomy = taxonomyService.createTaxonomy(taxonomyData)
                call.respond(HttpStatusCode.Created, "Taxonomia  creada: ${newTaxonomy?.id}")

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
                val taxonomyData = call.receive<NewTaxonomyData>()
                val updatedTaxonomy = taxonomyService.updateTaxonomy(id, taxonomyData)

                println("◀️ RESULTADO DEL REPOSITORIO: $updatedTaxonomy")
                if (updatedTaxonomy != null) {
                    call.respond(HttpStatusCode.OK, updatedTaxonomy.toResponse())
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