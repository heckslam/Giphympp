/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package org.example.library.domain.di

import com.github.aakira.napier.Napier
import com.russhwolf.settings.Settings
import dev.icerock.moko.network.exceptionfactory.HttpExceptionFactory
import dev.icerock.moko.network.exceptionfactory.parser.ErrorExceptionParser
import dev.icerock.moko.network.exceptionfactory.parser.ValidationExceptionParser
import dev.icerock.moko.network.features.ExceptionFeature
import dev.icerock.moko.network.features.TokenFeature
import dev.icerock.moko.network.generated.apis.GifsApi
import io.ktor.client.HttpClient
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import org.example.library.domain.repository.ConfigRepository
import org.example.library.domain.repository.GifsRepository
import org.example.library.domain.storage.KeyValueStorage

class DomainFactory(
    private val settings: Settings,
    private val baseUrl: String
) {
    private val keyValueStorage: KeyValueStorage by lazy { KeyValueStorage(settings) }

    private val json: Json by lazy {
        @Suppress("EXPERIMENTAL_API_USAGE")
        Json.nonstrict
    }

    private val httpClient: HttpClient by lazy {
        HttpClient {
            install(ExceptionFeature) {
                exceptionFactory = HttpExceptionFactory(
                    defaultParser = ErrorExceptionParser(json),
                    customParsers = mapOf(
                        HttpStatusCode.UnprocessableEntity.value to ValidationExceptionParser(json)
                    )
                )
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.d(message = message)
                    }
                }
                level = LogLevel.HEADERS
            }
            install(TokenFeature) {
                tokenHeaderName = "api_key"
                tokenProvider = object : TokenFeature.TokenProvider {
                    override fun getToken(): String? = "o5tAxORWRXRxxgIvRthxWnsjEbA3vkjV"
                }
            }

            // disable standard BadResponseStatus - exceptionfactory do it for us
            expectSuccess = false
        }
    }

    private val gifsApi: GifsApi by lazy {
        GifsApi(
            basePath = baseUrl,
            httpClient = httpClient,
            json = json
        )
    }

    val gifsRepository: GifsRepository by lazy {
        GifsRepository(
            newsApi = gifsApi
        )
    }

    val configRepository: ConfigRepository by lazy {
        ConfigRepository(keyValueStorage = keyValueStorage)
    }
}
