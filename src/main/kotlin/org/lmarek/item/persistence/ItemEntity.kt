package org.lmarek.item.persistence

@JvmInline
value class ItemId(val value: Long)

data class NewItemEntity(val name: String, val description: String)

data class ItemEntity(val id: ItemId, val name: String, val description: String)
