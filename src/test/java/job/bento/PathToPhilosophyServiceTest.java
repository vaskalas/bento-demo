package job.bento;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import job.bento.service.PathToPhilosophyServiceImpl;
import job.bento.service.StepRepository;
import job.bento.utils.LinkUtils;
import job.bento.domain.Step;
import job.bento.exception.CandidateLinkException;
import job.bento.exception.CircularDepdendencyExeption;
import job.bento.exception.PathToPhilosophyException;
import job.bento.exception.RecursionLimitException;
import job.bento.exception.WikiDoesNotExistException;
import job.bento.repository.WikiSource;
import job.bento.repository.WikiSourceClassPathImpl;
import job.bento.repository.WikiSourceHttpImpl;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PathToPhilosophyServiceTest {
    
	
    private PathToPhilosophyServiceImpl wikiHttpLib;
    private PathToPhilosophyServiceImpl wikiClassPathLib;
    @Autowired
    private StepRepository stepRepository;
    private final String wikiPhilosophyPage = "/wiki/Philosophy";
    private final String wikiQualityPhilosophyPage = "/wiki/Quality_(philosophy)";
    private final String wikiConsiousnessPage = "/wiki/Consciousness";
    private final String wikiNoCandidatePage = "/wiki/NoCandidateLink";
    
    @Before public void setUp(){
    	WikiSource wikiSourceClassPath = new WikiSourceClassPathImpl(this.getClass());
    	WikiSource wikiSourceHttp = new WikiSourceHttpImpl();
    	this.wikiHttpLib = new PathToPhilosophyServiceImpl(wikiSourceHttp, stepRepository);
    	this.wikiClassPathLib = new PathToPhilosophyServiceImpl(wikiSourceClassPath, stepRepository);
    }
    
    @Test public void testGetCandidateLink() throws Exception{
    	Document document = this.wikiClassPathLib.getWikiSource().getDocument(this.wikiPhilosophyPage);
		Element link = this.wikiClassPathLib.getCandidateLink(document);
		assertTrue(LinkUtils.isCandidateLink(link));
		Assert.assertFalse(LinkUtils.isPhilosophyLink(link));
    }
    
    @Test public void testGetPhilosophyLink() throws Exception{
    	Document document = this.wikiClassPathLib.getWikiSource().getDocument(this.wikiQualityPhilosophyPage);
		Element link = this.wikiClassPathLib.getCandidateLink(document);
		assertTrue(LinkUtils.isCandidateLink(link));
		assertTrue(LinkUtils.isPhilosophyLink(link));
    }
    
    @Test public void testWikiUrl() throws PathToPhilosophyException, IOException, URISyntaxException{
    	Step pathHttp = wikiHttpLib.startPathToPhilosophy(this.wikiConsiousnessPage);
    	Assert.assertEquals(pathHttp.getDistance(),2);
    }
    
    @Test public void testWikiClassPath() throws PathToPhilosophyException, IOException, URISyntaxException{
    	Step pathCP = wikiClassPathLib.startPathToPhilosophy(this.wikiConsiousnessPage);
    	Assert.assertEquals(pathCP.getDistance(),2);
    }
    
    @Test(expected=CandidateLinkException.class) 
    public void testNoCandidateLink() throws Exception{
    	Document document = this.wikiClassPathLib.getWikiSource().getDocument(this.wikiNoCandidatePage);
		this.wikiClassPathLib.getCandidateLink(document);
    }
    
    
    @Test(expected=RecursionLimitException.class)
    public void testRecursionLimit() throws IOException, PathToPhilosophyException, URISyntaxException{
    	int iteration = PathToPhilosophyServiceImpl.ITERATION_LIMIT + 1;
    	List<Step> pathToPhilosophy = new ArrayList<>();
    	Step step = new Step();
    	step.setUrlPath("junkPath");
    	pathToPhilosophy.add(step);
    	wikiHttpLib.pathToPhilosophy(null, iteration, pathToPhilosophy, null);
    }
    
    @Test(expected=CircularDepdendencyExeption.class)
    public void testCircularDependency() throws IOException, PathToPhilosophyException, URISyntaxException{
    	Step path = new Step();
    	path.setUrlPath("/wiki/Identity");
    	List<Step> pathToPhilosophy = new ArrayList<>();
    	pathToPhilosophy.add(path);
    	Assert.assertTrue(pathToPhilosophy.contains(path));
    	String html = "<html>"
    			+ "<head>"
    			+ "</head>"
    			+ "<body>"
    			+ "	<div id=\"mw-content-text\">"
    			+ "		<p>"
    			+ "			<a href=\"/wiki/Identity\">identity</a>"
    			+ "		</p>"
    			+ "</div>"
    			+ "</body>"
    			+ "</html>";
    	Document document = Jsoup.parse(html);
    	this.wikiHttpLib.pathToPhilosophy(document, 1, pathToPhilosophy, path);
    }
    
    @Test(expected=WikiDoesNotExistException.class)
    public void testClassPathWikiDoesNotExist() throws IOException, PathToPhilosophyException, URISyntaxException{
    	this.wikiClassPathLib.getWikiSource().getDocument("/wiki/does_not_exist");
    }
    
    @Test(expected=WikiDoesNotExistException.class)
    public void testHttpWikiDoesNotExist() throws IOException, PathToPhilosophyException, URISyntaxException{
    	this.wikiHttpLib.getWikiSource().getDocument("/wiki/does_not_exist");
    }

}
