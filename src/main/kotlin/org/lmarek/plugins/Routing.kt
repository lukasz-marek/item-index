package org.lmarek.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.lmarek.item.adapter.primary.configureItemRouting


fun Application.configureRouting() {
    routing {
        configureItemRouting()
    }
}
