package org.lmarek.item.persistence

data class NewItemEntity(val name: String, val description: String)

data class ItemEntity(val id: Long, val name: String, val description: String)
