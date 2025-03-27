package com.snapbizz.core.sync.downloadSyncDto

import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.entities.Appointments
import com.snapbizz.core.utils.DownSyncConfig
import java.util.Date

data class AppointmentsDto(
    @SerializedName("appointment_id") val appointmentId: Long?,
    @SerializedName("customer_number") val customerNumber: Long,
    @SerializedName("representative_id") val representativeId: Long,
    @SerializedName("start_date") val startDate: Date?,
    @SerializedName("end_date") val endDate: Date?,
    @SerializedName("description") val description: String?,
    @SerializedName("is_deleted") val isDeleted: Boolean,
    @SerializedName("status") val status: String?,
    @SerializedName("cancellation_reason") val cancellationReason: String?,
    @SerializedName("is_sync") val isSync: Boolean,
    @SerializedName("created_at") val createdAt: Date?,
    @SerializedName("updated_at") val updatedAt: Date?
)

fun appointmentsDtoToEntity(dto: AppointmentsDto): Appointments {
    return Appointments(
        appointmentId = dto.appointmentId,
        customerNumber = dto.customerNumber,
        representativeId = dto.representativeId,
        startDate = dto.startDate ?: Date(),
        endDate = dto.endDate ?: Date(),
        description = dto.description ?: "",
        isDeleted = dto.isDeleted,
        status = dto.status ?: "",
        cancellationReason = dto.cancellationReason,
        isSync = dto.isSync,
        createdAt = dto.createdAt ?: Date(),
        updatedAt = dto.updatedAt ?: Date()
    )
}

fun getAppointmentsSyncConfig(snapDb: SnapDatabase): DownSyncConfig<Appointments, AppointmentsDto> {
    return DownSyncConfig(
        tableName = "appointments",
        jsonKey = "appointmentsList",
        entityClass = Appointments::class.java,
        dtoClass = AppointmentsDto::class.java,
        daoProvider = { snapDb.appointmentsDao() },
        dtoToEntityMapper = ::appointmentsDtoToEntity
    )
}

fun convertToAppointmentsAPIObjectList(appointmentsList: List<Appointments>?): List<AppointmentsDto> {
    return appointmentsList?.map { appointment ->
        AppointmentsDto(
            appointment.appointmentId,
            appointment.customerNumber,
            appointment.representativeId,
            appointment.startDate,
            appointment.endDate,
            appointment.description,
            appointment.isDeleted,
            appointment.status,
            appointment.cancellationReason,
            appointment.isSync,
            appointment.createdAt,
            appointment.updatedAt
        )
    } ?: emptyList()
}
