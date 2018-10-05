package de.partyschaum.giftcard.domain.giftcard.boundary.thrift

import de.partyschaum.giftcard.domain.giftcard.Command
import de.partyschaum.giftcard.thrift.GiftCard
import de.partyschaum.giftcard.thrift.GiftCardException
import de.partyschaum.giftcard.thrift.IssueGiftCardRequest
import de.partyschaum.giftcard.thrift.RedeemGiftCardRequest

import org.axonframework.commandhandling.gateway.CommandGateway

class ThriftHandler(private val commandGateway: CommandGateway) : GiftCard.Iface {
    override fun issueGiftCard(request: IssueGiftCardRequest?) {
        if (request == null) {
            error("No request")
        }
        commandGateway.sendAndWait<Nothing>(Command.Issue(request.id, request.amount))
    }

    override fun redeemGiftCard(request: RedeemGiftCardRequest?) {
        if (request == null) {
            error("No request")
        }
        commandGateway.sendAndWait<Nothing>(Command.Redeem(request.id, request.amount))
    }

    private fun error(message: String): Nothing {
        val exception = GiftCardException()
        exception.setMessage(message)
        throw exception
    }
}
