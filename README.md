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
  reflected when we do a replay. *[1]*
  
## Questions

  - What do `sealed` classes do in Kotlin?
    > Sealed classes are used for representing restricted class hierarchies, when
      a value can have one of the types from a limited set, but cannot have any
      other type. They are, in a sense, an extension of enum classes: the set of
      values for an enum type is also restricted, but each enum constant exists
      only as a single instance, whereas a subclass of a sealed class can have
      multiple instances which can contain state. *[2]*

    > The key benefit of using sealed classes comes into play when you use them
      in a when expression. If it's possible to verify that the statement covers
      all cases, you don't need to add an else clause to the statement. However,
      this works only if you use when as an expression (using the result) and not
      as a statement. *[2]*

  - Do I need the empty default constructor in `GiftCard`?
    > If you are using Axon for Event Sourcing, default constructor is needed
      so Axon can instantiate the Aggregate and apply all sourced events. *[1]*

## Links

  - *[1]* [Axon Quickstart - Aggregate](https://docs.axonframework.org/part-i-getting-started/quick-start#aggregate)
  - *[2]* [Sealed Classes](http://kotlinlang.org/docs/reference/sealed-classes.html)
