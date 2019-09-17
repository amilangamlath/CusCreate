package com.mbsl.velocity.risk.auth;

import com.mbsl.velocity.dal.as400.AS400DBConnection;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
author- Amila Jayarathna
 */
@Controller
public class MLRepDataController {

    @RequestMapping(value = "/getRiskRepCat")
    @ResponseBody
    public List<String> getRiskRepCat(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rsRep = null;
        try {
            String queryRep = "SELECT DISTINCT REPORT_CAT FROM ML_REPORT where ENABLE='1'";
            rsRep = AS400DBConnection.search(queryRep);

            if (rsRep != null) {
                while (rsRep.next()) {
                    myList.add(rsRep.getString("REPORT_CAT"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MLRepDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rsRep != null) {
                    rsRep.getStatement().close();
                    rsRep.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return myList;
    }

    @RequestMapping(value = "/getRiskRepType/{repType}")
    @ResponseBody
    public List<String> getRiskRepType(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("repType") String repType) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        List<String> repNameList = new ArrayList<String>();
        ResultSet rsRepName = null;
        try {
            String queryRepName = "SELECT REPORT_NAME FROM ML_REPORT where REPORT_CAT = '" + repType + "' and ENABLE='1'";

            rsRepName = AS400DBConnection.search(queryRepName);

            if (rsRepName != null) {
                while (rsRepName.next()) {
                    repNameList.add(rsRepName.getString("REPORT_NAME"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MLRepDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rsRepName != null) {
                    rsRepName.getStatement().close();
                    rsRepName.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return repNameList;
    }

    @RequestMapping(value = "/get_branch_list")
    @ResponseBody
    public List<String> get_branch_list(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ParseException, IOException, Exception {

        List<String> BranchList = new ArrayList<>();
        String searchFrom = "select DISTINCT CFBRNM from CFP102";
        ResultSet brSearch = null;

        try {
            HttpSession session = httpServletRequest.getSession();
            List<String> libs = (List<String>) session.getAttribute("as400libs");
            AS400DBConnection.setLibraries(libs);

            brSearch = AS400DBConnection.search(searchFrom);

            while (brSearch.next()) {
                BranchList.add(brSearch.getString("CFBRNM"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (brSearch != null) {
                    brSearch.getStatement().close();
                    brSearch.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
            }

        }
        return BranchList;
    }

}
