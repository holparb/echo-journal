package com.holparb.echojournal.echoes.domain.data_source

import kotlinx.coroutines.flow.Flow

interface EchoDataSource {
    fun observeEchoes(): Flow<List<Echo>>
    fun observeTopics(): Flow<List<String>>
    fun searchTopics(query: String): Flow<List<String>>
    suspend fun insertEcho(echo: Echo)
}