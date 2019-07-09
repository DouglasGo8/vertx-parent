package io.vertx.pagseguro.feed;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import lombok.NoArgsConstructor;
import org.apache.camel.Body;

/**
 *
 */
@NoArgsConstructor
public class ReleasePostFilter {

    /**
     * Accepts only release announce.
     */
    public boolean isRelease(@Body SyndFeed feed) {
        SyndEntry firstEntry = (SyndEntry) feed.getEntries().get(0);
        return firstEntry.getTitle().toLowerCase().contains("release");
    }
}
