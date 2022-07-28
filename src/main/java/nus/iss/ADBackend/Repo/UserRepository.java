package nus.iss.ADBackend.Repo;

import nus.iss.ADBackend.model.User;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByName(String name);

    User findById (int userId);

    @Transactional
    void deleteById(int userId);

}
