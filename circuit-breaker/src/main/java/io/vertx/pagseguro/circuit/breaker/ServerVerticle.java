package io.vertx.pagseguro.circuit.breaker;

import io.vertx.core.AbstractVerticle;
import lombok.NoArgsConstructor;

/**
 * 
 * @author dbatista
 *
 */
@NoArgsConstructor
public class ServerVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		
		super.vertx.createHttpServer()
			.requestHandler(req -> req.response().end("Hi"))
			.listen(8999);
	}
}
