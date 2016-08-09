package grails.security.rest.redis

import grails.util.Holders
import org.grails.datastore.mapping.engine.event.DatastoreInitializedEvent
import org.springframework.context.ApplicationEvent
import spock.lang.Shared
import java.util.concurrent.Executors

/**
 * There is currently an issue where the functional tests do not allow one to bootstrap data
 * This is because the application context is not ready when the setup methods fire
 *
 * By implementing this trait you can can setup data that is needed for your functional test.
 * All that needs to be done is to implement the static getSetupDataClosure method (more info below)
 */
trait GebBootstrapTrait {

    @Shared
    def executor

    /**
     * The implementing class should include all of the gorm setup data within a closure.
     * Additionally, all of the data calls need to be wrapped with <DomainClass>.withTransaction{}
     * @return Closure
     */
    abstract static Closure getSetupDataClosure()

    def setupSpec() {
        if (!setupDataClosure) return

        def context = Holders.findApplicationContext()
        if (context == null || !context.isActive()) {
            executor = Executors.newSingleThreadExecutor()
            executor.submit {
                while (Holders.findApplicationContext() == null) {
                    // wait
                    sleep 10
                }
                context = Holders.findApplicationContext()
                context.addApplicationListener { ApplicationEvent event ->
                    if (event instanceof DatastoreInitializedEvent) {
                        setupDataClosure.call()
                    }
                }
            }
        } else {
            // context already exists so just use it
            setupDataClosure.call()
        }

    }

    def cleanupSpec() {
        executor?.shutdown()
    }
}
