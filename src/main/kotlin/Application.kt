package com.Biodex

import application.services.SpecimenService
import com.Biodex.application.services.CollectionService
import com.Biodex.application.services.ExhibitionContentService
import com.Biodex.application.services.ExhibitionService
import com.Biodex.application.services.LocationService
import com.Biodex.application.services.SpecimenImageService
import com.Biodex.application.services.TaxonomyService
import com.Biodex.domain.models.renewExhibitionContent
import com.Biodex.domain.repositorys.ExhibitionContentRepository
import com.Biodex.infrastructure.persistence.ExposedCollectionRespository
import com.Biodex.infrastructure.persistence.ExposedExhibitionContentRepository
import com.Biodex.infrastructure.persistence.ExposedExhibitionrepository
import com.Biodex.infrastructure.persistence.ExposedLocationRepository
import com.Biodex.infrastructure.persistence.ExposedSpecimenImageRepository
import com.Biodex.infrastructure.persistence.ExposedTaxonomyRepository
import com.Biodex.interfaces.routes.collectionRoutes
import com.Biodex.interfaces.routes.exhibitionContentRoutes
import com.Biodex.interfaces.routes.exhibitionRoutes
import com.Biodex.interfaces.routes.locationRoutes
import com.Biodex.interfaces.routes.specimenImageRoutes
import com.Biodex.interfaces.routes.taxonomyRoutes
import com.Biodex.interfaces.routes.uploadRoutes
import infrastructure.config.DatabaseFactory
import infrastructure.persistence.ExposedSpecimenRepository
import com.Biodex.interfaces.routes.specimenRoutes
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.staticFiles
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.*
import java.io.File


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {


    install(ContentNegotiation) {
        json()
    }

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHost("localhost:4200") // Permitir solicitudes desde el frontend de Angular
        anyHost() // Puedes usar esto para permitir cualquier host durante el desarrollo, pero es menos seguro en producción
    }


    DatabaseFactory.init(environment.config)


    val specimenRepository = ExposedSpecimenRepository()
    val specimenService = SpecimenService(specimenRepository)
    val specimenImageRepository = ExposedSpecimenImageRepository()
    val specimenImageService = SpecimenImageService(specimenImageRepository)
    val taxonomyRepository = ExposedTaxonomyRepository()
    val taxonomyService = TaxonomyService(taxonomyRepository)
    val locationRepository = ExposedLocationRepository()
    val locationService = LocationService(locationRepository)
    val collectionRepository = ExposedCollectionRespository()
    val collectionService = CollectionService(collectionRepository, specimenRepository)
    val exhibitionRepository = ExposedExhibitionrepository()
    val exhibitionContentRepository = ExposedExhibitionContentRepository()
    val exhibitionService = ExhibitionService(exhibitionRepository, exhibitionContentRepository)
    val exhibitionContentService = ExhibitionContentService(exhibitionContentRepository)



    routing {
        specimenRoutes(specimenService)
        specimenImageRoutes(specimenImageService)
        taxonomyRoutes(taxonomyService)
        locationRoutes(locationService)
        collectionRoutes(collectionService)
        exhibitionRoutes(exhibitionService)
        exhibitionContentRoutes(exhibitionContentService)
        uploadRoutes()

        staticFiles(remotePath = "/uploads", dir = File("uploads"))
        }
}