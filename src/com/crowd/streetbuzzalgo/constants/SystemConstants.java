/**
 * 
 */
package com.crowd.streetbuzzalgo.constants;

/**
 * @author Atrijit
 *
 */
public interface SystemConstants {

	//for Bing synonym API calls
	public static final String URIString = "https://api.datamarket.azure.com/Bing/Synonyms/GetSynonyms?Query=";
	public static final String account_key = "ag7BfZcgmWD3eVymbDQ8JSJSM2RTiRLnX4QIxDFY250";
	public static final String customerID = "5e2ec1e3-c766-476b-b739-0d63dd672cbd";
	
	//For Open Calais
	public static final String OPENCALAISSERVICEKEY = "kwrbxutramsxep4zdh6asemz";
	public static final String OPENCALAISURL = "http://api.opencalais.com/enlighten/rest/";
	
	//For Relations Parse API
	public static final String RPSERVICEKEY = "921236741c804a5efe6cf36c477053048ec1f568";
	public static final String RPRANKEDKEYWORDSURL = "http://access.alchemyapi.com/calls/text/TextGetRankedKeywords";
	public static final String RPRANKEDENTITIESURL = "http://access.alchemyapi.com/calls/text/TextGetRankedNamedEntities";
	public static final String RPRELATIONSURL = "http://access.alchemyapi.com/calls/text/TextGetRelations";
	public static final String RPTARGETEDSENTIMENTURL = "http://access.alchemyapi.com/calls/text/TextGetTargetedSentiment";
	public static final String RPSENTIMENTURL = "http://access.alchemyapi.com/calls/url/TextGetTextSentiment";
	
	//For different sites to be crawled with jaunt and parsed
	public static final String BLOGSPOT = "blogspot";
	public static final String WORDPRESS = "wordpress";
	public static final String TUMBLR = "tumblr";
	public static final String FLIPKART = "flipkart";
	public static final String GSMARENA = "gsmarena";
	
	public static final String CHICTOPIA = "chictopia";
	public static final String GIZMODO = "gizmodo";
	public static final String INDIATIMES = "indiatimes";
	public static final String PHONEARENA = "phonearena";
	public static final String SITEJABBER = "sitejabber";
	public static final String THEVERGE = "theverge";
	public static final String URBANSPOON = "urbanspoon";
	public static final String WIRED = "wired.co.uk";
	
	public static final String YELP = "yelp";
	public static final String ROTTENTOMATOES = "rottentomatoes";
	public static final String MOUTHSHUT = "mouthshut";
	public static final String CHOW = "chow";
	
	//Twitter
	public static final String TWITTER_KEY = "kDXOSUkqjnsCkMJvyZp1Q2mzT";
	public static final String TWITTER_SECRET = "kDu2F1NC26Q7vxQpbTIfGhKaWExr5KGcssr2cLNbt4fzlgq29g";
	public static final String TWITTER_ACCESS_TOKEN= "54838505-OzJYkYXXW4WeIcPeJPc5kwLWIxUbnGq0dDJLjIsB9";
	public static final String TWITTER_ACCESS_TOKEN_SECRET = "uVHij6NX8o6MXlBOahHegBbspCrUO5YNve8HaZ4keZ0I9";
	
	// Facebook
	public static final String FB_GRAPH_ACCESS_TOKEN = "CAACEdEose0cBAEUZA6r4Bb0MZCu6OPsjw8pCTMa9dmOrHj4ZAHgnFUsotSfs9FxLDVNDc9K03ZA3ZCV4C8wTHBfEkvvF7TuQBlZBEolVotUqJLHSNxtpUSavhhaG71vFMYG7BEKCslUz9ZA0YYI6Bd7Qchzpi1HSZBBgnPcrtmHCczwePoeQHZAKRHa5J0mmfbOyEKl5TwmAqRiFUplsG0akxZAkHh6WhASXwZD";
	
	//Youtube
	//public static final String YT_DEV_KEY = "AI39si4x04tL1jmMTC609bmAS26NorP-SDqQVmCrBOwKvIZFyu6g7lWlWbeqXmw2U9ijVI4m1vejItainkzWF1y24jRngtQk1Q";
	public static final String YT_DEV_KEY = "AIzaSyA_ql9m6i-KbtKrXrNbfscoFz4LTpovp9s";
	
	//Freebase
	public static final String FREEBASE_API_KEY = "AIzaSyA_ql9m6i-KbtKrXrNbfscoFz4LTpovp9s";
	public static final String FREEBASE_GENERIC_URL = "https://www.googleapis.com/freebase/v1/search";
	
