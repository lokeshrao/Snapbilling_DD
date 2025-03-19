package com.snapbizz.onboarding.downSync.downloadSyncDto

import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.entities.Doctors
import com.snapbizz.core.utils.DownSyncConfig
import java.util.Date

data class DoctorsDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: Long,
    @SerializedName("specialist_in") val specialistIn: String,
    @SerializedName("is_deleted") val isDeleted: Boolean,
    @SerializedName("is_sync") val isSync: Boolean,
    @SerializedName("created_at") val createdAt: Date?,
    @SerializedName("updated_at") val updatedAt: Date?
)

fun doctorsDtoToEntity(dto: DoctorsDto): Doctors {
    return Doctors(
        id = dto.id,
        name = dto.name,
        phone = dto.phone,
        specialistIn = dto.specialistIn,
        isDeleted = dto.isDeleted,
        isSync = dto.isSync,
        createdAt = dto.createdAt ?: Date(),
        updatedAt = dto.updatedAt ?: Date()
    )
}

fun getDoctorsSyncConfig(snapDb: SnapDatabase): DownSyncConfig<Doctors, DoctorsDto> {
    return DownSyncConfig(
        tableName = "doctors",
        jsonKey = "doctorsList",
        entityClass = Doctors::class.java,
        dtoClass = DoctorsDto::class.java,
        daoProvider = { snapDb.doctorsDao() },
        dtoToEntityMapper = ::doctorsDtoToEntity
    )
}

fun convertToDoctorsAPIObjectList(doctorsList: List<Doctors>?): List<DoctorsDto> {
    return doctorsList?.map { doctor ->
        DoctorsDto(
            doctor.id,
            doctor.name,
            doctor.phone,
            doctor.specialistIn,
            doctor.isDeleted,
            doctor.isSync,
            doctor.createdAt,
            doctor.updatedAt
        )
    } ?: emptyList()
}
