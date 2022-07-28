package nus.iss.ADBackend.Repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nus.iss.ADBackend.model.UserReward;

public interface UserRewardRepository extends JpaRepository<UserReward, Integer> {
    
    List<UserReward> findByUserId(int userId);

    @Query("Select ur From UserReward ur Where ur.reward.id = ?1")
    List<UserReward> findByRewardId(int rewardId);

    
    // @Modifying
    // @Query("Delete ur from UserReward ur where ur.user.id = ?1")
    @Transactional
    void deleteByUserId(int userId);
}
