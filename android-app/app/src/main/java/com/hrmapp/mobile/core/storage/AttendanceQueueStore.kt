package com.hrmapp.mobile.core.storage

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class QueuedAttendanceAction(
    val employeeId: Long,
    val assignmentId: Long,
    val eventType: String,
    val eventTimeDevice: String,
    val geoLat: Double,
    val geoLng: Double,
    val deviceId: String,
    val idempotencyKey: String
)

class AttendanceQueueStore(
    context: Context
) {
    private val prefs = context.getSharedPreferences("attendance_queue", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val key = "queued_actions"

    fun getAll(): List<QueuedAttendanceAction> {
        val raw = prefs.getString(key, "[]") ?: "[]"
        val type = object : TypeToken<List<QueuedAttendanceAction>>() {}.type
        return gson.fromJson(raw, type) ?: emptyList()
    }

    fun saveAll(items: List<QueuedAttendanceAction>) {
        prefs.edit().putString(key, gson.toJson(items)).apply()
    }

    fun enqueue(item: QueuedAttendanceAction) {
        val current = getAll().toMutableList()
        current.add(item)
        saveAll(current)
    }

    fun clear() {
        saveAll(emptyList())
    }

    fun size(): Int = getAll().size
}