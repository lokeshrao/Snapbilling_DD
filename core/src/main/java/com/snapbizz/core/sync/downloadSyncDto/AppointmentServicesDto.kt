package com.snapbizz.core.sync.downloadSyncDto

import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.entities.AppointmentServices
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.utils.DownSyncConfig
import java.util.Date

data class AppointmentServicesDto(
    @SerializedName("service_id") val serviceId: Long,
    @SerializedName("appointment_id") val appointmentId: Long?,
    @SerializedName("service_name") val serviceName: String?,
    @SerializedName("service_product_code") val serviceProductCode: Long?,
    @SerializedName("created_at") val createdAt: Date?,
    @SerializedName("updated_at") val updatedAt: Date?
)

fun appointmentServicesDtoToEntity(dto: AppointmentServicesDto): AppointmentServices {
    return AppointmentServices(
        id = dto.serviceId,
        appointmentId = dto.appointmentId ?: 0,
        serviceName = dto.serviceName ?: "",
        serviceProductCode = dto.serviceProductCode ?: 0L,
        createdAt = dto.createdAt ?: Date(),
        updatedAt = dto.updatedAt ?: Date()
    )
}

fun getAppointmentServicesSyncConfig(snapDb: SnapDatabase): DownSyncConfig<AppointmentServices, AppointmentServicesDto> {
    return DownSyncConfig(
        tableName = "appointment_services",
        jsonKey = "appointment_services_list",
        entityClass = AppointmentServices::class.java,
        dtoClass = AppointmentServicesDto::class.java,
        daoProvider = { snapDb.appointmentServicesDao() },
        dtoToEntityMapper = ::appointmentServicesDtoToEntity
    )
}
