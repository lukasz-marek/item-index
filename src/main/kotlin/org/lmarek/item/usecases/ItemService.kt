package org.lmarek.item.usecases

import org.lmarek.item.adapter.secondary.Item
import org.lmarek.item.adapter.secondary.ItemId
import org.lmarek.item.adapter.secondary.ItemStorageService
import org.lmarek.item.adapter.secondary.NewItem

class ItemService(private val itemStorageService: ItemStorageService) {
    suspend fun getItemById(itemId: ItemId): Item? =
        itemStorageService.getById(itemId)

    suspend fun storeNewItem(newItem: NewItem): Item {
        return itemStorageService.save(newItem)
    }
}