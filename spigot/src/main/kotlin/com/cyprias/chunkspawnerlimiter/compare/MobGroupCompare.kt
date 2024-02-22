package com.cyprias.chunkspawnerlimiter.compare

import org.bukkit.entity.*
import org.jetbrains.annotations.Contract

class MobGroupCompare(private var mobGroup: String?): EntityCompare {

    override fun isSimilar(entity: Entity?): Boolean {
        return (getMobGroup(entity) == this.mobGroup)
    }

    companion object {
        @Contract(pure = true)
        fun getMobGroup(entity: Entity?): String {
            // Determine the general group this mob belongs to.
            if (entity is Animals) {
                // Chicken, Cow, MushroomCow, Ocelot, Pig, Sheep, Wolf
                return "ANIMAL"
            }

            if (entity is Monster) {
                // Blaze, CaveSpider, Creeper, Enderman, Giant, PigZombie, Silverfish, Skeleton, Spider, Witch, Wither, Zombie
                return "MONSTER"
            }

            if (entity is Ambient) {
                // Bat
                return "AMBIENT"
            }

            if (entity is WaterMob) {
                // Squid, Fish
                return "WATER_MOB"
            }

            if (entity is NPC) {
                // Villager
                return "NPC"
            }

            if (entity is Vehicle) {
                //Boat, Minecart, TnT Minecart etc.
                return "VEHICLE"
            }

            // Anything else.
            return "OTHER"
        }
    }

}