	//Textalytics
	public static final String TEXTALYTICS_CORE_URL = "http://textalytics.com/core/class-1.1";
	public static final String TEXTALYTICS_SENTIMENT_URL = "http://textalytics.com/core/sentiment-1.2";
	public static final String TEXTALYTICS_TOPICS_URL = "http://textalytics.com/core/topics-1.2";
	public static final String TEXTALYTICS_KEY = "c9a2ac4537b2c9486169e2b7505d7228";
	
	//Mahout variables
	public static final String MAHOUT_SENTIMENT_URL = "http://textalytics.com/core/sentiment-1.2";
	public static final String MAHOUT_KEY = "c9a2ac4537b2c9486169e2b7505d7228";
	
	//WikiSynonyms API
	public static final String WIKI_SYNONYM_CORE_URL = "https://wikisynonyms.p.mashape.com/";
	public static String WIKI_HEADER_KEY = "X-Mashape-Key";
	public static String WIKI_HEADER_VALUE = "yeeavhp2ATmshMDOTRD51dHMRslPp1b82fojsnLb15PUMEcvEW";
	
	//Google Search
	//public static final String GOOGLE_SEARCH_URL = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start=<i>&rsz=large&quotaUser=<qu>&q=";
	//public static final String GOOGLE_SEARCH_URL = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start=<i>&rsz=large&userip=<ip>&q=";
	public static final String GOOGLE_SEARCH_URL = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start=<i>&rsz=large&q=";
	public static final String CHARSET = "UTF-8";
	
	//Google Custom Search
	public static final String GOOGLE_CUSTOM_SEARCH_KEY = "AIzaSyBcl7aSEvawcr0X2PBtNIDf9cVlYNgZzpo";
	
	//Faroo Search API
	public static final String FAROO_SEARCH_KEY = "8dDxu-Ctl4Jbp7qSS32oAgAkbNk_";
	//http://www.faroo.com/api?q=iphone&start=1&length=10&l=en&src=web&i=false&f=json
	public static final String FAROO_SEARCH_URL = "http://www.faroo.com/api?q=<q>&start=1&length=<i>&l=en&src=web&i=false&f=xml";
	
	//Sindice Search API
	public static final String SINDICE_SEARCH_URL = "http://api.sindice.com/v2/search?q=<q>&qt=term&page=1";
	
	//Google Suggest API
	public static final String GOOGLE_SUGGEST_URL = "http://suggestqueries.google.com/complete/search?output=toolbar&hl=en&q=";
	
	//Big Huge Thesaurus API
	public static final String BHT_KEY = "1366d8f27c935c951e2db95033d19040";
	public static final String BHT_URL = "http://words.bighugelabs.com/api/2/"+BHT_KEY+"/";
	
	//Google GCM Push Notification API key
	public static final String GCM_SERVER_KEY = "AIzaSyBcl7aSEvawcr0X2PBtNIDf9cVlYNgZzpo";
	public static final String MESSAGE_KEY = "message";
	public static final String APPNAME_KEY = "appName";
	public static final String DATA_KEY = "data";
	
	//Google Geocoding API
	public static final String GOOGLE_GEOCOING_API_KEY = "AIzaSyBcl7aSEvawcr0X2PBtNIDf9cVlYNgZzpo";
	public static final String GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/xml?";
	
	//Yahoo BOSS
	public static final String yahoo_consumer_key="dj0yJmk9UWRITVVyem1PZHJ0JmQ9WVdrOVRWWnZhMnBpTldFbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD1mOA--";
	public static final String yahoo_consumer_secret = "9056cc5085a0f53b659ae470d4133a983de5c388";
	//Yandex Search
	//public static final String YANDEX_SEARCH_URL = "http://xmlsearch.yandex.com/xmlsearch?user=atrijitdasgupta&key=03.299766443:e15fbe475c7a165d13216efe2161da48&l10n=en&filter=none";
	//Search URL changed as per mail from Yandex to atrijitdasgupta@gmail.com dated 13/05/2015
	public static final String YANDEX_SEARCH_URL = " https://yandex.com/search/xml?user=atrijitdasgupta&key=03.299766443:e15fbe475c7a165d13216efe2161da48&l10n=en&filter=none";
	public static final String YANDEX_SEARCH_REQUEST_XML = "<?xml version='1.0' encoding='utf-8'?><request><query>#QUERY#</query><groupings><groupby attr='d' mode='deep' groups-on-page='10' docs-in-group='10' /></groupings><page>0</page></request>";
	
