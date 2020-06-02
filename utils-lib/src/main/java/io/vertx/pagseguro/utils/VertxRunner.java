
package io.vertx.pagseguro.utils;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.hazelcast.config.Config;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;


/**
 *
 */
public class VertxRunner {

    /**
     * @param clazz
     */
    public static void runVerticle(Class<? extends AbstractVerticle> clazz) {

        final DeploymentOptions deploymentOptions = new DeploymentOptions();
        //
        deploymentOptions.setInstances(2);
        //
        final Consumer<Vertx> runner = vertx -> {

            vertx.deployVerticle(clazz.getName(), deploymentOptions);

        };

        final Vertx vertx = Vertx.vertx(new VertxOptions());

        runner.accept(vertx);
    }

    /**
     * @param runner
     */
    public static void runVerticle(final Consumer<Vertx> runner) {
        runner.accept(Vertx.vertx(new VertxOptions()));
    }

    /**
     * @param runner
     */
    public static void runVerticle(final Consumer<Vertx> runner, long maxEventLoopTime) {
        runner.accept(Vertx.vertx(new VertxOptions()
                .setBlockedThreadCheckInterval(maxEventLoopTime)
                .setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS)));
    }

    /**
     * @param runner
     */
    public static void clusteredVertx(final Consumer<Vertx> runner) {

        final Config haZelCastConfig = new Config();

        haZelCastConfig
                .getNetworkConfig()
                .getJoin()
                .getTcpIpConfig()
                .addMember("127.0.0.1").setEnabled(true);

        haZelCastConfig
                .getNetworkConfig()
                .getJoin()
                .getMulticastConfig().setEnabled(false);

        final ClusterManager mgr = new HazelcastClusterManager(haZelCastConfig);

        final VertxOptions options = new VertxOptions().setClusterManager(mgr);

        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                runner.accept(res.result());
            } else {
                throw new IllegalArgumentException(res.cause());
            }
        });
    }

}