package job.bento.service;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import job.bento.domain.Step;
import job.bento.exception.CandidateLinkException;
import job.bento.exception.CircularDepdendencyExeption;
import job.bento.exception.PathToPhilosophyException;
import job.bento.exception.RecursionLimitException;
import job.bento.repository.WikiSource;
import job.bento.utils.LinkUtils;

@Service
@Transactional
public class PathToPhilosophyServiceImpl implements PathToPhilosophyService{
	
	public static final int ITERATION_LIMIT = 100;
	private final WikiSource wikiSource;
	private final StepRepository stepRepository;
	
    public PathToPhilosophyServiceImpl(WikiSource wikiSource, StepRepository stepRepository){
    	this.wikiSource = wikiSource;
    	this.stepRepository = stepRepository;
    }
	
	public Element getCandidateLink(Document document) throws CandidateLinkException {
		
		String selector = new String("#mw-content-text p a");
		Elements links = document.select(selector);
		for(Element link : links){
			if(LinkUtils.isCandidateLink(link)){
				return link;
			}
		}
		
		throw new CandidateLinkException("Cannot find a link to follow on " + document.baseUri());
	}
	
	public List<Step> pathToPhilosophy(Document document, final int iteration, List<Step> path, Step parent) throws IOException, PathToPhilosophyException, URISyntaxException{
		
		if(iteration > PathToPhilosophyServiceImpl.ITERATION_LIMIT){
			String startUrl = path.get(0).getUrlPath();
			throw new RecursionLimitException("Cannot reach philosophy from " + startUrl + ".  "
					+ "Quitting after " + PathToPhilosophyServiceImpl.ITERATION_LIMIT + " iterations.");
		}
		
		Element candidateLink = getCandidateLink(document);
		
		if(LinkUtils.isPhilosophyLink(candidateLink)){
			return path;
		}
		
		Document nextPage = wikiSource.getDocument(candidateLink);
		Step jump = new Step();
		jump.setUrlPath(candidateLink.attr("href"));
		
		if(path.contains(jump)){
			String startUrl = path.get(0).getUrlPath();
			throw new CircularDepdendencyExeption("Cannot reach philosophy from "
					+ "" + startUrl + ".  " + jump.getUrlPath() + " causes an infinite loop");
		}
		
		path.add(jump);
		return pathToPhilosophy(nextPage, iteration+1, path, jump);
		
	}
	
	public Step startPathToPhilosophy(String url) throws IOException, PathToPhilosophyException, URISyntaxException{
		
		Document document = wikiSource.getDocument(url);
		URI uri = new URI(document.baseUri());
		url = uri.getPath();
		List<Step> path = new ArrayList<>();
		Step start = new Step();
		start.setUrlPath(url);
		path.add(start);
		
		List<Step> steps = pathToPhilosophy(document, path.size(), path, start);
		
		save(steps.iterator(), null);
		start = steps.iterator().next();
		start.setDistance(steps.size());
		
		return start;
	}
	
	public Step getOrCreateOrUpdate(Step step, Step parent){
		Step entity = stepRepository.findByUrlPath(step.getUrlPath());
		if(entity == null){
			entity = stepRepository.save(step);
		}
		if(parent != null){
			parent.setNext(entity);
			stepRepository.save(parent);
		}
		return entity;
	}
	
	public void save(Iterator<Step> iter, Step parent){
		if(iter.hasNext()){
			Step current = iter.next();
			current = getOrCreateOrUpdate(current, parent);
			save(iter, current);
		}
	}
	
	public WikiSource getWikiSource() {
		return wikiSource;
	}
	

   
}
