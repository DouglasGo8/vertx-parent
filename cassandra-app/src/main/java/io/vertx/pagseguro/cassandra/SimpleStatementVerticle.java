package io.vertx.pagseguro.cassandra;

import org.apache.log4j.Logger;

import io.vertx.cassandra.ResultSet;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.core.AbstractVerticle;

import static java.lang.System.out;

/**
 * 
 * @author dbatista
 *
 */
public class SimpleStatementVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		Logger.getLogger(SimpleStatementVerticle.class).info("SimpleExample Verticle ready...");

		final CassandraClient client = CassandraClient.createShared(super.vertx,
				new CassandraClientOptions().setPort(9042));

		final String statement = "SELECT * FROM system_schema.keyspaces;";

		client.execute(statement, rs -> {
			if (rs.succeeded()) {

				ResultSet result = rs.result();

				result.all(all -> {

					if (all.succeeded()) {
						all.result().stream().forEach(out::println);
					} else {
						all.cause().printStackTrace();
					}

				});

			} else {
				rs.cause().printStackTrace();
			}
		});
	}

}
