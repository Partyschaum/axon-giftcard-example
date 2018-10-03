package de.partyschaum.gettingStartedWithAxon.domain.giftcard

import org.axonframework.commandhandling.TargetAggregateIdentifier

sealed class Command {
    data class Issue(val id: String, val amount: Int) : Command()
    data class Redeem(@TargetAggregateIdentifier val id: String, val amount: Int) : Command()
}
