package com.nammaraste.health

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roads")
data class RoadEntity(
    @PrimaryKey val roadId: String,
    val roadName: String,
    val taluka: String,
    val lengthKm: Double,
    val contractorName: String,
    val currentHealthScore: Int,
    val warrantyDate: String = "N/A"
)

@Entity(tableName = "damage_reports")
data class DamageReportEntity(
    @PrimaryKey val reportId: String,
    val roadId: String,
    val latitude: Double,
    val longitude: Double,
    val damageType: String,
    val severity: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val photoUrl: String = "",
    val syncStatus: String = "pending"
)

@Entity(tableName = "contractors")
data class ContractorEntity(
    @PrimaryKey val contractorId: String,
    val roadId: String,
    val name: String,
    val license: String,
    val warrantyEnd: String
)