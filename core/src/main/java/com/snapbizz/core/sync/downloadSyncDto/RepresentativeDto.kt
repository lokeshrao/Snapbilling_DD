package com.snapbizz.core.sync.downloadSyncDto

import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.entities.Representative
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.utils.DownSyncConfig
import java.util.Date

data class RepresentativeDto(
    @SerializedName("representative_id") val representativeId: Long,
    @SerializedName("name") val name: String?,
    @SerializedName("phone") val phone: Long?,
    @SerializedName("is_deleted") val isDeleted: Boolean,
    @SerializedName("is_sync") val isSync: Boolean,
    @SerializedName("created_at") val createdAt: Date?,
    @SerializedName("updated_at") val updatedAt: Date?
)

fun representativeDtoToEntity(dto: RepresentativeDto): Representative {
    return Representative(
        representativeId = dto.representativeId,
        name = dto.name ?: "",
        phone = dto.phone ?: 0,
        isDeleted = dto.isDeleted,
        isSyncPending = dto.isSync,
        createdAt = dto.createdAt ?: Date(),
        updatedAt = dto.updatedAt ?: Date()
    )
}

fun getRepresentativeSyncConfig(snapDb: SnapDatabase): DownSyncConfig<Representative, RepresentativeDto> {
    return DownSyncConfig(
        tableName = "representative",
        jsonKey = "representativeList",
        entityClass = Representative::class.java,
        dtoClass = RepresentativeDto::class.java,
        daoProvider = { snapDb.representativeDao() },
        dtoToEntityMapper = ::representativeDtoToEntity
    )
}
fun representativeToEntity(representative: Representative): RepresentativeDto {
    return RepresentativeDto(
        representative.representativeId?:0L,
        representative.name,
        representative.phone,
        representative.isDeleted,
        representative.isSyncPending,
        representative.createdAt,
        representative.updatedAt
    )
}
