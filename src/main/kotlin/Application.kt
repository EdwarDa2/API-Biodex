package com.Biodex

import application.services.SpecimenService
import com.Biodex.application.services.CollectionService
import com.Biodex.application.services.ExhibitionContentService
import com.Biodex.application.services.ExhibitionService
import com.Biodex.application.services.LocationService
import com.Biodex.application.services.SpecimenImageService
import com.Biodex.application.services.TaxonomyService
import com.Biodex.application.services.UserService
import com.Biodex.infrastructure.persistence.*
import com.Biodex.infrastructure.security.configureJWTAuth
import com.Biodex.interfaces.routes.*
import infrastructure.config.DatabaseFactory
import infrastructure.persistence.ExposedSpecimenRepository
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.staticFiles
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import com.Biodex.application.services.RequestService
import com.Biodex.infrastructure.persistence.RequestsTable

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
        allowHost("localhost:4200")
        anyHost()
    }

    DatabaseFactory.init(environment.config)

    transaction {
        SchemaUtils.createMissingTablesAndColumns(Users)
    }

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

    val userRepository = ExposedUserRepository()
    val userService = UserService(userRepository)

    val requestRepository = ExposedRequestRepository()
    val requestService = RequestService(requestRepository, userRepository)

    configureJWTAuth()

    routing {
        specimenRoutes(specimenService)
        specimenImageRoutes(specimenImageService)
        taxonomyRoutes(taxonomyService)
        locationRoutes(locationService)
        collectionRoutes(collectionService)
        exhibitionRoutes(exhibitionService)
        exhibitionContentRoutes(exhibitionContentService)
        uploadRoutes()

        userRoutes(userService)
        requestRoutes(requestService)

        staticFiles(remotePath = "/uploads", dir = File("uploads"))
    }
}