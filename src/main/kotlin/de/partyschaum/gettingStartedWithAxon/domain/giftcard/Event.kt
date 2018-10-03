package de.partyschaum.gettingStartedWithAxon.domain.giftcard

sealed class Event {
    data class Issued(val id: String, val amount: Int) : Event()
    data class Redeemed(val id: String, val amount: Int) : Event()
}
