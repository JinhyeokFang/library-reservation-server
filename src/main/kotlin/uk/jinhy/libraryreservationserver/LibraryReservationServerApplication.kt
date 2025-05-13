package uk.jinhy.libraryreservationserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LibraryReservationServerApplication

fun main(args: Array<String>) {
    runApplication<LibraryReservationServerApplication>(*args)
}
