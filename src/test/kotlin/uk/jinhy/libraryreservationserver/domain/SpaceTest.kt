package uk.jinhy.libraryreservationserver.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import uk.jinhy.libraryreservationserver.domain.entity.Space
import uk.jinhy.libraryreservationserver.domain.repository.SpaceRepository

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SpaceTest
    @Autowired
    constructor(
        private val spaceRepository: SpaceRepository,
    ) : BehaviorSpec({
            Given("SpaceRepository의") {
                When("save()를 호출하면") {
                    val newSpace =
                        Space(
                            name = "창의팩토리",
                            floor = 1,
                        )
                    val savedNewSpace = spaceRepository.save(newSpace)
                    Then("데이터베이스에 잘 저장되어야한다.") {
                        savedNewSpace.name shouldBe "창의팩토리"
                        savedNewSpace.floor shouldBe 1
                        savedNewSpace.seats.size shouldBe 0
                    }
                }
            }
            Given("SpaceRepository에 장소가 저장되어있고") {
                val savedSpace1 =
                    Space(
                        name = "창의토론실",
                        floor = 1,
                    )
                val savedSpace2 =
                    Space(
                        name = "제3열람실",
                        floor = 3,
                    )
                spaceRepository.save(savedSpace1)
                spaceRepository.save(savedSpace2)

                When("findAll()을 호출하면") {
                    val foundSpaceList = spaceRepository.findAll()
                    Then("도서관의 공간을 모두 불러온다.") {
                        foundSpaceList.size shouldBe 2
                        foundSpaceList.shouldExist { it.name == "창의토론실" && it.floor == 1 }
                        foundSpaceList.shouldExist { it.name == "제3열람실" && it.floor == 3 }
                    }
                }
            }
        })
