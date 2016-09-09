package job.bento.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import job.bento.domain.Step;
import job.bento.exception.PathToPhilosophyException;
import job.bento.service.PathToPhilosophyService;
import job.bento.service.StepRepository;

@RestController
public class PhilosophyController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final PathToPhilosophyService pathToPhilosophyService;
	
	@Autowired
	public PhilosophyController(PathToPhilosophyService pathToPhilosophyService, StepRepository stepRepository) {
		super();
		this.pathToPhilosophyService = pathToPhilosophyService;
	}

	@RequestMapping("/philosophy")
	@JsonIgnore
    @JsonProperty(value = "ancestors")
    public Step path(@RequestParam("wikiPage") String wikiPage) throws IOException, PathToPhilosophyException, URISyntaxException {
		
		logger.info("Getting path to philosophy from " + wikiPage);
		Step step = pathToPhilosophyService.startPathToPhilosophy(wikiPage);
		return step;
    }

}
