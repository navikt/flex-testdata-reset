package no.nav.helse.flex.testdata.reset

import no.nav.helse.flex.testdata.kafka.TESTDATA_RESET_TOPIC
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ResetController(
    private val producer: KafkaProducer<String, String?>

) {

    @DeleteMapping(value = ["/api/testdata/{fnr}"])
    @CrossOrigin
    fun nullstillAktor(@PathVariable fnr: String): ResponseEntity<String> {
        if (fnr.any { !it.isDigit() } || fnr.length != 11) {
            return ResponseEntity.badRequest().body("Ugyldig format på fnr")
        }

        producer.send(ProducerRecord(TESTDATA_RESET_TOPIC, fnr, null)).get()
        return ResponseEntity.ok("Resetting av $fnr bestilt")
    }
}
