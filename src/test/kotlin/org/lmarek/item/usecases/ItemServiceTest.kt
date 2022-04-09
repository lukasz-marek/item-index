package org.lmarek.item.usecases

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.lmarek.item.adapter.secondary.ItemStorageService
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import kotlin.test.Test


class ItemServiceTest {
    private val itemStorage: ItemStorageService = mockk()
    private val tested = ItemService(itemStorage)

    @Test
    fun `Returns existing item`(): Unit = runBlocking {
        // given
        val existingItemId = ItemId(9871)
        coEvery { itemStorage.getById(existingItemId) } returns Item(existingItemId, "a name", "a description")

        // when
        val existingItem = tested.getItemById(existingItemId)

        // then
        expectThat(existingItem).isNotNull().and {
            get { id }.isEqualTo(existingItemId)
            get { description }.isEqualTo("a description")
            get { name }.isEqualTo("a name")
        }
    }

    @Test
    fun `Returns null when item is missing`(): Unit = runBlocking {
        // given
        val missingItemId = ItemId(9871)
        coEvery { itemStorage.getById(missingItemId) } returns null

        // when
        val missingItem = tested.getItemById(missingItemId)

        // then
        expectThat(missingItem).isNull()
    }

    @Test
    fun `Saves a new item under a new id`(): Unit = runBlocking {
        // given
        val newId = ItemId(1234567)
        val command = NewItemCommand("new name", "new description")
        coEvery { itemStorage.save(NewItem("new name", "new description")) } returns Item(
            newId,
            "new name",
            "new description"
        )

        // when
        val savedItem = tested.storeNewItem(command)

        // then
        expectThat(savedItem) {
            get { id }.isEqualTo(newId)
            get { name }.isEqualTo("new name")
            get { description }.isEqualTo("new description")
        }
    }
}