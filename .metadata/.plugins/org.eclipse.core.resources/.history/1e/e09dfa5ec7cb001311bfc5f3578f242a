package code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import auth.ConfigBuilder;


public class Driver {
	
	private static final transient Logger LOG = LoggerFactory.getLogger(Driver.class);
	
	public static void main(String[] args)
	{
		LOG.info("Initializing...");
		LOG.debug("Creating Listener...");
		EnglishStatusListener listener = new EnglishStatusListener();
		LOG.debug("Creating Stream...");
	    TwitterStream twitterStream = new TwitterStreamFactory(ConfigBuilder.getConfig()).getInstance();
	    twitterStream.addListener(listener);
	    LOG.info("Initialization Complete.");
	    twitterStream.sample();
	}

}
