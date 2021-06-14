package com.bytelegend.app.shared.entities

import com.bytelegend.app.shared.annotations.DynamoDbIgnore
import com.bytelegend.app.shared.annotations.JsonIgnore
import com.bytelegend.app.shared.annotations.ReadOnly

val ANONYMOUS_DUMMY_MAP = "ANONYMOUS_DUMMY_MAP"

open class Player : BasePlayer() {
    @get: ReadOnly
    var star: Int = 0

    @get: ReadOnly
    var coin: Int = 0

    @get: ReadOnly
    var reputation: Int = 0

    /**
     * Note: items and states are updated asynchronously.
     * This means that `save()` should avoid saving these two fields.
     */
    @get: ReadOnly
    // it's stored as dynamodb set
    var items: MutableList<String> = ArrayList()

    /**
     * This is actually a set, but enhanced ddb client can't map to java.util.LinkedHashSet by default.
     * Caller should take care of deduplication.
     */
    @get: ReadOnly
    var states: MutableMap<String, String> = HashMap()

    val online: Boolean
        @JsonIgnore
        @DynamoDbIgnore
        get() = server != 0

    /**
     * The preferred locale.
     */
    var locale: String? = null

    var avatarUrl: String? = null

    /**
     * The main email.
     */
    val email: String?
        @JsonIgnore
        @DynamoDbIgnore
        get() = emails.firstOrNull()

    /**
     * All possible emails.
     */
    @JsonIgnore
    var emails: MutableList<String> = ArrayList()

    fun toPartialEntity() = BasePlayer().apply {
        this.id = this@Player.id
        this.username = this@Player.username
        this.nickname = this@Player.nickname
        this.map = this@Player.map
        this.x = this@Player.x
        this.y = this@Player.y
        this.characterId = this@Player.characterId
        this.server = this@Player.server
    }
}

fun ghLoginToPlayerId(ghLogin: String) = "gh#$ghLogin"
