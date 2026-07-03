/**
 * MIT License
 * <p>
 * Copyright (c) 2021 TriumphTeam
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:Suppress("removal")

package me.gabytm.minecraft.guihelper.util

import org.bukkit.Bukkit
import java.util.regex.Pattern


/**
 * Class for detecting server version for legacy support
 * @author Matt ([@ipsk](https://github.com/ipsk))
 */
object ServerVersion {

    private val CURRENT: Int = getCurrentVersion()

    /**
     * **NO**
     */
    val IS_EXTREMELY_OLD: Boolean = CURRENT < 1_08_00 // 1.8

    /**
     * Whether the [CURRENT] version is *ancient* (< **1.9**)
     */
    val IS_ANCIENT: Boolean = CURRENT < 1_09_00

	/**
	 * Whether the [CURRENT] version is *legacy* (< **1.13**)
	 */
	val IS_LEGACY: Boolean = CURRENT < 1_13_00

    /**
     * Whether the [CURRENT] version has [org.bukkit.attribute.Attribute] (>= **1.13.2**)
     */
    val HAS_ATTRIBUTE: Boolean = CURRENT >= 1_13_02

    /**
     * Whether in the [CURRENT] version, [org.bukkit.inventory.meta.ItemMeta] has `unbreakable` methods (>= **1.11**)
     * @see org.bukkit.inventory.meta.ItemMeta.isUnbreakable
     * @see org.bukkit.inventory.meta.ItemMeta.setUnbreakable
     */
    val ITEM_META_HAS_UNBREAKABLE: Boolean = CURRENT >= 1_11_00

    /**
     * Whether the [CURRENT] version has [org.bukkit.inventory.meta.SpawnEggMeta] (>= **1.11**)
     */
    val HAS_SPAWN_EGG_META: Boolean = CURRENT >= 1_11_00

	/**
	 * Whether in the [CURRENT] version, [org.bukkit.inventory.meta.PotionMeta] has `color` methods (>= **1.11**)
	 * @see org.bukkit.inventory.meta.PotionMeta.hasColor
	 * @see org.bukkit.inventory.meta.PotionMeta.getColor
	 * @see org.bukkit.inventory.meta.PotionMeta.setColor
	 */
	val POTION_META_HAS_COLOR: Boolean = CURRENT >= 1_11_00

    /**
     * Whether in the [CURRENT] version, [org.bukkit.inventory.meta.SkullMeta] has `owning player` (>= **1.12**)
     * @see org.bukkit.inventory.meta.SkullMeta.getOwningPlayer
     * @see org.bukkit.inventory.meta.SkullMeta.setOwningPlayer
     */
    val SKULL_META_HAS_OWNING_PLAYER: Boolean = CURRENT >= 1_12_00

    /**
     * Whether the [CURRENT] version has
     * [CustomModelData](https://minecraft.fandom.com/wiki/Player.dat_format#General_Tags) (>= **1.14**)
     * @see org.bukkit.inventory.meta.ItemMeta.hasCustomModelData
     * @see org.bukkit.inventory.meta.ItemMeta.getCustomModelData
     */
    val HAS_CUSTOM_MODEL_DATA: Boolean = CURRENT >= 1_14_00

    /**
     * Whether the [CURRENT] version has HEX support (>= **1.15.2**)
     */
    val HAS_HEX: Boolean = CURRENT >= 1_15_02

	/**
	 * Whether the [CURRENT] version has [org.bukkit.profile] API (>= **1.19**)
	 */
	val HAS_PROFILE_API: Boolean = CURRENT >= 1_19_00

	/**
	 * Whether in the [CURRENT] version [org.bukkit.potion.PotionData] is deprecated or removed (>= **1.20.5**)
	 */
	val POTION_DATA_IS_DEPRECATED: Boolean = CURRENT >= 1_20_05

    /**
     * Gets the current server version
     *
     * @return A protocol like number representing the version, for example 1.16.5 - 11605
     */
    private fun getCurrentVersion(): Int {
        // No need to cache since will only run once
        val matcher = Pattern.compile("(?<major>\\d+)\\.(?<minor>\\d+)(?:\\.(?<patch>\\d+))?").matcher(Bukkit.getBukkitVersion())

        if (!matcher.find()) {
            throw RuntimeException("Could not retrieve server version from: " + Bukkit.getBukkitVersion())
        }

        val major = matcher.group("major").toInt()
        val minor = matcher.group("minor").toInt()
        val patch = matcher.group("patch")?.toInt() ?: 0

        return major * 10000 + minor * 100 + patch
    }

}
