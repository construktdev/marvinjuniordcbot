package de.construkter.marvinjuniorbot.modules.news;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public class RssHandler {

    @Nullable
    public static SyndEntry getNewestFeed() {
        try {
            URL feedUrl = new URL("https://newsfeed.kicker.de/team/dynamo-dresden");
            SyndFeedInput input = new SyndFeedInput();
            return input.build(new XmlReader(feedUrl)).getEntries().getFirst();
        } catch (Exception e) {
            System.out.println("Couldn't fetch newest feed");
            return null;
        }
    }
}
