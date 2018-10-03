# Axon Gift Card Example

An example project for getting started with Kotlin and Axon Framework.

## Tech Stack

  - Axon Framework

## Axon Notes

Why does all business logic belong into the command handlers?

> All business logic / rules are defined in the @CommandHandlers, and all state
  changes are defined in the @EventSourcingHandlers. The reason for this is when
  we want to get the current state of event-sourced Aggregate, we have to apply
  all sourced events - we have to invoke @EventSourcingHandlers. If the state
  of our Aggregate is changed outside of @EventSourcingHandlers it will not be
  reflected when we do a replay.
  
## Questions

  - What do `sealed` classes do in Kotlin?
  - Do I need the empty default constructor in `GiftCard`?
    > If you are using Axon for Event Sourcing, default constructor is needed
      so Axon can instantiate the Aggregate and apply all sourced events.
