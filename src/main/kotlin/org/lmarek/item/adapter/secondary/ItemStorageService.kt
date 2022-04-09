package org.lmarek.item.adapter.secondary

interface ItemStorageService {
    suspend fun getById(itemId: ItemId): Item?
    suspend fun save(item: NewItem): Item
}