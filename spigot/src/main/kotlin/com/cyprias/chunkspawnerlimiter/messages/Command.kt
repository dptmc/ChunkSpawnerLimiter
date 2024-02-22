package com.cyprias.chunkspawnerlimiter.messages

object Command {
    object Reload {
        const val COMMAND: String = "reload"
        const val ALIAS: String = "cslreload"
        const val PERMISSION: String = "csl.reload"
        const val DESCRIPTION: String = "Reloads the config file."
    }

    object Settings {
        const val COMMAND: String = "settings"
        const val ALIAS: String = "cslsettings"
        const val PERMISSION: String = "csl.settings"
        const val DESCRIPTION: String = "Shows config settings."
    }

    object Info {
        const val COMMAND: String = "info"
        const val ALIAS: String = "cslinfo"
        const val PERMISSION: String = "csl.info"
        const val DESCRIPTION: String = "Shows config info."
    }
}