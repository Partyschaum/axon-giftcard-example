package de.partyschaum.giftcard

import com.mysql.cj.jdbc.MysqlDataSource

import de.partyschaum.giftcard.domain.giftcard.CardSummary
import de.partyschaum.giftcard.domain.giftcard.Command
import de.partyschaum.giftcard.domain.giftcard.GiftCard
import de.partyschaum.giftcard.domain.giftcard.Query
import de.partyschaum.giftcard.domain.giftcard.projection.CardSummaryProjection

import org.axonframework.common.jdbc.DataSourceConnectionProvider
import org.axonframework.common.transaction.NoTransactionManager
import org.axonframework.config.DefaultConfigurer
import org.axonframework.config.EventHandlingConfiguration
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore
import org.axonframework.eventsourcing.eventstore.jdbc.JdbcEventStorageEngine
import org.axonframework.eventsourcing.eventstore.jdbc.MySqlEventTableFactory
import org.axonframework.queryhandling.responsetypes.ResponseTypes

object Application {
    @JvmStatic
    fun main(args: Array<String>) {

        val eventHandlingConfiguration = EventHandlingConfiguration()
        eventHandlingConfiguration.registerEventHandler { _ -> CardSummaryProjection() }

        val dataSource = MysqlDataSource()
        with(dataSource) {
            databaseName = "giftcard"
            user = "giftcard"
            setPassword("secret")
            serverName = "localhost"
        }

        val connectionProvider = DataSourceConnectionProvider(dataSource)
        val transactionManager = NoTransactionManager.instance()

        val jdbcEventStorageEngine = JdbcEventStorageEngine(connectionProvider, transactionManager)
        jdbcEventStorageEngine.createSchema(MySqlEventTableFactory())

        val configuration = DefaultConfigurer.defaultConfiguration()
                .configureAggregate(GiftCard::class.java)
                .configureEventStore { _ -> EmbeddedEventStore(jdbcEventStorageEngine) }
                .registerModule(eventHandlingConfiguration)
                .registerQueryHandler { _ -> CardSummaryProjection() }
                .buildConfiguration()

        configuration.start()

        val commandGateway = configuration.commandGateway()
        val queryGateway = configuration.queryGateway()

        commandGateway.sendAndWait<Nothing>(Command.Issue("gc1", 100))
        commandGateway.sendAndWait<Nothing>(Command.Issue("gc2", 50))
        commandGateway.sendAndWait<Nothing>(Command.Redeem("gc1", 10))
        commandGateway.sendAndWait<Nothing>(Command.Redeem("gc2", 20))

        val cardSummaries = queryGateway.query(
                Query.FetchCardSummaries(2, 0),
                ResponseTypes.multipleInstancesOf(CardSummary::class.java)
        ).get()

        cardSummaries.forEach { println(it) }
    }
}
