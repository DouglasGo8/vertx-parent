package com.nubank.check.balance;

import com.nubank.check.balance.api.rest.CheckBalanceServiceVerticle;
import com.nubank.check.balance.infrastructure.database.CheckBalanceRepoVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author nubank
 */
public class MainVerticle extends AbstractVerticle {


    private Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    /**
     *
     * @throws Exception at All
     */
    @Override
    public void start() throws Exception {




        logger.info("**** MainVerticle ready... ****");

        super.vertx.deployVerticle(CheckBalanceServiceVerticle.class.getName(),
                new DeploymentOptions().setConfig(super.config()));

        super.vertx.deployVerticle(CheckBalanceRepoVerticle.class.getName(),
                new DeploymentOptions());
    }
}
