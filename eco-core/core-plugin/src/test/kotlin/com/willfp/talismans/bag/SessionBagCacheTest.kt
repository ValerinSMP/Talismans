package com.willfp.talismans.bag

import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SessionBagCacheTest {
    @Test fun `same session preserves edits and empty bags`() {
        val cache = SessionBagCache<List<String>>()
        val id = UUID.randomUUID()
        val profile = Any()
        cache.put(id, profile, listOf("strength"))
        assertEquals(listOf("strength"), cache.get(id, profile))
        cache.put(id, profile, emptyList())
        assertEquals(emptyList(), cache.get(id, profile))
    }

    @Test fun `equal profiles in different sessions cannot reuse old bag`() {
        data class Profile(val id: UUID)
        val id = UUID.randomUUID()
        val cache = SessionBagCache<List<String>>()
        cache.put(id, Profile(id), emptyList())
        assertNull(cache.get(id, Profile(id)))
    }

    @Test fun `quit removes only departing player`() {
        val cache = SessionBagCache<String>()
        val a = UUID.randomUUID()
        val b = UUID.randomUUID()
        val profile = Any()
        cache.put(a, profile, "a")
        cache.put(b, profile, "b")
        cache.remove(a)
        assertNull(cache.get(a, profile))
        assertEquals("b", cache.get(b, profile))
    }
}
