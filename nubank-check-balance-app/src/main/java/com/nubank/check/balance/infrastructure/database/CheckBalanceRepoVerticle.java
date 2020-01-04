package com.nubank.check.balance.infrastructure.database;

import com.nubank.check.balance.domain.Account;
import com.nubank.check.balance.domain.Customer;
import com.nubank.check.balance.domain.EnumBalanceEvent;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 *
 */
@NoArgsConstructor
public class CheckBalanceRepoVerticle extends AbstractVerticle {

    private List<Account> listOfAccount;
    private List<Customer> listOfCustomer;
    private Logger logger = LoggerFactory.getLogger(CheckBalanceRepoVerticle.class);

    /**
     * @throws Exception for Verticle
     */
    @Override
    public void start() throws Exception {

        logger.info("**** CheckBalanceRepoVerticle ready... ****");

        final String defaultSsn = "613-94-9711";
        final String defaultFName = "dbatista";
        final String defaultEmail = "dbatista@nubank.com";

        final String defaultAccountId = "0998872-1";
        final String defaultId = "30717a82-ab1b-11e9-a2a3-2a2ae2dbcce4";

        this.listOfCustomer = Collections.synchronizedList(Collections.singletonList(new
                Customer(defaultSsn, defaultEmail, defaultId, defaultFName)));
        this.listOfAccount = Collections.synchronizedList(Collections.singletonList(new
                Account(defaultAccountId, defaultId, 0D)));

        super.vertx.eventBus().consumer(EnumBalanceEvent.BALANCE_EVENT.toString(), this::dispatchMessage);
    }

    /**
     * @param message ofBus
     */
    private void dispatchMessage(final Message<JsonObject> message) {

        final String operation = message.body().getString("operation");

        switch (operation.toLowerCase()) {
            case "greeting":
                this.ofGreetingsByCustomerId(message);
                break;
            case "status":
                this.ofStatusCheckBalance(message);
                break;
            case "withdraw":
                this.ofBalanceWithdraw(message);
                break;
            case "add":
                this.ofAddCreditTransaction(message);
                break;
            default:
                throw new IllegalArgumentException("Invalid Operation");
        }
    }


    /**
     * @param message of Customer
     */
    private void ofGreetingsByCustomerId(Message<JsonObject> message) {

        final String defCustomerFName = message.body().getString("fName");

        final Customer defCustomer = this.listOfCustomer
                .stream()
                .filter(this.isDefaultCustomer(defCustomerFName))
                .findFirst()
                .orElse(Customer.toJsonUnknownUser());

        message.reply(Customer.toJson(defCustomer));
    }


    /**
     * @param message of Balance
     */
    private void ofStatusCheckBalance(Message<JsonObject> message) {

        final String id = message.body().getString("id");
        final String acc = message.body().getString("acc");

        final Account account = this.listOfAccount
                .stream()
                .filter(this.isCustomerAndAccountById(id, acc))
                .findFirst()
                .orElse(Account.unknownAccount());

        message.reply(new JsonObject().put("currentBalance", String.format("%.2f", account.getBalance())));
    }

    /**
     * @param message of Balance
     */
    private void ofBalanceWithdraw(Message<JsonObject> message) {

        final String id = message.body().getString("id");
        final String acc = message.body().getString("acc");
        final double debit = message.body().getDouble("debit");

        final Optional<Account> account = this.listOfAccount
                .stream()
                .filter(this.isCustomerAndAccountById(id, acc))
                .findFirst();

        if (!account.isPresent()) {
            message.reply(new JsonObject().put("message", "Invalid Customer or Account"));
        } else {
            final double currentBalance = account.get().getBalance();
            if (currentBalance < debit) {
                message.reply(new JsonObject().put("message", "Operation now allowed - Insufficient Balance"));
            } else {
                final double updateBalance = currentBalance - debit;
                account.get().setBalance(updateBalance);
                message.reply(new JsonObject().put("message",
                        String.format("Got it %.2f of total %.2f", debit, updateBalance)));
            }
        }
    }

    /**
     * @param message
     */
    private void ofAddCreditTransaction(Message<JsonObject> message) {

        final String id = message.body().getString("id");
        final String acc = message.body().getString("acc");
        final double credit = message.body().getDouble("credit");

        final Optional<Account> account = this.listOfAccount
                .stream()
                .filter(this.isCustomerAndAccountById(id, acc))
                .findFirst();

        if (!account.isPresent()) {
            message.reply(new JsonObject().put("message", "Invalid Customer or Account"));
        } else {

            final Account fAccount = account.get();

            final double thenBalance = fAccount.getBalance();
            final double nowBalance = thenBalance + credit;

            fAccount.setBalance(nowBalance);

            message.reply(new JsonObject().put("message",
                    String.format("Got your previous Balance was %.2f now is %.2f", thenBalance, nowBalance)));

        }

    }

    /**
     * @param defCustomer Customer
     * @return Customer filtered
     */
    private Predicate<Customer> isDefaultCustomer(String defCustomer) {
        return (c) -> c.getCustomerName().equals(defCustomer);
    }

    /**
     * @param id  of Customer
     * @param acc of Customer
     * @return Customer and Account Filtered
     */
    private Predicate<Account> isCustomerAndAccountById(String id, String acc) {
        return (account) -> ((account.getCustomerId().equals(id)) &&
                (account.getAccountId().equals(acc)));
    }


}
