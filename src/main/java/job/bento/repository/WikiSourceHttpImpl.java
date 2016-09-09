package job.bento.repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Repository;

import job.bento.exception.PathToPhilosophyException;
import job.bento.exception.WikiDoesNotExistException;

@Repository
public class WikiSourceHttpImpl implements WikiSource{
	
	private String baseUrl;
	
	public WikiSourceHttpImpl(){
		String baseUrl = "https://en.wikipedia.org";
		System.out.println("base url "+baseUrl);
		this.baseUrl = baseUrl;
	}
	
	@Override
	public Document getDocument(Element link) throws IOException, PathToPhilosophyException, URISyntaxException {
		String wikiUrl = buildURI(link.attr("href"));
		return getDocument(wikiUrl);
	}

	@Override
	public Document getDocument(String url) throws IOException, PathToPhilosophyException, URISyntaxException {
		// TODO Auto-generated method stub
		String absoluteUrl = buildURI(url);
		try{
			Document document = Jsoup.connect(absoluteUrl).get();
			return document;
		}catch(HttpStatusException httpException){
			if(httpException.getStatusCode() == 404){
				throw new WikiDoesNotExistException(url + " is not a valid wiki page");
			}else{
				throw new PathToPhilosophyException(url);
			}
		}
	}
	
	public String buildURI(String url) throws PathToPhilosophyException, URISyntaxException{
		
		if(url.startsWith(this.baseUrl)){
			return url;
		}else if(url.startsWith("/wiki/")){
			URI uri = new URIBuilder()
					.setScheme("https")
					.setHost("en.wikipedia.org")
					.setPath(url)
					.build();
			return uri.toString();
		}else{
			URI uri = new URIBuilder()
					.setScheme("https")
					.setHost("en.wikipedia.org")
					.setPath("/wiki/" + url)
					.build();
			return uri.toString();
		}
	}

}
