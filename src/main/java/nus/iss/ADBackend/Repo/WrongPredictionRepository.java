package nus.iss.ADBackend.Repo;

import java.util.List;

import javax.transaction.Transactional;

import nus.iss.ADBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nus.iss.ADBackend.model.WrongPrediction;

public interface WrongPredictionRepository extends JpaRepository<WrongPrediction, Integer> {

    
}
