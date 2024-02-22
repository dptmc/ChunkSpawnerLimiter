package com.cyprias.chunkspawnerlimiter.messages

object Debug {
    const val IGNORE_ENTITY: String = "Ignoring %s due spawn-reason: %s"
    const val REMOVING_ENTITY_AT: String = "Removing %d %s @ %dx %dz"
    const val ACTIVE_CHECK: String = "Active check @ %dx %dz"
    const val REGISTER_LISTENERS: String = "Registered listeners."
    const val CHUNK_UNLOAD_EVENT: String = "ChunkUnloadEvent %s %s"
    const val CHUNK_LOAD_EVENT: String = "ChunkLoadEvent %s %s"
    const val BLOCK_PLACE_CHECK: String = "Material=%s, Count=%d, Limit=%d"
}