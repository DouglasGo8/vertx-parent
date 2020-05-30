package io.vertx.pagseguro.cassandra;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.core.AbstractVerticle;
import static java.lang.System.out;

import com.datastax.driver.core.PreparedStatement;

/**
 * 
 * @author dbatista
 *
 */
public class PreparedStatementVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub

		final CassandraClient client = CassandraClient.createNonShared(super.vertx,
				new CassandraClientOptions().setPort(9042));

		final String statement = "SELECT * from system_schema.tables  WHERE keyspace_name = ?";

		client.prepare(statement, preparedStatementAsyncResult -> {
			if (preparedStatementAsyncResult.succeeded()) {

				out.println("The query has successfully been prepared");

				final PreparedStatement preparedStatement = preparedStatementAsyncResult.result();

				client.executeWithFullFetch(preparedStatement.bind("system_schema"), listAsyncResult -> {
					if (listAsyncResult.succeeded()) {
						out.println("Tables in system_schema: ");
						listAsyncResult.result().forEach(row -> {
							out.println("\t" + row.getString("table_name"));
						});
					} else {
						listAsyncResult.cause().printStackTrace();
					}
				});

			} else {
				out.println("Unable to execute a prepared statement");
				preparedStatementAsyncResult.cause().printStackTrace();
			}
		});

	}
}
