package org.lmarek.item.adapter.secondary

import org.lmarek.item.usecases.Item
import org.lmarek.item.usecases.ItemId
import org.lmarek.item.usecases.NewItem

interface ItemStorageService {
    suspend fun getById(itemId: ItemId): Item?
    suspend fun save(item: NewItem): Item
}