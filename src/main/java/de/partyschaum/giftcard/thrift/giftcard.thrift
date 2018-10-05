namespace java de.partyschaum.giftcard.thrift

typedef string GiftCardId

struct IssueGiftCardRequest {
    1: optional GiftCardId id
    2: optional i32 amount
}

struct RedeemGiftCardRequest {
    1: optional GiftCardId id
    2: optional i32 amount
}

exception GiftCardException {
    1: optional string message
}

service GiftCard {
    void issueGiftCard(1: IssueGiftCardRequest request) throws (1: GiftCardException error)
    void redeemGiftCard(1: RedeemGiftCardRequest request) throws (1: GiftCardException error)
}
