package nus.iss.ADBackend.Repo;

import nus.iss.ADBackend.model.Goal;
import nus.iss.ADBackend.model.Role;
import nus.iss.ADBackend.model.User;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    //test only, the name may not be unique
    //please avoid to use in real
    User findByName(String name);
    User findById (int userId);
    @Modifying
    void deleteById(int userId);
    @Query("select u from User u where u.username = ?1 and u.password = ?2")
    User findByUsernameAndAndPassword(String username, String password);
    @Query("select u from User u where u.username = ?1")
    User findByUsername(String username);
    @Query("select u from User u where u.role = ?1")
    List<User> findAllByType(Role role);
    @Query("select u from User u where u.goal = ?1")
    List<User> findAllByGoal(Goal goal);

}
