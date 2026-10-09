package com.willfp.talismans.talismans.util

import com.willfp.eco.core.cache.EcoCache
import com.willfp.eco.core.fast.fast
import com.willfp.talismans.plugin
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.time.Duration

object TalismanUtils {
    private val TALISMAN_MATERIALS = mutableSetOf<Material>()

    private val TALISMAN_LIMITS: EcoCache<Player, Int> = EcoCache.builder<Player, Int>()
        .expireAfterWrite(Duration.ofSeconds(10))
        .build()

    fun convert(itemStack: ItemStack?) {
        if (itemStack == null) {
            return
        }
        if (!isTalismanMaterial(itemStack.type)) {
            return
        }
        val container = itemStack.fast().persistentDataContainer
        val talismanKey = container.keys.firstOrNull { it.namespace == "talismans" } ?: return
        if (!container.has(talismanKey, PersistentDataType.INTEGER)) {
            return
        }
        val level = container.get(talismanKey, PersistentDataType.INTEGER) ?: return
        container.remove(talismanKey)
        container.set(
            plugin.namespacedKeyFactory.create("talisman"),
            PersistentDataType.STRING,
            talismanKey.key + "_" + level
        )
    }

    fun getLimit(player: Player): Int {
        return TALISMAN_LIMITS.get(player) {
            TalismanLimit.fromPermissions(it.effectivePermissions.map { permission -> permission.permission })
        }
    }

    fun clearLimit(player: Player) {
        TALISMAN_LIMITS.invalidate(player)
    }

    fun isTalismanMaterial(material: Material): Boolean {
        return TALISMAN_MATERIALS.contains(material)
    }

    fun registerTalismanMaterial(material: Material) {
        TALISMAN_MATERIALS.add(material)
    }

    fun clearTalismanMaterials() {
        TALISMAN_MATERIALS.clear()
    }
}