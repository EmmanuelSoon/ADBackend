package nus.iss.ADBackend.Service;

import nus.iss.ADBackend.Repo.ReportRepository;
import nus.iss.ADBackend.model.Report;
import nus.iss.ADBackend.model.ReportStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    ReportRepository rpRepo;

    void createReport(Report report) {
        rpRepo.saveAndFlush(report);
    }
    @Transactional
    boolean saveReport(Report report) {
        if (rpRepo.findById(report.getId()) != null) {
            rpRepo.saveAndFlush(report);
            return true;
        }
        return false;
    }
    List<Report> findAllPendingReports() {
        return rpRepo.findReportByReportStatus(ReportStatus.SUBMITTED);
    }

    List<Report>findAllReportByUserId(int id) {
        return rpRepo.findReportByUserId(id);
    }

    boolean deleteReportById(int id) {
        if (rpRepo.findById(id) != null) {
            rpRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
