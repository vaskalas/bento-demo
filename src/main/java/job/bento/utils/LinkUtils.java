package job.bento.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.nodes.Element;

public class LinkUtils {
	
	public static boolean isPhilosophyLink(Element link) {
		// TODO Auto-generated method stub
		String path = link.attr("href");
		try {
			URL asUrl = new URL(path);
			path = asUrl.getPath();
			return path.equals("/wiki/Philosophy");
		} catch (MalformedURLException e) {
			// Do Nothing
			return path.equals("/wiki/Philosophy");
		}	
	}

	public static boolean isWikiHref(Element link) {
		// TODO Auto-generated method stub
		String href = link.attr("href");
		return href.startsWith("/wiki/");
	}

	public static boolean isLowerCaseLink(Element link) {
		// TODO Auto-generated method stub
		String innerHtml = link.html();
		Character firstLetter = innerHtml.charAt(0);
		return Character.isLowerCase(firstLetter);
	}
	
	public static boolean isCandidateLink(Element link){
		return isWikiHref(link) && isLowerCaseLink(link);
	}
}
