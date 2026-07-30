package com.example.productcatalog.data.mapper

import com.example.productcatalog.data.db.ProductEntity
import com.example.productcatalog.data.model.ProductDto
import com.example.productcatalog.data.model.RatingDto

fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image,
        rate = rating.rate,
        count = rating.count
    )
}

fun ProductEntity.toDto(): ProductDto {
    return ProductDto(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image,
        rating = RatingDto(
            rate = rate,
            count = count
        )
    )
}
