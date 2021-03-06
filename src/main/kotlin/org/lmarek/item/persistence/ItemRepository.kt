package org.lmarek.item.persistence

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement.RETURN_GENERATED_KEYS
import java.util.concurrent.ExecutorService
import javax.sql.DataSource

private const val ID_INDEX = 1
private const val NAME_INDEX = 2
private const val DESCRIPTION_INDEX = 3
private const val TABLE_NAME = "items"
private const val ID_COLUMN = "ID"
private const val NAME_COLUMN = "NAME"
private const val DESCRIPTION_COLUMN = "DESCRIPTION"

class ItemRepository(private val dataSource: DataSource, executorService: ExecutorService) {
    private val repositoryDispatcher = executorService.asCoroutineDispatcher()

    suspend fun insert(entity: NewItemEntity): ItemEntity = withContext(repositoryDispatcher) {
        withConnection {
            val insertion = it.prepareInsertionStatement(entity)
            insertion.execute()
            buildEntity(insertion.getGeneratedKey(), entity)
        }
    }

    suspend fun getById(id: Long): ItemEntity? = withContext(repositoryDispatcher) {
        withConnection {
            val findById = it.prepareFindByIdQuery(id)
            val result = findById.executeQuery()
            result.nextEntity()
        }
    }

    private fun buildEntity(id: Long, data: NewItemEntity): ItemEntity = with(data) {
        ItemEntity(id = id, name = name, description = description)
    }

    private inline fun <T> withConnection(block: (Connection) -> T): T = dataSource.connection.use(block)

    private fun Connection.prepareFindByIdQuery(id: Long): PreparedStatement {
        val findById =
            prepareStatement("select $ID_COLUMN, $NAME_COLUMN, $DESCRIPTION_COLUMN from $TABLE_NAME where $ID_COLUMN = ?")
        findById.setLong(1, id)
        return findById
    }

    private fun PreparedStatement.getGeneratedKey(): Long {
        val keys = generatedKeys
        check(keys.next()) { "Insertion result must be present" }
        return keys.getLong(1)
    }

    private fun Connection.prepareInsertionStatement(entity: NewItemEntity): PreparedStatement {
        val insertion = prepareStatement(
            "insert into $TABLE_NAME ($NAME_COLUMN, $DESCRIPTION_COLUMN) values (?, ?)", RETURN_GENERATED_KEYS
        )
        insertion.setString(1, entity.name)
        insertion.setString(2, entity.description)
        return insertion
    }

    private fun ResultSet.nextEntity(): ItemEntity? = if (next()) toEntity() else null

    private fun ResultSet.toEntity(): ItemEntity {
        val id = getLong(ID_INDEX)
        val name = getString(NAME_INDEX)
        val description = getString(DESCRIPTION_INDEX)
        return ItemEntity(id, name, description)
    }
}