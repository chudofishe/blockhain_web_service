package com.diplom.repositories;

import com.diplom.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RoleRepository extends MongoRepository<Role, String> {
    
    Role findByRole(String role);
    
}
