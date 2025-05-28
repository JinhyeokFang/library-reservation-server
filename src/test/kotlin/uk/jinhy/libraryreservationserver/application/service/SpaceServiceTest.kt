package uk.jinhy.libraryreservationserver.application.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import uk.jinhy.libraryreservationserver.domain.entity.Seat
import uk.jinhy.libraryreservationserver.domain.entity.Space
import uk.jinhy.libraryreservationserver.domain.repository.SpaceRepository

private var spaceRepository = mockk<SpaceRepository>()

@InjectMockKs
private var spaceService: SpaceService = SpaceServiceImpl(spaceRepository)

@ExtendWith(MockKExtension::class)
class SpaceServiceTest : BehaviorSpec({
    Given("SpaceService의") {
        val seatsOfSpace1: MutableList<Seat> = mutableListOf(
            Seat(
                code = 392,
                name = "2번",
            ),
            Seat(
                code = 393,
                name = "3번",
            ),
        )
        val space1 = Space(name = "창의팩토리", floor = 1, seats = seatsOfSpace1)
        val space2 = Space(name = "창의토론실", floor = 1)
        val savedSpaces = listOf(
            space1,
            space2,
        )

        every { spaceRepository.findAll() } returns savedSpaces

        When("getSpaceList()를 호출하면") {
            val spaces = spaceService.getSpaceList()
            Then("도서관의 모든 공간을 불러올 수 있어야 한다.") {
                val space1FromService = spaces.first { it.name == "창의팩토리" }
                val space2FromService = spaces.first { it.name == "창의토론실" }

                spaces shouldHaveSize 2

                space1FromService.floor shouldBe 1
                space1FromService.seats.size shouldBe 2

                space2FromService.floor shouldBe 1
            }
        }
    }
})
