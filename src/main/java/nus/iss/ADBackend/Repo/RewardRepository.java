package nus.iss.ADBackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import nus.iss.ADBackend.model.Reward;
import org.springframework.data.jpa.repository.Modifying;

public interface RewardRepository extends JpaRepository<Reward, Integer>{
    
    Reward findByName(String name);
    Reward findById(int id);
    @Modifying
    void deleteById(int id);
}
