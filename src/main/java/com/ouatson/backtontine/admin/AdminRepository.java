package com.ouatson.backtontine.admin;

import com.ouatson.backtontine.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Admin findByEmail(String email);

    Admin findByConfirmationToken(String confirmToken);

}
