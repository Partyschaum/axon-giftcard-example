package de.partyschaum.giftcard.domain.giftcard

data class CardSummary(val id: String, val initialAmount: Int, val remainingAmount: Int) {
    fun deductAmount(toBeDeducted: Int): CardSummary {
        return this.copy(remainingAmount = remainingAmount - toBeDeducted)
    }
}
