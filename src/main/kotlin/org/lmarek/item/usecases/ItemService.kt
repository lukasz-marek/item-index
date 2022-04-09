package org.lmarek.item.usecases

import org.lmarek.item.adapter.secondary.Item
import org.lmarek.item.adapter.secondary.ItemId
import org.lmarek.item.adapter.secondary.ItemStorageService

class ItemService(private val itemStorageService: ItemStorageService) {
    suspend fun getItemById(itemId: ItemId): Item? =
        itemStorageService.getById(itemId)
}