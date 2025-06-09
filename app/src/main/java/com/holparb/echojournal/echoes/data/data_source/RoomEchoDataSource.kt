package com.holparb.echojournal.echoes.data.data_source

import com.holparb.echojournal.core.database.echo.EchoDao
import com.holparb.echojournal.echoes.domain.data_source.Echo
import com.holparb.echojournal.echoes.domain.data_source.EchoDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomEchoDataSource(
    private val echoDao: EchoDao
): EchoDataSource {
    override fun observeEchoes(): Flow<List<Echo>> {
        return echoDao
            .observeEchoes()
            .map { echoesWithTopics ->
                echoesWithTopics.map { echoWithTopics ->
                    echoWithTopics.toEcho()
                }
            }
    }

    override fun observeTopics(): Flow<List<String>> {
        return echoDao
            .observeTopics()
            .map { topicEntities ->
                topicEntities.map { it.topic }
            }
    }

    override fun searchTopics(query: String): Flow<List<String>> {
        return echoDao
            .searchTopics(query)
            .map { topicEntities ->
                topicEntities.map { it.topic }
            }
    }

    override suspend fun insertEcho(echo: Echo) {
        echoDao.insertEchoWithTopics(echo.toEchoWithTopics())
    }
}