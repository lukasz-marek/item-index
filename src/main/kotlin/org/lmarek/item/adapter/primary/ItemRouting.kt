package org.lmarek.item.adapter.primary

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable


fun Route.configureItemRouting() {
    route("/items") {
        post {
            val data = call.receive<NewItemDto>()

        }
    }
}

@Serializable
data class NewItemDto(val name: String, val description: String)

@Serializable
data class ItemDto(val id: Long, val name: String, val description: String)

