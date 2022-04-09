package org.lmarek.item.usecases

@JvmInline
value class ItemId(val value: Long)

data class Item(val id: ItemId, val name: String, val description: String)

data class NewItem(val name: String, val description: String) {
    init {
        ensureConstraint(name.isNotBlank()) { "name cannot be empty nor blank" }
        ensureConstraint(description.isNotBlank()) { "description cannot be empty nor blank" }
    }
}

private inline fun ensureConstraint(constraint: Boolean, messageSupplier: () -> String) {
    if (!constraint)
        throw ItemException(messageSupplier())
}

class ItemException(message: String) : RuntimeException(message)