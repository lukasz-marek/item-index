package org.lmarek.item.adapter.primary

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import kotlin.test.Test

class ItemRoutingKtTest {

    @Test
    fun `Stores a new item`() = testApplication {
        // given
        val newItem = NewItemDto("a cool name", "a valid description")

        // when
        val response = client.post("/items") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(newItem))
        }

        // then
        expectThat(response) {
            get { contentType() }.isEqualTo(ContentType.Application.Json)
            get { status }.isEqualTo(HttpStatusCode.Created)
        }
        expectThat(response.body<ItemDto>()) {
            get { id }.isNotNull()
            get { name }.isEqualTo(newItem.name)
            get { description }.isEqualTo(newItem.description)
        }
    }
}