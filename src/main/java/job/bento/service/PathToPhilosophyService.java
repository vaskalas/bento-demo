package job.bento.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import job.bento.domain.Step;
import job.bento.exception.CandidateLinkException;
import job.bento.exception.PathToPhilosophyException;

public interface PathToPhilosophyService {
	public Element getCandidateLink(Document document) throws CandidateLinkException;
	public List<Step> pathToPhilosophy(Document document, final int iteration, List<Step> path, Step parent) throws IOException, PathToPhilosophyException, URISyntaxException;
	public Step startPathToPhilosophy(String url) throws IOException, PathToPhilosophyException, URISyntaxException;
	
}
