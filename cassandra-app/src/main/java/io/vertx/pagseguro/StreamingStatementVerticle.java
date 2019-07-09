package io.vertx.pagseguro;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.cassandra.CassandraRowStream;
import io.vertx.core.AbstractVerticle;

/**
 * 
 * @author dbatista
 *
 */
public class StreamingStatementVerticle extends AbstractVerticle {

	
	public StreamingStatementVerticle() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void start() throws Exception {
		
		
		// TODO Auto-generated method stub
		final CassandraClient client = CassandraClient.createShared(vertx, new CassandraClientOptions().setPort(9042));
		/**
		 * 
		 */

		final String statement = "SELECT * from system_schema.tables  WHERE keyspace_name = 'system_schema' ";

		client.queryStream(statement, cassandraRowStreamAsyncResult -> {
			if (cassandraRowStreamAsyncResult.succeeded()) {
				System.out.println("Tables in system_schema: ");
				
				CassandraRowStream stream = cassandraRowStreamAsyncResult.result();
				
				stream.endHandler(end -> {
					System.out.println("<End of Cassandra row stream>");
				}).handler(row -> {
					String systemSchema = row.getString("table_name");
					System.out.println("\t" + systemSchema);
				}).exceptionHandler(throwable -> {
					System.out.println("An exception occurred:");
					throwable.printStackTrace();
				});
				
			} else {
				System.out.println("Unable to execute the query");
				cassandraRowStreamAsyncResult.cause().printStackTrace();
			}
		});
	}

}
