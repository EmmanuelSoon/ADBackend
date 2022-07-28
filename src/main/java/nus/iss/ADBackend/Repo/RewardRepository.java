package nus.iss.ADBackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import nus.iss.ADBackend.model.Reward;

public interface RewardRepository extends JpaRepository<Reward, Integer>{
    
    Reward findByName(String name);
    
    void deleteById(Integer id);
}
