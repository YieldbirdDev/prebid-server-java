package org.prebid.server.functional.testcontainers

import org.prebid.server.functional.testcontainers.container.NetworkServiceContainer
import org.prebid.server.functional.util.ObjectMapperWrapper
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.Network
import org.testcontainers.lifecycle.Startables

class Dependencies {

    static final ObjectMapperWrapper objectMapperWrapper = new ObjectMapperWrapper()

    static final Network network = Network.newNetwork()

    static final MySQLContainer mysqlContainer = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("prebid")
            .withUsername("prebid")
            .withPassword("prebid")
            .withInitScript("org/prebid/server/functional/db_schema.sql")
            .withNetwork(network)

    static final NetworkServiceContainer networkServiceContainer = new NetworkServiceContainer(System.getProperty("mockserver.version"))
            .withNetwork(network)

    static void start() {
        Startables.deepStart([networkServiceContainer, mysqlContainer])
                  .join()
    }

    static void stop() {
        [networkServiceContainer, mysqlContainer].parallelStream()
                                                 .forEach({ it.stop() })
    }

    private Dependencies() {} // should not be instantiated
}
