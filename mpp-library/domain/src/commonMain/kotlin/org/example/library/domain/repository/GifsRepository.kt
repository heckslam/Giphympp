/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package org.example.library.domain.repository

import dev.icerock.moko.network.generated.apis.GifsApi
import org.example.library.domain.entity.Gif
import org.example.library.domain.entity.toDomain
import org.example.library.domain.storage.KeyValueStorage

class GifsRepository internal constructor(
    private val newsApi: GifsApi
) {
    suspend fun getGifsList(query: String): List<Gif> {
        return newsApi.searchGifs(
            q = query,
            limit = null,
            offset = null,
            rating = null,
            lang = null
        ).data?.map { it.toDomain() }.orEmpty()
    }
}
