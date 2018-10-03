package de.partyschaum.giftcard

import de.partyschaum.giftcard.domain.giftcard.CardSummary
import de.partyschaum.giftcard.domain.giftcard.Command
import de.partyschaum.giftcard.domain.giftcard.GiftCard
import de.partyschaum.giftcard.domain.giftcard.Query
import de.partyschaum.giftcard.domain.giftcard.projection.CardSummaryProjection

import org.axonframework.config.DefaultConfigurer
import org.axonframework.config.EventHandlingConfiguration
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.axonframework.queryhandling.responsetypes.ResponseTypes

object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        val cardSummaryProjection = CardSummaryProjection()

        val eventHandlingConfiguration = EventHandlingConfiguration()

        eventHandlingConfiguration.registerEventHandler { _ -> cardSummaryProjection }

        val configuration = DefaultConfigurer.defaultConfiguration()
                .configureAggregate(GiftCard::class.java)
                .configureEventStore { _ -> EmbeddedEventStore(InMemoryEventStorageEngine()) }
                .registerModule(eventHandlingConfiguration)
                .registerQueryHandler { _ -> cardSummaryProjection }
                .buildConfiguration()

        configuration.start()

        val commandGateway = configuration.commandGateway()
        val queryGateway = configuration.queryGateway()

        commandGateway.sendAndWait<Nothing>(Command.Issue("gc1", 100))
        commandGateway.sendAndWait<Nothing>(Command.Issue("gc2", 50))
        commandGateway.sendAndWait<Nothing>(Command.Redeem("gc1", 10))
        commandGateway.sendAndWait<Nothing>(Command.Redeem("gc2", 20))

        val cardSummaries = queryGateway.query(
                Query.FetchCardSummaries(2, 0),
                ResponseTypes.multipleInstancesOf(CardSummary::class.java)
        ).get()

        cardSummaries.forEach { println(it) }
    }
}
