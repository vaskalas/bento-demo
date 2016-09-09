package job.bento.repository;

import java.io.IOException;
import java.net.URISyntaxException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import job.bento.exception.PathToPhilosophyException;


public interface WikiSource {
	
	public Document getDocument(Element link) throws IOException, PathToPhilosophyException, URISyntaxException;
	public Document getDocument(String url) throws IOException, PathToPhilosophyException, URISyntaxException;
	
}
