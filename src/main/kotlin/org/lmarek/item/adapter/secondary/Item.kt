package org.lmarek.item.adapter.secondary

@JvmInline
value class ItemId(val value: Long)

data class Item(val id: ItemId, val name: String, val description: String)

data class NewItem(val name: String, val description: String)