package nus.iss.ADBackend.Service;

import nus.iss.ADBackend.Repo.RewardRepository;
import nus.iss.ADBackend.Repo.UserRewardRepository;
import nus.iss.ADBackend.model.Report;
import nus.iss.ADBackend.model.Reward;
import nus.iss.ADBackend.model.User;
import nus.iss.ADBackend.model.UserReward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class RewardService {

    @Autowired
    RewardRepository rRepo;

    @Autowired
    UserRewardRepository urRepo;
    public void createReward(Reward reward) {
        rRepo.saveAndFlush(reward);
    }
    @Transactional
    public boolean saveReward(Reward reward) {
        if (rRepo.findById(reward.getId()) != null) {
            rRepo.saveAndFlush(reward);
            return true;
        }
        return false;
    }
    @Transactional
    public boolean deleteById(int id){
        if (rRepo.findById(id) != null) {
            rRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public Reward findById(int id) {
        return rRepo.findById(id);
    }
    public Reward findByName(String name) {
        return rRepo.findByName(name);
    }

    public List<UserReward> getRewardsByUserId(int userId) {
        return urRepo.findByUserId(userId);
    }

    public List<User> findByRewardId(int rewardId) {
        return urRepo.findUsersByRewardId(rewardId);
    }

    public void AddRewardToUser(User u, Reward reward) {
        if (urRepo.findByUserIdAndRewardId(u.getId(), reward.getId()) == null) {
            UserReward ur = new UserReward(u, reward);
            urRepo.saveAndFlush(ur);
        }
    }
}