	//Flickr
	public static final String FLICKR_KEY = "9df9ff35bdff9d496dba69927a95da1f";
	public static final String FLICKR_SECRET = "45c626fcae430aed";
	public static final String FLICKR_TOKEN = "45c626fcae430aed";
	
	//Datasift details
	public static final String DATASIFT_USERID = "atrijitdasgupta";
	public static final String DATASIFT_API_KEY = "779a48cb8702d44518291d103a5be794";
	
	//Vimeo Details
	public static final String VIMEO_CLIENT_IDENTIFIER = "0774ed6639215b9ac87fb4a0a1d9bd7077e74a3a";
	public static final String VIMEO_CLIENT_SECRET = "b4dba370a1282b3cf469d9c27bdbda17e45c27f4";
	public static final String VIMEO_ACCESS_TOKEN = "e1bc6521b8bd0e5b5b23ca8aa9956ca9";
	public static final String VIMEO_OAUTH_URL = "https://api.vimeo.com/oauth/authorize";
	public static final String VIMEO_ACCESS_TOKEN_URL = "https://api.vimeo.com/oauth/access_token";
	public static final String VIMEO_UNAUTH_AUTH_HEADER = "Authorization : Basic MDc3NGVkNjYzOTIxNWI5YWM4N2ZiNGEwYTFkOWJkNzA3N2U3NGEzYTpiNGRiYTM3MGExMjgyYjNjZjQ2OWQ5YzI3YmRiZGExN2U0NWMyN2Y0";
	
	//For Taxonomy Tree
	public static final boolean googlebool = true;
	public static final boolean otterbool = false;
	public static final boolean yandexbool = false;
	public static final boolean faroobool = false;
	public static final int maxsentencesize = 400;
	public static final int comparewordsize = 400;
	
	//Disqus
	public static final String appname = "buzzstreet";
	public static final String secretkey = "iW0OInCBSxJsIKNrqB5jot9dib2DplLkpx4cflw1xKuFMLLCmoK1yoAX7eQgrQLK";
	public static final String apikey =  "tDa0TFGCr0xTyNyIKnaQIfRZ4H1MEeK1zXkF41iqZfxrmkH4qnTrocatn9tAgrW8";
	
	//Tumblr
	public static final String tumblroauthconsumerkey = "nzCsi5xUw4O0UaYWcUAO9PjEci4EMkvPjGrbIIZ72BGXMrplxg";
	public static final String tumblrsecretkey = "0KpbmIKQhFNDLxVwYSCZ9ODgTHmXjfEKavj6PLdYD2MlAqIvs0";
	public static final String tumblrapiurl = "http://api.tumblr.com/v2/tagged?";
	
	//TypePad
	public static final String typepadapiurl = "http://api.typepad.com/assets";
	
	//Instagram
	public static final String instagramclientid = "24cdc13f514e4526a10667e60a2ad84e";
	public static final String instagramclientsecret = "c10d0c73cbba472da4bb28767f724177";
	public static final String instagramapiurl = "https://api.instagram.com/v1/tags/#SEARCH-TAG#/media/recent?";
	
	//Langauge Detection
	public static final String languagedetectionkey = "ab6a29927da2e30b729a70dc80cc6781";
	
	//Amazon Key
	/*User Name,Access Key Id,Secret Access Key
	"atrijitdasgupta",AKIAJXJY3ITQMMMVYA4Q,WzkylOj1aN0MMpsbmL4/8Y/9AbR8ZwXATbe8Kxcv
	"atrijit",AKIAIKREO5BUGCRNFVDA,eCAVfvFZsN6yvH7BnCn1UuongEIpCVbGHxqSumqJ
	"adasgupta",AKIAJ7HEEA7RYQGHCIFQ,kQbvqK27mkEphqvH9Ab+4jd85oo9YVtBq8Pg5j/y*/
	public static final String amazonkeyid = "AKIAJXJY3ITQMMMVYA4Q";
	public static final String amazonsecretaccesskey = "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";
	
	//Deduplicator word match threshold
	public static final double threshold = 0.99;
	
	//Stamplay
	public static final String STAMPLAYKEY = "c6ec75513c65b88f8cd5b8439614d86ece4b3f90a4f556ec58acf9ce44a78244";
	
	//webhose.io
	//public static final String WEBHOSEKEY = "010e19a4-d612-46aa-a730-e08f56418767";//ATRIJIT
	public static final String WEBHOSEKEY = "7ea102de-d375-404e-83f5-67035d225666";//SAUGATA
	
	
}
