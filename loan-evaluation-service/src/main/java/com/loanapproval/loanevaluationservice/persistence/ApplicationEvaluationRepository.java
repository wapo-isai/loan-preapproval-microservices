package com.loanapproval.loanevaluationservice.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationEvaluationRepository extends CrudRepository<ApplicationEvaluation, Long> {

}
