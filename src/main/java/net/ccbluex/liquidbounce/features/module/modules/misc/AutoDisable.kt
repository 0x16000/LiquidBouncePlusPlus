/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */
package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.minecraft.network.play.server.S08PacketPlayerPosLook

@ModuleInfo(name = "AutoDisable", spacedName = "Auto Disable", description = "Automatically disable modules for you on flag or world respawn.", category = ModuleCategory.MISC, array = false)
class AutoDisable : Module() {
    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (event.packet is S08PacketPlayerPosLook) disableModules(DisableEvent.FLAG)
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        disableModules(DisableEvent.WORLD_CHANGE)
    }

    fun disableModules(enumDisable: DisableEvent) {
        var moduleNames: Int = 0
        LiquidBounce.moduleManager.modules.filter { it.autoDisable == enumDisable && it.state }.forEach { it.toggle(); moduleNames++ }

        if (moduleNames <= 0) return
        LiquidBounce.hud.addNotification(Notification("Disabled $moduleNames modules due to ${ when (enumDisable) {
                FLAG -> "unexpected teleport"
                WORLD_CHANGE -> "world change"
                else -> "game ended"
            }}.", Notification.Type.INFO, 1000L))
    }

    @JvmStatic
    fun handleGameEnd() {
        val autoDisableModule = LiquidBounce.moduleManager[AutoDisable::class.java]!! as AutoDisable
        autoDisableModule.disableModules(DisableEvent.GAME_END)
    }

    enum class DisableEvent {
        NONE,
        FLAG,
        WORLD_CHANGE,
        GAME_END
    }
}