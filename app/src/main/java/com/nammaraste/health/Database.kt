package com.nammaraste.health

import android.content.Context
import androidx.room.*

@Dao
interface RoadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoad(road: RoadEntity)

    @Query("SELECT * FROM roads")
    suspend fun getAllRoads(): List<RoadEntity>

    @Query("SELECT * FROM roads WHERE roadName LIKE '%' || :query || '%'")
    suspend fun searchRoads(query: String): List<RoadEntity>

    @Query("UPDATE roads SET currentHealthScore = :score WHERE roadId = :roadId")
    suspend fun updateScore(roadId: String, score: Int)
}

@Dao
interface DamageReportDao {

    @Insert
    suspend fun insertReport(report: DamageReportEntity)

    @Query("SELECT * FROM damage_reports WHERE syncStatus = 'pending'")
    suspend fun getPendingReports(): List<DamageReportEntity>

    @Query("UPDATE damage_reports SET syncStatus = 'synced' WHERE reportId = :id")
    suspend fun markSynced(id: String)
}

@Dao
interface ContractorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContractor(contractor: ContractorEntity)

    @Query("SELECT * FROM contractors WHERE roadId = :roadId")
    suspend fun getContractorForRoad(roadId: String): ContractorEntity?
}

@Database(
    entities = [
        RoadEntity::class,
        DamageReportEntity::class,
        ContractorEntity::class
    ],
    version = 1
)
abstract class NammaRasteDb : RoomDatabase() {

    abstract fun roadDao(): RoadDao
    abstract fun damageReportDao(): DamageReportDao
    abstract fun contractorDao(): ContractorDao

    companion object {

        @Volatile
        private var INSTANCE: NammaRasteDb? = null

        fun getInstance(context: Context): NammaRasteDb {

            return INSTANCE ?: synchronized(this) {

                Room.databaseBuilder(
                    context,
                    NammaRasteDb::class.java,
                    "namma_raste_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}