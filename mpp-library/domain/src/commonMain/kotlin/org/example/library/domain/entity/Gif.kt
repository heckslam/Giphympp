/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package org.example.library.domain.entity

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize

@Parcelize
data class Gif(
    val id: Int,
    val previewUrl: String,
    val sourceUrl: String
) : Parcelable

internal fun dev.icerock.moko.network.generated.models.Gif.toDomain(): Gif = Gif(
    url.hashCode(),
    requireNotNull(images?.downsizedMedium?.url) { "api can't respond without preview image" },
    requireNotNull(images?.original?.url) { "api can't respond without original image" }
)
