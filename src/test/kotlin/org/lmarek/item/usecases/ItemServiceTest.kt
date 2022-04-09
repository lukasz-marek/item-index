package org.lmarek.item.usecases

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.lmarek.item.adapter.secondary.Item
import org.lmarek.item.adapter.secondary.ItemId
import org.lmarek.item.adapter.secondary.ItemStorageService
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
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
}