package nus.iss.ADBackend.Repo;

import java.util.List;

import javax.transaction.Transactional;

import nus.iss.ADBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nus.iss.ADBackend.model.UserReward;

public interface UserRewardRepository extends JpaRepository<UserReward, Integer> {
    
    List<UserReward> findByUserId(int userId);
    @Query("Select ur From UserReward ur Where ur.reward.id = ?1")
    List<UserReward> findByRewardId(int rewardId);
    @Query("Select ur.user From UserReward ur Where ur.reward.id = ?1")
    List<User> findUsersByRewardId(int rewardId);
    // @Modifying
    // @Query("Delete ur from UserReward ur where ur.user.id = ?1")
    @Transactional
    void deleteByUserId(int userId);
    @Transactional
    void deleteByRewardId(int id);
    @Query("select ur from UserReward ur where ur.user.id = ?1 and ur.reward.id = ?2")
    UserReward findByUserIdAndRewardId(int userId, int rewardId);
}
