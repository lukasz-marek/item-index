package org.lmarek.item.adapter.secondary

import org.lmarek.item.persistence.ItemEntity
import org.lmarek.item.persistence.ItemRepository
import org.lmarek.item.persistence.NewItemEntity

class SimpleItemStorageService(private val itemRepository: ItemRepository) : ItemStorageService {
    override suspend fun getById(itemId: ItemId): Item? {
        val retrievedItem = itemRepository.getById(itemId.value)
        return retrievedItem?.toItem()
    }

    override suspend fun save(item: NewItem): Item {
        val saved = itemRepository.insert(item.toEntity())
        return saved.toItem()
    }

    private fun NewItem.toEntity(): NewItemEntity = NewItemEntity(name, description)

    private fun ItemEntity.toItem(): Item = Item(ItemId(id), name, description)
}