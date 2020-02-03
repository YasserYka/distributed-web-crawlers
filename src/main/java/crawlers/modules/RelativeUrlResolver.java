package crawlers.modules;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelativeUrlResolver {
	
	/*
	 * Stolen regex from stackoverflow url: https://stackoverflow.com/questions/24924072/website-url-validation-regex-in-java
	 * gropu(0) should return base url if matcher
	*/
	private static final Pattern BASEIT_PATTERN = Pattern.compile("((http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+)).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?");
	private static Matcher matcher;
	/*
	 * Three cases should be handled differently 
	 * Relative URL start with '/', start with "./", start with "../" or start with non of them (that should be handled same as "./" but appends a slash to it)
	 * "../" should be called recursively because it may contain more than one of those in relative URL
	*/
	public String resolve(String base, String relative) {
		String resolved = null, based;
		if(relative.startsWith("/")) {
			based = baseIt(base);
			if(based != null)
				resolved = baseIt(base) + relative;
		}
		return resolved;
	}
	
	//Takes full URL and delete all URI leaving the base URL
	//Example http://www.notMaliciousAtAll.com/page/id it should return http://www.notmalicious.com
	private String baseIt(String nonBase) {
		matcher = BASEIT_PATTERN.matcher(nonBase);
		return matcher.find() ? matcher.group(0) : null;
	}
	
	//Takes full URL and returns it without file-name in path
	//Example http://www.notMaliciousAtAll.com/page/fooling.html it should return http://www.notMaliciousAtAll.com/page/
	private String currentDirectory(String url) {
		int lengthOfUrl = url.length(), i, lastSlash = 0;
		char[] urlAsChars = url.toCharArray();
		
		for(i = 0; i< lengthOfUrl; i++)
			if(urlAsChars[i] == '/')
				lastSlash = i;
		
		return url.substring(0, lastSlash);
	}
	
	//Takes full URL and returns it by deleting file-name plus current directory
	//Example http://www.notMaliciousAtAll.com/page/anotherpage/fooling.html it should return http://www.notMaliciousAtAll.com/page/
	private String previousDirectory(String url) {
		int lengthOfUrl = url.length(), i, lastSlash = 0, beforeLastSlash = 0;
		char[] urlAsChars = url.toCharArray();
		
		for(i = 0; i< lengthOfUrl; i++)
			if(urlAsChars[i] == '/') {
				lastSlash = i;
				beforeLastSlash = lastSlash;
			}
		
		return url.substring(0, beforeLastSlash);
	}
	
	//Iterate through URLs and turn every relative to absolute URL
	public static void normalize(List<String> urls) {
		int lengthOfUrls = urls.size(), i;
		String url;
		
		for(i = 0; i < lengthOfUrls; i++) {
			//Resolve if need to be resolved
			//if resolve returns null drop URL
		}
	}}
