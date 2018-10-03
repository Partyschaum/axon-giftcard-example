package de.partyschaum.giftcard.domain.giftcard.projection

import de.partyschaum.giftcard.domain.giftcard.CardSummary
import de.partyschaum.giftcard.domain.giftcard.Event
import de.partyschaum.giftcard.domain.giftcard.Query

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler

class CardSummaryProjection {
    private val cardSummaries = mutableListOf<CardSummary>()

    @EventHandler
    fun on(event: Event) = when (event) {
        is Event.Issued -> issueGiftCard(event.id, event.amount, event.amount)
        is Event.Redeemed -> redeemGiftCard(event.id, event.amount)
    }

    private fun issueGiftCard(id: String, initialAmount: Int, remainingAmount: Int) {
        cardSummaries += CardSummary(id, initialAmount, remainingAmount)
    }

    private fun redeemGiftCard(id: String, amount: Int) {
        val cardSummary = cardSummaries.firstOrNull { id == it.id }
        if (cardSummary != null) {
            cardSummaries -= cardSummary
            cardSummaries += cardSummary.deductAmount(amount)
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
