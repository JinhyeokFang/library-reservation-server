package uk.jinhy.libraryreservationserver.global.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class LoggerDelegate : ReadOnlyProperty<Any?, Logger> {
    private lateinit var logger: Logger

    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): Logger {
        if (!::logger.isInitialized) {
            logger = LoggerFactory.getLogger(thisRef!!.javaClass)
        }
        return logger
    }
}

val logger = LoggerDelegate()
