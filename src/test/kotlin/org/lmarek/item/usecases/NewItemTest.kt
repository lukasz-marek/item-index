package org.lmarek.item.usecases

import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import kotlin.test.Test

class NewItemTest {

    @Test
    fun `Requires a non-empty name`() {
        // given
        val name = ""
        val description = "a description"

        // when / then
        expectThrows<ItemException> {
            NewItem(name, description)
        }.get { message }.isEqualTo("name cannot be empty nor blank")
    }

    @Test
    fun `Requires a non-blank name`() {
        // given
        val name = "   "
        val description = "a description"

        // when / then
        expectThrows<ItemException> {
            NewItem(name, description)
        }.get { message }.isEqualTo("name cannot be empty nor blank")
    }

    @Test
    fun `Requires a non-empty description`() {
        // given
        val name = "valid name"
        val description = ""

        // when / then
        expectThrows<ItemException> {
            NewItem(name, description)
        }.get { message }.isEqualTo("description cannot be empty nor blank")
    }

    @Test
    fun `Requires a non-blank description`() {
        // given
        val name = "valid name"
        val description = "   "

        // when / then
        expectThrows<ItemException> {
            NewItem(name, description)
        }.get { message }.isEqualTo("description cannot be empty nor blank")
    }
}