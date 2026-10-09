package com.willfp.talismans.bag

import com.willfp.libreforge.forceRefreshHolders
import com.willfp.libreforge.toDispatcher
import com.willfp.talismans.plugin
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent

internal object TalismanLifecycleListener : Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    fun onJoin(event: PlayerJoinEvent) = refreshNextTick(event.player)

    @EventHandler(priority = EventPriority.MONITOR)
    fun onRespawn(event: PlayerRespawnEvent) = refreshNextTick(event.player)

    @EventHandler(priority = EventPriority.MONITOR)
    fun onQuit(event: PlayerQuitEvent) {
        TalismanBag.forget(event.player)
    }

    private fun refreshNextTick(player: Player) {
        plugin.scheduler.run {
            if (player.isOnline) {
                player.toDispatcher().forceRefreshHolders()
            }
        }
    }
}
