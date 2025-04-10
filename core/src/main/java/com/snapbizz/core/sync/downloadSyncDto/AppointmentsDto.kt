package com.snapbizz.core.sync.downloadSyncDto

import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.entities.AppointmentServices
import com.snapbizz.core.database.entities.Appointments
import com.snapbizz.core.database.entities.AppointmentsWithServices
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
    @SerializedName("updated_at") val updatedAt: Date?,
    @SerializedName("appointment_service") val services: List<AppointmentServicesDto>? = null
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
        isSyncPending = dto.isSync,
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

fun appointmentsToEntity(appointment: Appointments): AppointmentsDto {
    return AppointmentsDto(
        appointment.appointmentId,
        appointment.customerNumber,
        appointment.representativeId,
        appointment.startDate,
        appointment.endDate,
        appointment.description,
        appointment.isDeleted,
        appointment.status,
        appointment.cancellationReason,
        appointment.isSyncPending,
        appointment.createdAt,
        appointment.updatedAt
    )
}

fun appointmentDtoToEntity(apiAppointment: AppointmentsDto): Pair<Appointments, List<AppointmentServices>> {
    val appointment = Appointments(
        appointmentId = apiAppointment.appointmentId,
        customerNumber = apiAppointment.customerNumber,
        representativeId = apiAppointment.representativeId,
        startDate = apiAppointment.startDate ?: Date(),
        endDate = apiAppointment.endDate ?: Date(),
        description = apiAppointment.description ?: "",
        isDeleted = apiAppointment.isDeleted ?: false,
        status = apiAppointment.status ?: "",
        cancellationReason = apiAppointment.cancellationReason ?: "",
        isSyncPending = false,
        createdAt = apiAppointment.createdAt ?: Date(),
        updatedAt = apiAppointment.updatedAt ?: Date()
    )

    val appointmentServices = apiAppointment.services?.map { serviceDto ->
        AppointmentServices(
            id = null,
            appointmentId = apiAppointment.appointmentId?:0,
            serviceName = serviceDto.serviceName ?: "",
            serviceProductCode = serviceDto.serviceProductCode ?: 0,
            createdAt = apiAppointment.createdAt ?: Date(),
            updatedAt = apiAppointment.updatedAt ?: Date()
        )
    } ?: emptyList()

    return Pair(appointment, appointmentServices)
}


fun convertAppointmentWithServicesToDto(appointmentsWithServices: AppointmentsWithServices): AppointmentsDto {
    val appointment = appointmentsWithServices.appointment
    val services = appointmentsWithServices.services
    return AppointmentsDto(
        appointmentId = appointment.appointmentId,
        customerNumber = appointment.customerNumber,
        representativeId = appointment.representativeId,
        startDate = appointment.startDate,
        endDate = appointment.endDate,
        description = appointment.description,
        isDeleted = appointment.isDeleted,
        status = appointment.status,
        cancellationReason = appointment.cancellationReason,
        isSync = appointment.isSyncPending,
        createdAt = appointment.createdAt,
        updatedAt = appointment.updatedAt,
        services = services.map {
            AppointmentServicesDto(
                appointmentId = it.appointmentId,
                serviceId = it.id?:0,
                serviceName = it.serviceName,
                serviceProductCode = it.serviceProductCode,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    )
}

