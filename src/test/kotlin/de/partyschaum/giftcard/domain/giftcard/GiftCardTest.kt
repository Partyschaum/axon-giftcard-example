package de.partyschaum.giftcard.domain.giftcard

import de.partyschaum.giftcard.util.whenever

import io.kotlintest.Description
import io.kotlintest.specs.StringSpec

import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration


class GiftCardTest : StringSpec() {

    lateinit var fixture: FixtureConfiguration<GiftCard>

    override fun beforeTest(description: Description) {
        fixture = AggregateTestFixture(GiftCard::class.java)
    }

    init {
        "Command.Issue('gift-card-1', 100) issues a GiftCard" {
            fixture.givenNoPriorActivity()
                    .whenever(Command.Issue("gift-card-1", 100))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(Event.Issued("gift-card-1", 100))
        }
    }
}
