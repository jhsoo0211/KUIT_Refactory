package com.konkuk.moru.data.token

/**
 * Guards an asynchronous refresh result from overwriting a different login session.
 *
 * A refresh may update tokens only when the persisted generation and both tokens still match the
 * snapshot taken before the network call. DataStore evaluates this predicate inside its serialized
 * edit transaction, making logout and a new login win safely over a stale response.
 */
internal object SessionTokenUpdatePolicy {
    fun matches(
        currentGeneration: Long,
        currentAccessToken: String?,
        currentRefreshToken: String?,
        expectedGeneration: Long,
        expectedAccessToken: String?,
        expectedRefreshToken: String
    ): Boolean =
        currentGeneration == expectedGeneration &&
            currentAccessToken == expectedAccessToken &&
            currentRefreshToken == expectedRefreshToken
}
