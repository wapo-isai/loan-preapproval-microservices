package com.loanapproval.applicationservice.persistence;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LoanApplicationRepository extends MongoRepository<LoanApplication, String> {
    List<LoanApplication> findByUserId(String userId);
}
