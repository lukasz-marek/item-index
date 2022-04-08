package org.lmarek.item.persistence

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.postgresql.ds.PGSimpleDataSource
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import java.util.concurrent.Executors
import javax.sql.DataSource
import kotlin.test.BeforeTest

internal class ItemRepositoryTest {
    private val postgres =
        PostgreSQLContainer(DockerImageName.parse("postgres:alpine3.15")).withInitScript("sql/init_db.sql")
    private lateinit var tested: ItemRepository

    @BeforeTest
    fun beforeEach() {
        postgres.start()
        val dataSource = getDataSource(postgres)
        val pool = Executors.newSingleThreadExecutor()
        tested = ItemRepository(dataSource, pool)
    }

    @Test
    fun `Inserts an item into db`(): Unit = runBlocking {
        // given
        val newItem = NewItemEntity("item name", "item description")

        // when
        val inserted = tested.insert(newItem)

        // then
        expectThat(inserted) {
            get { name }.isEqualTo(newItem.name)
            get { description }.isEqualTo(newItem.description)
        }
    }

    @Test
    fun `Subsequent insertions provide different ids`(): Unit = runBlocking {
        // given
        val newItem = NewItemEntity("item name", "item description")

        // when
        val inserted1 = tested.insert(newItem)
        val inserted2 = tested.insert(newItem)

        // then
        expectThat(inserted1.id).isNotEqualTo(inserted2.id)
    }

    @Test
    fun `Fetches existing item`(): Unit = runBlocking {
        // given
        val newItem = NewItemEntity("item name", "item description")

        // when
        val inserted = tested.insert(newItem)
        val existingItem = tested.getById(inserted.id)

        // then
        expectThat(existingItem).isNotNull().and {
            get { id }.isEqualTo(inserted.id)
            get { name }.isEqualTo(newItem.name)
            get { description }.isEqualTo(newItem.description)
        }
    }

    @Test
    fun `Returns null for non-existent item`(): Unit = runBlocking {

        // when
        val missingItem = tested.getById(ItemId(1234))

        // then
        expectThat(missingItem).isNull()
    }

    private fun getDataSource(container: JdbcDatabaseContainer<*>): DataSource {
        val dataSource = PGSimpleDataSource()
        dataSource.setURL(container.jdbcUrl)
        dataSource.user = container.username
        dataSource.password = container.password
        dataSource.databaseName = container.databaseName
        return dataSource
    }
}