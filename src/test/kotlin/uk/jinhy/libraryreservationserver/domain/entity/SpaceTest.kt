package uk.jinhy.libraryreservationserver.domain.entity

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import uk.jinhy.libraryreservationserver.domain.repository.SpaceRepository

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SpaceTest @Autowired constructor(
    private val spaceRepository: SpaceRepository,
) : BehaviorSpec({
    Given("Space가 주어젔을 때") {
        val newSpace = Space(
            name = "창의팩토리",
            floor = 1,
        )
        When("저장을 시도하면") {
            val savedSpace = spaceRepository.save(newSpace);
            Then("데이터베이스에 잘 저장된다.") {
                savedSpace.name shouldBe "창의팩토리"
                savedSpace.floor shouldBe 1
                savedSpace.seats.size shouldBe 0
            }
        }
    }
})
