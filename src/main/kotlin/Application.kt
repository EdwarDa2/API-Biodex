package com.Biodex

import application.services.SpecimenService
import com.Biodex.application.services.SpecimenImageService
import com.Biodex.infrastructure.persistence.ExposedSpecimenImageRepository
import com.Biodex.interfaces.routes.specimenImageRoutes
import infrastructure.config.DatabaseFactory
import infrastructure.persistence.ExposedSpecimenRepository
import interfaces.routes.specimenRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {


    install(ContentNegotiation) {
        json()
    }


    DatabaseFactory.init(environment.config)


    val specimenRepository = ExposedSpecimenRepository()
    val specimenService = SpecimenService(specimenRepository)
    val specimenImageRepository = ExposedSpecimenImageRepository()
    val specimenImageService = SpecimenImageService(specimenImageRepository)


    routing {
        specimenRoutes(specimenService)
        specimenImageRoutes(specimenImageService)
    }
}