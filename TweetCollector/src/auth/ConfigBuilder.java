package auth;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class ConfigBuilder {
	private static Configuration config;
	
	private static final String API_KEY = "3ArGI0DG1Bg0k5T39W8yw";
	private static final String API_SECRET = "MWkMYLm50Dh7ZJM1t2zf2eOuKPx1ke9kjep62PHaok";
	private static final String ACCESS_TOKEN = "167761283-ZH6oxvFHXPP9vv7px1yaAGTl2QbQJw4HMGXnN1kg";
	private static final String ACCESS_SECRET = "Y7jw6ua8M7n1z0nShKKLbDvDnUuIjhgGNPcU5YIucJZ91";
	
	static {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(API_KEY);
		cb.setOAuthConsumerSecret(API_SECRET);
		cb.setOAuthAccessToken(ACCESS_TOKEN);
		cb.setOAuthAccessTokenSecret(ACCESS_SECRET);
		config = cb.build();
	}
	
	public static Configuration getConfig() {
		return config;
	}
}
