package interfaces.routes

import application.services.SpecimenService
import domain.repositorys.NewSpecimenData
import domain.repositorys.UpdateSpecimenData
import interfaces.controllers.toResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.httpDateFormat
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.specimenRoutes(specimenService: SpecimenService) {
    route("/specimens") {
        get("{id}") {

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de espécimen inválido.")
                return@get
            }


            val specimen = specimenService.findSpecimenById(id) ?: return@get



            if (specimen != null) {
                call.respond(HttpStatusCode.OK, specimen)
            } else {

                call.respond(HttpStatusCode.NotFound)
            }
        }
        post {
            try {
                val specimenData = call.receive<NewSpecimenData>()
                val newSpecimen = specimenService.createNewSpecimen(specimenData)
                call.respond(HttpStatusCode.Created, "New specimen creada: ${newSpecimen.id}")
            }catch (e:Exception){
                call.respond(HttpStatusCode.InternalServerError, "Datos invaldos: ${e.localizedMessage}")
            }
        }
        put("{id}"){
            val id = call.parameters["id"]?.toIntOrNull()

            println("▶️ INTENTANDO ACTUALIZAR. ID RECIBIDO: $id")
            if(id == null) {
                call.respond(HttpStatusCode.BadRequest,"Id invalido")
                return@put
            }
            try {
                val specimenData = call.receive<UpdateSpecimenData>()
                val updatedSpecimen = specimenService.updateSpecimen(id, specimenData)

                println("◀️ RESULTADO DEL REPOSITORIO: $updatedSpecimen")
                if (updatedSpecimen != null) {
                    call.respond(HttpStatusCode.OK, updatedSpecimen.toResponse())
                } else {
                    println("❌ NO SE ENCONTRÓ EL ID $id. ENVIANDO 404.")
                    call.respond(HttpStatusCode.NotFound)
                }
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest, "Datos invaldos: ${e.localizedMessage}")
            }
        }
        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if(id == null) {
                call.respond(HttpStatusCode.BadRequest, "Id invalido")
                return@delete
            }
            val success = specimenService.deleteSpecimen(id)
            if(success) {
                call.respond(HttpStatusCode.NoContent,"Specimen eliminado")
            }else{
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}