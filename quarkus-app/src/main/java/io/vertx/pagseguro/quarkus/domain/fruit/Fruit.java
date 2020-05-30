package io.vertx.pagseguro.quarkus.domain.fruit;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.reactiverse.axle.pgclient.PgIterator;
import io.reactiverse.axle.pgclient.PgPool;
import io.reactiverse.axle.pgclient.PgRowSet;
import io.reactiverse.axle.pgclient.Row;
import io.reactiverse.axle.pgclient.Tuple;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.NoArgsConstructor;

/**
 *
 */
@NoArgsConstructor
@ApplicationScoped
public class Fruit {

	@Inject
	private PgPool client;

	private Long id;
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public CompletionStage<JsonArray> findAll() {

		return this.client.query("SELECT id, name FROM fruits ORDER BY id ASC").thenApply(pgRowSet -> {

			final JsonArray array = new JsonArray();
			final PgIterator pgIterator = pgRowSet.iterator();
			/**
			 *
			 */
			while (pgIterator.hasNext()) {
				array.add(this.funFruit(pgIterator.next()));
			}

			return array;
		});
	}

	/**
	 * @param id
	 * @return
	 */
	public CompletionStage<JsonObject> findById(Long id) {

		return this.client.preparedQuery("SELECT id, name FROM Fruits WHERE id = $1", Tuple.of(id))
				.thenApply(PgRowSet::iterator).thenApply(this::funFruit);
		// .thenApply(iterator -> iterator.hasNext() ? funFruit(iterator.next()): null);

	}

	/**
	 * @param fruit
	 * @return
	 */
	public CompletionStage<JsonObject> save(Fruit fruit) {
		return this.client.preparedQuery("INSERT INTO Fruits (name) VALUES($1) RETURNING (id)", Tuple.of(fruit.name))
				.thenApply(this::funFruitInsertId);

	}

	public CompletionStage<JsonObject> update(Fruit fruit) {
		return this.client.preparedQuery("UPDATE Fruits SET name = $1 WHERE id = $2", Tuple.of(fruit.name, fruit.id))
				.thenApply(this::funFruitUpdateAndDeleteId);
	}

	public CompletionStage<JsonObject> delete(Long id) {
		return this.client.preparedQuery("DELETE FROM Fruits WHERE id = $1", Tuple.of(id))
				.thenApply(this::funFruitUpdateAndDeleteId);
	}

	/**
	 * @param row
	 * @return
	 */
	private JsonObject funFruit(Row row) {
		return new JsonObject().put("id", row.getLong("id")).put("name", row.getString("name"));
	}

	/**
	 * @param rowSet
	 * @return
	 */
	private JsonObject funFruitInsertId(PgRowSet rowSet) {
		return new JsonObject().put("id", rowSet.iterator().next().getLong("id"));
	}

	/**
	 * @param rowSet
	 * @return
	 */
	private JsonObject funFruitUpdateAndDeleteId(PgRowSet rowSet) {
		return new JsonObject().put("isRowsAffected", rowSet.rowCount() == 1);
	}

	/**
	 * @param iterator
	 * @return
	 */
	private JsonObject funFruit(PgIterator iterator) {

		if (iterator.hasNext()) {

			final Row row = iterator.next();

			return new JsonObject().put("id", row.getLong("id")).put("name", row.getString("name"));

		} else {
			return new JsonObject().put("msg", "Id not found");
		}
	}

}
