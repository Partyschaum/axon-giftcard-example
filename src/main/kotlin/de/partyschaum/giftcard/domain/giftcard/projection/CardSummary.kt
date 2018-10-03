package de.partyschaum.giftcard.domain.giftcard.projection

import de.partyschaum.giftcard.domain.giftcard.CardSummary
import de.partyschaum.giftcard.domain.giftcard.Event
import de.partyschaum.giftcard.domain.giftcard.Query

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler

class CardSummaryProjection {
    private val cardSummaries = mutableListOf<CardSummary>()

    @EventHandler
    fun on(event: Event.Issued) {
        cardSummaries += CardSummary(event.id, event.amount, event.amount)
    }

    @EventHandler
    fun on(event: Event.Redeemed) {
        val cardSummary = cardSummaries.firstOrNull { event.id == it.id }
        if (cardSummary != null) {
            cardSummaries -= cardSummary
            cardSummaries += cardSummary.deductAmount(event.amount)
        }
    }

    @QueryHandler
    fun fetch(query: Query.FetchCardSummaries): List<CardSummary> {
        return cardSummaries
                .asSequence()
                .drop(query.offset)
                .take(query.size)
                .toList()
    }
}
