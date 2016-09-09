package job.bento.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import job.bento.domain.Step;

public interface StepRepository extends CrudRepository<Step, Long> {

    Page<Step> findAll(Pageable pageable);
    Step findByUrlPath(String urlPath);
}
