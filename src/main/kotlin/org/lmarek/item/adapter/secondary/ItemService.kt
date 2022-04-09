package org.lmarek.item.adapter.secondary

interface ItemService {
    suspend fun getById(itemId: ItemId): Item?
    suspend fun save(item: NewItem): Item
}