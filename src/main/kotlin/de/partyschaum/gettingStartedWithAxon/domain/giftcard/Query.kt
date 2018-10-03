package de.partyschaum.gettingStartedWithAxon.domain.giftcard

sealed class Query {
    data class FetchCardSummaries(val size: Int, val offset: Int) : Query()
}
