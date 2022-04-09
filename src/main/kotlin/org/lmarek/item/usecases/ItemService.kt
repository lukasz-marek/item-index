package org.lmarek.item.usecases

import org.lmarek.item.adapter.secondary.ItemStorageService


data class NewItemCommand(val name: String, val description: String)

class ItemService(private val itemStorageService: ItemStorageService) {
    suspend fun getItemById(itemId: ItemId): Item? =
        itemStorageService.getById(itemId)

    suspend fun storeNewItem(command: NewItemCommand): Item {
        val newItem = NewItem(command.name, command.description)
        return itemStorageService.save(newItem)
    }
}