package com.konkuk.moru.presentation.home.screen

/**
 * Keeps the selection snapshot aligned with the asynchronously delivered routine steps.
 * A corrupt or differently sized persisted snapshot is discarded as a whole so that an
 * index from the current step list can never address stale selection state.
 */
internal object RoutineStepSelectionReducer {

    /** Uses the immutable routine ID so routines with the same title never share state. */
    fun storageKey(routineId: String): String = "routine_step_selection_id_$routineId"

    /** Reads the title-based key only once to migrate state written by older app versions. */
    fun legacyStorageKey(routineTitle: String): String = "saved_selected_states_$routineTitle"

    fun restore(stepCount: Int, serializedSelection: String?): List<Boolean> =
        reconcile(stepCount, parse(serializedSelection))

    fun reconcile(stepCount: Int, selection: List<Boolean>?): List<Boolean> {
        val safeStepCount = stepCount.coerceAtLeast(0)
        return selection
            ?.takeIf { it.size == safeStepCount }
            ?: List(safeStepCount) { false }
    }

    fun toggle(selection: List<Boolean>, index: Int): List<Boolean> {
        if (index !in selection.indices) return selection

        return selection.mapIndexed { currentIndex, isSelected ->
            if (currentIndex == index) !isSelected else isSelected
        }
    }

    fun serialize(selection: List<Boolean>): String =
        selection.joinToString(separator = ",", prefix = "[", postfix = "]")

    private fun parse(serializedSelection: String?): List<Boolean>? {
        val value = serializedSelection?.trim() ?: return null
        if (value.length < 2 || value.first() != '[' || value.last() != ']') return null

        val body = value.substring(1, value.lastIndex).trim()
        if (body.isEmpty()) return emptyList()

        return body.split(',').map { token ->
            when (token.trim()) {
                "true" -> true
                "false" -> false
                else -> return null
            }
        }
    }
}
