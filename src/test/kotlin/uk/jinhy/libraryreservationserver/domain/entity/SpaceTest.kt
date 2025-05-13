package uk.jinhy.libraryreservationserver.domain.entity

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import uk.jinhy.libraryreservationserver.domain.repository.SpaceRepository

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SpaceTest @Autowired constructor(
    private val spaceRepository: SpaceRepository,
) : BehaviorSpec({
    Given("SpaceRepository은") {
        When("항상") {
            Then("null이 아니다.") {
                spaceRepository shouldNotBe null
            }
        }
    }
})
