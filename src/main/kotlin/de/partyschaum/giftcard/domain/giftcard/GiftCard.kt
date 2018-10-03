package de.partyschaum.giftcard.domain.giftcard

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler


class GiftCard() {

    @AggregateIdentifier
    private lateinit var id: String

    private var remainingValue: Int = 0

    @CommandHandler
    constructor(command: Command.Issue) : this() {
        if (command.amount <= 0) throw IllegalArgumentException("amount <= 0")
        AggregateLifecycle.apply(Event.Issued(command.id, command.amount))
    }

    @CommandHandler
    fun handle(command: Command.Redeem) {
        if (command.amount <= 0) throw IllegalArgumentException("amount <= 0")
        if (command.amount > remainingValue) throw IllegalStateException("amount > remaining value")
        AggregateLifecycle.apply(Event.Redeemed(id, command.amount))
    }

    @EventSourcingHandler
    fun on(event: Event.Issued) {
        id = event.id
        remainingValue = event.amount
    }

    @EventSourcingHandler
    fun on(event: Event.Redeemed) {
        remainingValue -= event.amount
    }
}
