package com.konkuk.moru.presentation.home.screen

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class RoutineStepSelectionReducerTest {

    @Test
    fun `storage keys isolate routines that have the same title`() {
        val firstRoutineKey = RoutineStepSelectionReducer.storageKey("routine-1")
        val secondRoutineKey = RoutineStepSelectionReducer.storageKey("routine-2")

        assertNotEquals(firstRoutineKey, secondRoutineKey)
        assertEquals(
            "saved_selected_states_Morning routine",
            RoutineStepSelectionReducer.legacyStorageKey("Morning routine")
        )
    }

    @Test
    fun `restore accepts only a valid snapshot matching the current step count`() {
        data class Case(
            val name: String,
            val stepCount: Int,
            val saved: String?,
            val expected: List<Boolean>
        )

        val cases = listOf(
            Case("missing snapshot", 2, null, listOf(false, false)),
            Case("matching snapshot", 2, "[true,false]", listOf(true, false)),
            Case("whitespace is accepted", 2, "[ true, false ]", listOf(true, false)),
            Case("short snapshot", 3, "[true,false]", listOf(false, false, false)),
            Case("long snapshot", 1, "[true,false]", listOf(false)),
            Case("corrupt snapshot", 2, "[true,invalid]", listOf(false, false)),
            Case("negative count", -1, "[]", emptyList())
        )

        cases.forEach { case ->
            assertEquals(
                case.name,
                case.expected,
                RoutineStepSelectionReducer.restore(case.stepCount, case.saved)
            )
        }
    }

    @Test
    fun `empty initial steps reconcile safely when asynchronous data arrives`() {
        val savedSelection = "[true,false,true]"
        val beforeLoad = RoutineStepSelectionReducer.restore(
            stepCount = 0,
            serializedSelection = savedSelection
        )
        val whileRestoring = RoutineStepSelectionReducer.reconcile(
            stepCount = 3,
            selection = beforeLoad
        )
        val afterRestore = RoutineStepSelectionReducer.restore(
            stepCount = 3,
            serializedSelection = savedSelection
        )

        assertEquals(emptyList<Boolean>(), beforeLoad)
        assertEquals(listOf(false, false, false), whileRestoring)
        assertEquals(listOf(true, false, true), afterRestore)
    }

    @Test
    fun `toggle changes only an in-range item`() {
        data class Case(
            val name: String,
            val selection: List<Boolean>,
            val index: Int,
            val expected: List<Boolean>
        )

        val cases = listOf(
            Case("first item", listOf(false, true), 0, listOf(true, true)),
            Case("last item", listOf(false, true), 1, listOf(false, false)),
            Case("negative index", listOf(false, true), -1, listOf(false, true)),
            Case("index at size", listOf(false, true), 2, listOf(false, true)),
            Case("empty state", emptyList(), 0, emptyList())
        )

        cases.forEach { case ->
            assertEquals(
                case.name,
                case.expected,
                RoutineStepSelectionReducer.toggle(case.selection, case.index)
            )
        }
    }

    @Test
    fun `serialized state round trips through restore`() {
        val selection = listOf(true, false, true)

        assertEquals(
            selection,
            RoutineStepSelectionReducer.restore(
                stepCount = selection.size,
                serializedSelection = RoutineStepSelectionReducer.serialize(selection)
            )
        )
    }
}
