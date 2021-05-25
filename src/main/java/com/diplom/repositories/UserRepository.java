package com.diplom.repositories;

import com.diplom.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {
    
    User findByEmail(String email);
    
}
