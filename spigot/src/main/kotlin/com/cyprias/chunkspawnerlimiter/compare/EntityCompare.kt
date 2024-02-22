package com.cyprias.chunkspawnerlimiter.compare

import org.bukkit.entity.Entity


fun interface EntityCompare {
    /**
     * Evaluates the given Entity against the specific criteria as defined by the implementing class.
     *
     * @param entity The entity to evaluate.
     * @return Returns true if the entity is found to be similar.
     */
    fun isSimilar(entity: Entity?): Boolean
}