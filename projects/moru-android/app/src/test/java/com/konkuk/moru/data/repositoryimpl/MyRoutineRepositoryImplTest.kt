package com.konkuk.moru.data.repositoryimpl

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.konkuk.moru.data.service.ImageService
import com.konkuk.moru.data.service.MyRoutineService
import java.time.DayOfWeek
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertThrows
import org.junit.Test
import retrofit2.Retrofit

class MyRoutineRepositoryImplTest {

    @Test
    fun `deleteRoutineSafe treats 2xx and 404 as idempotent success`() {
        listOf(200, 204, 404).forEach { status ->
            withRepository { server, repository ->
                server.enqueue(MockResponse().setResponseCode(status))

                val deleted = runBlocking {
                    repository.deleteRoutineSafe(ROUTINE_ID)
                }

                assertTrue("status=$status", deleted)
                assertSingleRequest(server, "DELETE", "/api/routines/$ROUTINE_ID")
            }
        }
    }

    @Test
    fun `deleteRoutineSafe preserves schedules for every non-success response`() {
        listOf(401, 403, 422, 500).forEach { status ->
            withRepository { server, repository ->
                server.enqueue(MockResponse().setResponseCode(status))
                enqueueLegacyFallbackResponses(server)

                val deleted = runBlocking {
                    repository.deleteRoutineSafe(ROUTINE_ID)
                }

                assertFalse("status=$status", deleted)
                assertSingleRequest(server, "DELETE", "/api/routines/$ROUTINE_ID")
            }
        }
    }

    @Test
    fun `deleteRoutineSafe reports an offline transport failure without crashing`() {
        withRepository { server, repository ->
            server.enqueue(
                MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
            )

            val deleted = runBlocking {
                repository.deleteRoutineSafe(ROUTINE_ID)
            }

            assertFalse(deleted)
        }
    }

    @Test
    fun `updateSchedule preserves existing schedules when PATCH fails`() {
        listOf(401, 403, 422, 500).forEach { status ->
            withRepository { server, repository ->
                server.enqueue(
                    MockResponse()
                        .setResponseCode(status)
                        .setHeader("Content-Type", "application/json")
                        .setBody("""{"message":"refresh-token-secret"}""")
                )
                enqueueLegacyFallbackResponses(server)

                val error = assertThrows(IllegalStateException::class.java) {
                    runBlocking {
                        repository.updateSchedule(
                            routineId = ROUTINE_ID,
                            schId = SCHEDULE_ID,
                            time = "07:30:00",
                            days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                            alarm = true
                        )
                    }
                }

                assertEquals("updateSchedule failed: HTTP $status", error.message)
                assertFalse(error.message.orEmpty().contains("refresh-token-secret"))
                assertSingleRequest(
                    server,
                    "PATCH",
                    "/api/routines/$ROUTINE_ID/schedules/$SCHEDULE_ID"
                )
            }
        }
    }

    @Test
    fun `updateSchedule maps a successful PATCH response without follow-up calls`() {
        withRepository { server, repository ->
            server.enqueue(scheduleResponse())

            val schedules = runBlocking {
                repository.updateSchedule(
                    routineId = ROUTINE_ID,
                    schId = SCHEDULE_ID,
                    time = "07:30:00",
                    days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                    alarm = true
                )
            }

            val schedule = schedules.single()
            assertEquals("updated-schedule", schedule.id)
            assertEquals("MON", schedule.dayOfWeek)
            assertEquals("07:30:00", schedule.time)
            assertTrue(schedule.alarmEnabled == true)
            assertEquals("CUSTOM", schedule.repeatType)
            assertEquals(listOf("MON", "WED"), schedule.daysToCreate)
            assertSingleRequest(
                server,
                "PATCH",
                "/api/routines/$ROUTINE_ID/schedules/$SCHEDULE_ID"
            )
        }
    }

    private fun withRepository(
        block: (server: MockWebServer, repository: MyRoutineRepositoryImpl) -> Unit
    ) {
        val server = MockWebServer()
        server.start()
        try {
            val json = Json { ignoreUnknownKeys = true }
            val retrofit = Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(
                    json.asConverterFactory("application/json".toMediaType())
                )
                .build()
            val repository = MyRoutineRepositoryImpl(
                service = retrofit.create(MyRoutineService::class.java),
                imageService = retrofit.create(ImageService::class.java)
            )

            block(server, repository)
        } finally {
            server.shutdown()
        }
    }

    private fun assertSingleRequest(
        server: MockWebServer,
        method: String,
        path: String
    ) {
        val request = server.takeRequest()
        assertEquals(method, request.method)
        assertEquals(path, request.path)
        assertEquals("no destructive fallback request is allowed", 1, server.requestCount)
    }

    /**
     * Keeps a regression test finite if the old delete-and-recreate fallback is reintroduced.
     * Correct code leaves all of these queued responses untouched.
     */
    private fun enqueueLegacyFallbackResponses(server: MockWebServer) {
        server.enqueue(MockResponse().setResponseCode(204))
        server.enqueue(scheduleResponse())
        server.enqueue(scheduleResponse())
    }

    private fun scheduleResponse(): MockResponse = MockResponse()
        .setResponseCode(200)
        .setHeader("Content-Type", "application/json")
        .setBody(
            """
            [
              {
                "id": "updated-schedule",
                "dayOfWeek": "MON",
                "time": "07:30:00",
                "alarmEnabled": true,
                "repeatType": "CUSTOM",
                "daysToCreate": ["MON", "WED"]
              }
            ]
            """.trimIndent()
        )

    private companion object {
        const val ROUTINE_ID = "routine-42"
        const val SCHEDULE_ID = "schedule-7"
    }
}
