typedef string GiftCardId

struct IssueGiftCardRequest {
    1: optional GiftCardId id;
    2: optional i32 amount;
}

struct RedeemGiftCardRequest {
    1: optional GiftCardId id;
    2: optional i32 amount;
}

service GiftCard {
    void issueGiftCard(1: IssueGiftCardRequest request)
    void redeemGiftCard(1: RedeemGiftCardRequest request)
}
