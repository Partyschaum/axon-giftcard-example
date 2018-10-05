package de.partyschaum.giftcard

import com.mysql.cj.jdbc.MysqlDataSource

import de.partyschaum.giftcard.domain.giftcard.GiftCard
import de.partyschaum.giftcard.domain.giftcard.boundary.thrift.ThriftHandler
import de.partyschaum.giftcard.domain.giftcard.projection.CardSummaryProjection
import de.partyschaum.giftcard.thrift.GiftCard.Processor

import org.apache.thrift.server.TServer.Args
import org.apache.thrift.server.TSimpleServer
import org.apache.thrift.transport.TNonblockingServerSocket

import org.axonframework.common.jdbc.DataSourceConnectionProvider
import org.axonframework.common.transaction.NoTransactionManager
import org.axonframework.config.DefaultConfigurer
import org.axonframework.config.EventHandlingConfiguration
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore
import org.axonframework.eventsourcing.eventstore.jdbc.JdbcEventStorageEngine
import org.axonframework.eventsourcing.eventstore.jdbc.MySqlEventTableFactory

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

        val thriftHandler = ThriftHandler(configuration.commandGateway())

        val serverSocket = TNonblockingServerSocket(9090)
        val serverArgs = Args(serverSocket)
        val giftCardProcessor = Processor(thriftHandler)
        val server = TSimpleServer(serverArgs.processor(giftCardProcessor))

        try {
            server.serve()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
