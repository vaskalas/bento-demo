package job.bento.repository;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import job.bento.exception.PathToPhilosophyException;
import job.bento.exception.WikiDoesNotExistException;

public class WikiSourceClassPathImpl implements WikiSource {
	
	private Class clazz;
	
	public WikiSourceClassPathImpl(Class clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public Document getDocument(Element link) throws IOException, PathToPhilosophyException, URISyntaxException {
		return getDocument(link.attr("href"));
	}

	@Override
	public Document getDocument(String url) throws IOException, PathToPhilosophyException, URISyntaxException {
		InputStream in = this.clazz.getResourceAsStream(url);
		if(in == null){
			throw new WikiDoesNotExistException(url);
		}
		Document document = Jsoup.parse(in, null, "");
		return document;
	}

}
