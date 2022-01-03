package no.nav.helse.flex.testdata

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ResetTestdataTest : Testoppsett() {

    @Test
    fun `mottar bestilling med http delete`() {
        val fnr = "12345678987"
        val response = mockMvc.perform(delete("/api/testdata/$fnr"))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        response `should be equal to` "Nullstilling av $fnr bestilt hos flex apper fra flex-testdata-reset"

        val records = kafkaConsumer.ventPåRecords(antall = 1)
        records shouldHaveSize 1
        records.first().key() `should be equal to` fnr
        records.first().value() `should be equal to` fnr
    }

    @Test
    fun `krever 11 siffer`() {
        val fnr = "12345678"
        val response = mockMvc.perform(delete("/api/testdata/$fnr"))
            .andExpect(status().isBadRequest)
            .andReturn().response.contentAsString

        response `should be equal to` "Ugyldig format på fnr"
    }

    @Test
    fun `krever 11 siffer uten bokstaver`() {
        val fnr = "12345678aaa"
        val response = mockMvc.perform(delete("/api/testdata/$fnr"))
            .andExpect(status().isBadRequest)
            .andReturn().response.contentAsString

        response `should be equal to` "Ugyldig format på fnr"
    }
}
