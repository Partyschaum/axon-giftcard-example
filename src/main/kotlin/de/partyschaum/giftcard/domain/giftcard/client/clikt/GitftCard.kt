package de.partyschaum.giftcard.domain.giftcard.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

class GiftCard : CliktCommand() {
    override fun run() {}
}

class IssueGiftCard : CliktCommand(name = "issue-gift-card") {
    override fun run() {
        println("Issue gift card")
    }
}

class RedeemGiftCard : CliktCommand(name = "redeem-gift-card") {
    override fun run() {
        println("Redeem gift card")
    }
}

fun main(args: Array<String>): Unit = GiftCard().subcommands(listOf(
        IssueGiftCard(),
        RedeemGiftCard()
)
).main(args)
