package org.lmarek.item.adapter.secondary

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.lmarek.item.persistence.ItemEntity
import org.lmarek.item.persistence.ItemRepository
import org.lmarek.item.persistence.NewItemEntity
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import kotlin.test.Test


class SimpleItemStorageServiceTest {
    private val repository: ItemRepository = mockk()
    private val tested = SimpleItemStorageService(repository)

    @Test
    fun `Saves item when repository saves it`(): Unit = runBlocking {
        // given
        val generatedId = 2137L
        coEvery { repository.insert(NewItemEntity("a name", "a description")) } returns ItemEntity(
            generatedId, "a name", "a description"
        )

        // when
        val result = tested.save(NewItem("a name", "a description"))

        // then
        expectThat(result) {
            get { id.value }.isEqualTo(generatedId)
            get { name }.isEqualTo("a name")
            get { description }.isEqualTo("a description")
        }
    }

    @Test
    fun `Retrieves an item when repository contains it`(): Unit = runBlocking {
        // given
        val itemId = 1234L
        coEvery { repository.getById(itemId) } returns ItemEntity(itemId, "a name", "a description")

        // when
        val result = tested.getById(ItemId(itemId))

        // then
        expectThat(result).isNotNull().and {
            get { id.value }.isEqualTo(itemId)
            get { name }.isEqualTo("a name")
            get { description }.isEqualTo("a description")
        }
    }

    @Test
    fun `Returns null item when item is missing`(): Unit = runBlocking {
        // given
        val itemId = 1234L
        coEvery { repository.getById(itemId) } returns null

        // when
        val result = tested.getById(ItemId(itemId))

        // then
        expectThat(result).isNull()
    }
}