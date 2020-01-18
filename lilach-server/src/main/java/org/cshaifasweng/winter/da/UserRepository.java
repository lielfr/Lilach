package org.cshaifasweng.winter.da;

import org.cshaifasweng.winter.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
