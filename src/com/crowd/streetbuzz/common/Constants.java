/**
 * 
 */
package com.crowd.streetbuzz.common;

/**
 * @author Atrijit
 *
 */
public interface Constants {
public static final String INPUTPAGE = "pages/entry.jsp?p=";
public static final String FIRSTPROCESSRESULTPAGE = "pages/firstprocessresult.jsp?p=";
public static final String SECONDPROCESSRESULTPAGE = "pages/secondprocessresult.jsp?p=";
public static final String THIRDPROCESSRESULTPAGE = "pages/thirdprocessresult.jsp?p=";

public static final String UPDATEGRAPHAPITOKENPAGE = "pages/graphtoken.jsp?p=";

//Different Search sources
//Card types
public static final String FACEBOOK = "facebook";
public static final String GOOGLEPLUS = "googleplus";
public static final String TWITTER = "twitter";
public static final String YOUTUBE = "youtube";
public static final String GOOGLE = "google";
public static final String BLOG = "blog";
public static final String WEB = "web";
public static final String STREETBUZZ = "streetbuzz";

//Base external library path
public static final String BASEEXTLIBPATH = "/home/jboss-onlineadms/server/default/";
//public static final String BASEEXTLIBPATH = "C:/SubEtha-1.0.2/jboss/server/default/";

//Base streetbuzz storage path
//blic static final String BASESBSTORAGEPATH = "C:/Mywork/StreetBuzz/boom/";
public static final String BASESBSTORAGEPATH = "/home/streetbuzz/";

public static final String fbappId = "1536747489904371";
public static final String fbappSecret = "120c0e3b801b58f3eb7a112713759e43";

public static final String LOCATION = "LOCATION";
public static final String TIME = "TIME";
public static final String PERSON = "PERSON";
public static final String ORGANIZATION = "ORGANIZATION";
public static final String MONEY = "MONEY";
public static final String PERCENT = "PERCENT";
public static final String DATE = "DATE";

//Date formats
public static final String dateformat = "dd-MM-yyyy hh:mm";
public static final String dateformat2 = "dd-MM-yyyy";
public static final String yandexdateformat = "yyyyMMdd";
public static final String tumblrdateformat = "yyyy-MM-dd hh:mm:ss";
public static final String webhosedateformat = "yyyy-MM-dd";

//Incoming JSON parameters
public static final String JSONHEADERPARAM = "jsonheader";
public static final String JSONBODYPARAM = "jsonbody";

//Forward path addendum
public static final String FORWARDDDENDUM = "./";

//Standard Error response
public static final String STANDARERRORRESPONSE = "500 ERROR: ";
public static final String STANDARSUCCESSESPONSE = "200 OK";
public static final String SUCCESSESPONSEWITHID = "200 ";
public static final String SUCCESSESPONSE = "200";
//Card types
public static final String SEARCHTYPE = "S";
public static final String CONVERSATIONTYPE = "C";

public static final String ACTIONINTEREST = "I";
public static final String ACTIONINTERESTPROCESSING = "IP";
public static final String ACTIONYES = "Y";
public static final String ACTIONNO = "N";
public static final String UNDERPROCESSING = "U";

public static final String UNDERREFRESHPROCESSING = "RU";

public static final String ACTIONTYPENEEW = "NEW";
public static final String ACTIONTYPEEDIT = "EDIT";
public static final String ACTIONTYPEREFRESH = "REFRESH";

//Media file types
public static final String IMAGE = "IMAGE";
public static final String VIDEO = "VIDEO";

// Reverse Geo Location city-country constants
public static final String CITY = "city";
public static final String COUNTRY = "country";
// tag to locate full adress in reverse geo location lookup result
public static final String REVGEOSTARTTAG = "<formatted_address>";
public static final String REVGEOENDTAG = "</formatted_address>";

//Channels
public static final String WEBSITECHANNEL = "website";
public static final String BLOGCHANNEL = "blog";
public static final String NETWORKCHANNEL = "network";
public static final String VIDEOCHANNEL = "video";
public static final String STREETBUZZCHANNEL = "streetbuzz";

//List of user activities
public static final String ACTION_POST = "POST";

public static final String ACTION_SHARE_CARD = "SHARECARD";
public static final String ACTION_SHARE_VOICE = "SHAREVOICE";

public static final String ACTION_LIKE = "LIKE";
public static final String ACTION_VOTEUP = "VOTEUP";
public static final String ACTION_VOTEDOWN = "VOTEDOWN";
public static final String ACTION_COMMENT = "COMMENT";
public static final String ACTION_BOOKMARK = "BOOKMARK";
public static final String ACTION_REPORT = "REPORT";
public static final String ACTION_BLOCK = "BLOCK";

public static final String ACTION_GET_SHARED_CARD = "GET_SHARED_CARD";
public static final String ACTION_GET_SHARED_VOICE = "GET_SHARED_VOICE";

public static final String ACTION_GET_BOOKMARKED = "GET_BOOKMARKED";
public static final String ACTION_GET_COMMENTED = "GET_COMMENTED";
public static final String ACTION_GET_LIKE = "GET_LIKE";
public static final String ACTION_GET_VOTE_UP = "GET_VOTE_UP";
public static final String ACTION_GET_VOTE_DOWN = "GET_VOTE_DOWN";
public static final String ACTION_GET_REPORTED = "GET_REPORTED";
public static final String ACTION_GET_BLOCKED = "GET_BLOCKED";

//Score per user activity
public static final int ACTION_POST_VALUE = 2;

public static final int ACTION_SHARE_CARD_VALUE = 2;
public static final int ACTION_SHARE_VOICE_VALUE = 2;

public static final int ACTION_LIKE_VALUE = 2;
public static final int ACTION_VOTEUP_VALUE = 2;
public static final int ACTION_VOTEDOWN_VALUE = 2;
public static final int ACTION_COMMENT_VALUE = 2;
public static final int ACTION_BOOKMARK_VALUE = 2;
public static final int ACTION_REPORT_VALUE = 0;
public static final int ACTION_BLOCK_VALUE = 0;

public static final int ACTION_GET_SHARED_CARD_VALUE = 2;
public static final int ACTION_GET_SHARED_VOICE_VALUE = 2;

public static final int ACTION_GET_BOOKMARKED_VALUE = 2;
public static final int ACTION_GET_COMMENTED_VALUE = 1;
public static final int ACTION_GET_LIKE_VALUE = 1;
public static final int ACTION_GET_VOTE_UP_VALUE = 1;
public static final int ACTION_GET_VOTE_DOWN_VALUE = -1;
public static final int ACTION_GET_REPORTED_VALUE = -1;
public static final int ACTION_GET_BLOCKED_VALUE = -1;

//Beginning score
public static final int BEGINNING_REPUTATION_SCORE = 100;
public static final int REPUTATION_BRIDGE_SCORE_ONE = 1000;
public static final int REPUTATION_BRIDGE_SCORE_TWO = 5000;

//Different Channels
public static final String BLOGSPOTC = "blogspot";
public static final String WORDPRESSC = "wordpress";
public static final String BLOGGERC = "blogger";
public static final String GOOGLESEARCHC = "google";
public static final String TUMBLRC = "tumblr";
public static final String TYPEPADC = "typepad";
public static final String PINTERESTC = "pinterest";
public static final String INSTAGRAMC = "instagram";
public static final String VKC = "vk";
public static final String FLICKRC = "flickr";
public static final String VINEC = "vine";
public static final String AMAZONC = "amazon";
public static final String EPINIONSC = "epinions";
public static final String TRIPADVISORC = "tripadvisor";
public static final String YELPC = "yelp";
public static final String MOUTHSHUTC = "mouthshut";
public static final String EBAYC = "ebay";
public static final String WALMARTC = "walmart";
public static final String BESTBUYC = "bestbuy";
public static final String HOMEDEPOTC = "homedepot";
public static final String SEARSC = "sears";
public static final String QUORAC = "quora";

//Exclusions in Google Search results
public static final String YOUTUBEEXC = "youtube";
public static final String WIKIPEDIAEXC = "wikipedia";
public static final String PDFEXC = ".pdf";
public static final boolean showwithoutcomments = false;

//Uploaded image URL prefix
public static final String uploadedimageurlprefix = "http://203.123.190.50/streetbuzz/";

//Map key values for positive / negative word lists
public static final String positivemaptag = "positive";
public static final String negativemaptag = "negative";

//Switches for turning faroo and Yandex searches on and off
public static final boolean RUNFAROO = true;
public static final boolean RUNYANDEX = true;

//type for disambiguation push
public static final String disambiguationtype = "INTERESTSELECTION";

//Card share url 
public static final String CARDSHAREURL = "http://203.123.190.50/streetbuzz/cardshare.htm?id=";
public static final String CARDSHAREPAGE = "pages/cardpreview.jsp?p=";
public static final String UNDERCONSTRUCTIONPAGE = "pages/underconstruction.jsp?p=";

//Twitter URL
public static final String TWITTERURL = "https://twitter.com/statuses/";

//Sentiment parallel processing types
public static final String WEBSPP = "web";
public static final String TWITTERSPP = "twitter";
public static final String OTTERSPP = "otter";
public static final String YOUTUBESPP = "youtube";
public static final String TUMBLRSPP = "tumblr";
public static final String INSTAGRAMSPP = "instagram";

//Lingpipe
public static final String TRAINPATH = "lingpipe/train/";
public static final String INTERESTTRAINPATH = "interesttrain/";
public static final String CLASSIFIERPATH = "lingpipe/classifier";
public static final String CLASSIFIERFILENAME = "interest.lingPipe";
public static final String INTERESTCLASSIFIERPATH = "interestclassifier/";
public static final String POSITIVELING = "pos";
public static final String NEGATIVELING = "neg";
public static final String NEUTRALLING = "neu";
public static final String BASELINGPIPEPATH = "C:/Mywork/StreetBuzz/Lingpipe/";

//uClassify
public static final String uClassifyReadKey = "95KY8EyGWyZq";
public static final String uClassifyWriteKey = "CLEMcnj3yxE1";

//Channels
//Reviews, Forums, Blogs, Facebook, Tweets, Articles, Videos, Images.
public static final String REVIEWCH = "Reviews";
public static final String FORUMCH = "Forums";
public static final String BLOGCH = "Blogs";
public static final String FACEBOOKCH = "Facebook";
public static final String TWEETCH = "Tweets";
public static final String ARTICLECH = "Articles";
public static final String VIDEOCH = "Videos";
public static final String IMAGECH = "Images";

//PERPAGE value
public static final int PERPAGECARDS = 4;
public static final int PERPAGEVOICES = 8;

//Card up down vote types
public static final String UPVOTE = "UP";
public static final String DOWNVOTE = "DOWN";

//Graph share URLs
/*public static final String BARCHARTSHAREURL = "http://203.123.190.50/streetbuzz/graphshare.htm?type=bar&cardid=";
public static final String LINEMAPSHAREURL = "http://203.123.190.50/streetbuzz/graphshare.htm?type=line&cardid=";
public static final String WORDCLOUDSHAREURL = "http://203.123.190.50/streetbuzz/graphshare.htm?type=word&cardid=";*/

public static final String BARCHARTSHAREURL = "http://203.123.190.50/streetbuzz/cardshare.htm?id=";
public static final String LINEMAPSHAREURL = "http://203.123.190.50/streetbuzz/cardshare.htm?id=";
public static final String WORDCLOUDSHAREURL = "http://203.123.190.50/streetbuzz/cardshare.htm?id=";








}
