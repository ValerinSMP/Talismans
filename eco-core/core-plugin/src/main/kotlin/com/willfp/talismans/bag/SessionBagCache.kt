package com.willfp.talismans.bag

import java.util.UUID

/** Profiles compare equal by UUID; cache ownership must compare their identity instead. */
internal class SessionBagCache<T> {
    private data class Entry<T>(val profile: Any, val value: T)
    private val entries = mutableMapOf<UUID, Entry<T>>()

    fun get(id: UUID, profile: Any): T? = entries[id]?.takeIf { it.profile === profile }?.value

    fun put(id: UUID, profile: Any, value: T) {
        entries[id] = Entry(profile, value)
    }

    fun remove(id: UUID) {
        entries.remove(id)
    }
}
