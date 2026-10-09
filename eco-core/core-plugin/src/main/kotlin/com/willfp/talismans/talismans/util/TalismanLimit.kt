package com.willfp.talismans.talismans.util

import kotlin.text.toIntOrNull

object TalismanLimit {
    fun fromPermissions(permissions: Iterable<String>): Int {
        val prefix = "talismans.limit."
        var highest = -1
        for (permission in permissions) {
            if (permission.startsWith(prefix)) {
                val limit = permission.substringAfterLast('.').toIntOrNull() ?: continue
                if (limit > highest) {
                    highest = limit
                }
            }
        }
        return if (highest < 0) {
            10000
        } else {
            highest
        }
    }
}
