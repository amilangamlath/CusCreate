package com.mbsl.velocity.risk.auth;

import com.mbsl.velocity.dal.as400.AS400DBConnection;
import com.mbsl.velocity.risk.ml.RepdataDTO;
import com.mbsl.velocity.risk.ml.cus;
import com.mbsl.velocity.risk.utill.AS400Date;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/*
author- Amila Jayarathna
 */
@Controller
public class MLProcessController {

    @RequestMapping(value = "/sendCusRisk/{customerNo}", method = RequestMethod.POST)
    @ResponseBody
    public int postSubmit(@RequestBody cus dto, HttpServletRequest req, @PathVariable("customerNo") List<String> customerNo) throws ParseException, Exception {

        HttpSession session = req.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        List<String> cusno = customerNo;
        String name = dto.getName();
        String nature = dto.getNature_itemSelected();
        String location = dto.getLoc_itemSelected();
        String purpose = dto.getPurpose_itemSelected();
        String source = dto.getSource_itemSelected();
        String vol = dto.getVol_itemSelected();
        String individual = dto.getIndividual();
        String suspicious = dto.getSuspicious();
        String terrorist = dto.getTerrorist();
        String cusType = dto.getType_itemSelected();
        String category = dto.getCategory();
        String branch = dto.getBranch();
        String nicNo = dto.getCusNic();
        //LocalDate myTimeObj = LocalDate.now();
        String user = (String) session.getAttribute("user");

        AS400Date aS400Date = new AS400Date(libs);
        String assDate = aS400Date.getCurrentDateStr();

        //System.out.println("Ass400 Date========" + assDate);
        int response = 0;

        String query_ml_cus_risk_record = "";

        // String upRisklastDateCol = "UPDATE ML_CUS_RISK SET ML_CUS_RISK.changed_date = '" + myTimeObj.toString() + "' WHERE id=(SELECT max(id) FROM ML_CUS_RISK where cusno = '" + cusno + "')";
        MLRiskEvaluation riskOb = new MLRiskEvaluation();

        int finalRiskScore = riskOb.riskEvaluation(category, individual, suspicious, terrorist, cusType, nature, source, vol, location, purpose, req);

        String finalRiskLevel = "";
        ResultSet riskLevelQuery = null;
        ResultSet rsRiskCusSearch = null;

        String finalRiskLevelQuery = "select FINAL_RISK_LEVEL from ML_RISK_RANGE where FROM_VAL <= '" + finalRiskScore + "' and TO_VAL >= '" + finalRiskScore + "'";

        try {
            riskLevelQuery = AS400DBConnection.search(finalRiskLevelQuery);
            if (riskLevelQuery.next()) {
                finalRiskLevel = riskLevelQuery.getString("FINAL_RISK_LEVEL");
                response = response + 1;
            }

            for (String cusnoSelected : cusno) {
                String uplastDateCol = "UPDATE ML_RISK_RECORD SET changed_date = '" + assDate + "',CHANGED_USR = '" + user + "' WHERE id=(SELECT max(id) FROM ML_RISK_RECORD where cusno = '" + cusnoSelected + "')";

                AS400DBConnection.changeData(uplastDateCol);

                query_ml_cus_risk_record = "INSERT INTO ML_RISK_RECORD (CUSNO, CATEGORY, TERRORIST, RATED_DATE, CHANGED_DATE, NAME,"
                        + " INDIVIDUAL, BRANCH_NAME, RISK_SCORE, RISK_LEVEL, NICNO, RATED_USR, SUSPICIOUS,TYPE_ITEMSELECTED,NATURE_ITEMSELECTED,SOURCE_ITEMSELECTED,VOL_ITEMSELECTED,LOC_ITEMSELECTED,PURPOSE_ITEMSELECTED,CHANGED_USR) VALUES "
                        + "('" + cusnoSelected + "', '" + category + "', '" + terrorist + "', '" + assDate + "', '" + assDate + "'"
                        + ", '" + name + "', '" + individual + "', '" + branch + "', '" + finalRiskScore + "', '" + finalRiskLevel + "', '" + nicNo + "', '" + user + "'"
                        + ", '" + suspicious + "', '" + cusType + "', '" + nature + "', '" + source + "', '" + vol + "', '" + location + "', '" + purpose + "', '" + user + "')";

                response = response + AS400DBConnection.changeData(query_ml_cus_risk_record);

                String query_ml_cus_risk = "";

                String query_search_cus_risk = "select cusno from ml_cus_risk where cusno = '" + cusnoSelected + "'";

                String query_up_cus_risk = "update ml_cus_risk set CATEGORY = '" + category + "',SUSPICIOUS = '" + suspicious + "',TERRORIST = '" + terrorist + "',"
                        + "TYPE_ITEMSELECTED = '" + cusType + "',NATURE_ITEMSELECTED = '" + nature + "',SOURCE_ITEMSELECTED = '" + source + "',VOL_ITEMSELECTED = '" + vol + "',"
                        + "LOC_ITEMSELECTED = '" + location + "',PURPOSE_ITEMSELECTED = '" + purpose + "',risk_score = '" + finalRiskScore + "' , risk_level = '" + finalRiskLevel + "' "
                        + ",changed_date = '" + assDate + "' , changed_usr = '" + user + "' where cusno = '" + cusnoSelected + "'";

                rsRiskCusSearch = AS400DBConnection.search(query_search_cus_risk);
                if (rsRiskCusSearch.next()) {
                    response = response + AS400DBConnection.changeData(query_up_cus_risk);
                } else {
                    query_ml_cus_risk = "INSERT INTO ml_cus_risk (CUSNO,RISK_SCORE,RISK_LEVEL,RATED_DATE,CHANGED_DATE,RATED_USR,CHANGED_USR,NAME,INDIVIDUAL,BRANCH_NAME,"
                            + "NICNO,CATEGORY,TERRORIST,SUSPICIOUS,TYPE_ITEMSELECTED,NATURE_ITEMSELECTED,SOURCE_ITEMSELECTED,VOL_ITEMSELECTED,LOC_ITEMSELECTED,PURPOSE_ITEMSELECTED)"
                            + " VALUES ('" + cusnoSelected + "', '" + finalRiskScore + "', '" + finalRiskLevel + "', '" + assDate + "', '" + assDate + "', '" + user + "', '" + user + "',"
                            + " '" + name + "', '" + individual + "', '" + branch + "', '" + nicNo + "', '" + category + "', '" + terrorist + "', '" + suspicious + "', '" + cusType + "', '" + nature + "', '" + source + "',"
                            + " '" + vol + "', '" + location + "', '" + purpose + "')";

                    response = response + AS400DBConnection.changeData(query_ml_cus_risk);
                    //   con.close();
                }
            }
        } catch (Exception e) {
            Logger.getLogger(MLProcessController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (riskLevelQuery != null) {
                    riskLevelQuery.getStatement().close();
                    riskLevelQuery.close();
                }
                if (rsRiskCusSearch != null) {
                    rsRiskCusSearch.getStatement().close();
                    rsRiskCusSearch.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @RequestMapping(value = "/allHighRisk/{br}/{fTime}/{tTime}/{repType}/{cusType}")
    @ResponseBody
    public List<RepdataDTO> allHighRisk(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("br") String branch,
            @PathVariable("fTime") String fTime, @PathVariable("tTime") String tTime, @PathVariable("repType") String repType, @PathVariable("cusType") String cusType) throws ParseException, Exception {

        // SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        // Date fT = format.parse(fTime);
        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        List<RepdataDTO> myList = new ArrayList<>();
        String RiskRepQuery = "";
        ResultSet rs = null;

        if (cusType.equals("All")) {
            if (branch.equals("All")) {
                if (repType.equals("High Risk Customers' Report")) {
                    RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.RISK_SCORE,RS.NAME,RS.NICNO,RS.SUSPICIOUS,RS.BRANCH_NAME,RS.CATEGORY,RS.TERRORIST,"
                            + "RS.RATED_DATE,RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED,"
                            + "RS.CHANGED_DATE,RS.RATED_USR,RS.CHANGED_USR FROM ML_CUS_RISK RS where RS.RISK_LEVEL = 'High' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "'";
                } else if (repType.equals("Medium Risk Customers' Report")) {
                    RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.SUSPICIOUS,RS.RISK_SCORE,RS.NAME,RS.BRANCH_NAME,RS.CATEGORY,RS.RATED_DATE,RS.TERRORIST,"
                            + "RS.NICNO,RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED,"
                            + "RS.CHANGED_DATE,RS.RATED_USR,RS.CHANGED_USR FROM ML_CUS_RISK RS where RS.RISK_LEVEL = 'Medium' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "'";
                } else if (repType.equals("Low Risk Customers' Report")) {
                    RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.SUSPICIOUS,RS.RISK_SCORE,RS.NAME,RS.BRANCH_NAME,RS.CATEGORY,RS.RATED_DATE,RS.TERRORIST,"
                            + "RS.NICNO,RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED,"
                            + "RS.CHANGED_DATE,RS.RATED_USR,RS.CHANGED_USR FROM ML_CUS_RISK RS where RS.RISK_LEVEL = 'Low' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "'";
                } else if (repType.equals("PEP Report")) {
                    RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.RISK_SCORE,RS.NAME,RS.BRANCH_NAME,RS.CATEGORY,RS.RATED_DATE,RS.TERRORIST,"
                            + "RS.NICNO,RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,"
                            + "RS.TYPE_ITEMSELECTED,RS.CHANGED_DATE,RS.CATEGORY,RS.RATED_USR,RS.CHANGED_USR FROM ML_CUS_RISK RS where RS.CATEGORY = 'PEP' and  RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "'";
                } else if (repType.equals("Suspicious Activities Report")) {
                    RiskRepQuery = "select distinct RS.CUSNO,RS.PURPOSE_ITEMSELECTED,RS.RISK_LEVEL,RS.RISK_SCORE,RS.NAME,RS.BRANCH_NAME,RS.CATEGORY,RS.RATED_DATE,RS.TERRORIST,"
                            + "RS.NICNO,RS.LOC_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED,RS.CHANGED_DATE,RS.SUSPICIOUS,"
                            + "RS.RATED_USR,RS.CHANGED_USR FROM ML_CUS_RISK RS  where RS.SUSPICIOUS != '44' and  RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "'";
                } else if (repType.equals("Suspected Terrorist Report")) {
                    RiskRepQuery = "select distinct RS.CUSNO,RS.PURPOSE_ITEMSELECTED,RS.RISK_LEVEL,RS.RISK_SCORE,RS.NAME,RS.BRANCH_NAME,RS.RATED_DATE,RS.TERRORIST,"
                            + "RS.CATEGORY,RS.LOC_ITEMSELECTED,RS.TYPE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.NICNO,RS.NATURE_ITEMSELECTED,RS.CHANGED_DATE,"
                            + "RS.TERRORIST,RS.RATED_USR,RS.CHANGED_USR FROM ML_CUS_RISK RS where RS.TERRORIST = 'YES' and  RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "'";
                } else if (repType.equals("All Risk Type Report")) {
                    RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.BRANCH_NAME,RS.RISK_SCORE,RS.TERRORIST,RS.NAME,RS.NICNO,RS.BRANCH_NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.CHANGED_DATE,RS.RATED_DATE,RS.RATED_USR,RS.CHANGED_USR,"
                            + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED FROM ML_CUS_RISK RS"
                            + " where RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "'";
                }
            } else {
                if (repType.equals("High Risk Customers' Report")) {
                    RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.BRANCH_NAME,RS.RISK_SCORE,RS.TERRORIST,RS.NAME,RS.NICNO,RS.BRANCH_NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.CHANGED_DATE,RS.RATED_DATE,RS.RATED_USR,RS.CHANGED_USR,"
                            + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED FROM ML_CUS_RISK RS"
                            + " where RS.RISK_LEVEL = 'High' and RS.BRANCH_NAME = '" + branch + "' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "'";
                } else if (repType.equals("Medium Risk Customers' Report")) {
                    RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.BRANCH_NAME,RS.TERRORIST,RS.RISK_SCORE,RS.NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.NICNO,RS.RATED_DATE,RS.BRANCH_NAME,RS.CHANGED_DATE,RS.RATED_USR,RS.CHANGED_USR,"
                            + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED"
                            + " FROM ML_CUS_RISK RS where RS.RISK_LEVEL = 'Medium' and RS.BRANCH_NAME = '" + branch + "' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "'";
                } else if (repType.equals("Low Risk Customers' Report")) {
                    RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.BRANCH_NAME,RS.TERRORIST,RS.RISK_SCORE,RS.NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.NICNO,RS.RATED_DATE,RS.BRANCH_NAME,RS.CHANGED_DATE,RS.RATED_USR,RS.CHANGED_USR,"
                            + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED"
                            + " FROM ML_CUS_RISK RS where RS.RISK_LEVEL = 'Low' and RS.BRANCH_NAME = '" + branch + "' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "'";
                } else if (repType.equals("PEP Report")) {
                    RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.RISK_SCORE,RS.BRANCH_NAME,RS.TERRORIST,RS.NAME,RS.NICNO,RC.BRANCH_NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.RATED_DATE,RS.CHANGED_DATE,RS.CATEGORY,RS.RATED_USR,RS.CHANGED_USR,"
                            + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED FROM ML_CUS_RISK RS "
                            + "where RS.CATEGORY = 'PEP' and RS.BRANCH_NAME = '" + branch + "' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "'";
                } else if (repType.equals("Suspicious Activities Report")) {
                    RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.RISK_SCORE,RS.BRANCH_NAME,RS.TERRORIST,RS.NAME,RS.NICNO,RS.BRANCH_NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.RATED_DATE,RS.CHANGED_DATE,RC.SUSPICIOUS,RS.RATED_USR,RS.CHANGED_USR,"
                            + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED FROM ML_CUS_RISK RS "
                            + " where RC.SUSPICIOUS = 'YES' and RS.BRANCH_NAME = '" + branch + "' and  RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "'";
                } else if (repType.equals("Suspected Terrorist Report")) {
                    RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.RISK_SCORE,RS.BRANCH_NAME,RS.TERRORIST,RS.NAME,RS.NICNO,RC.BRANCH_NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.RATED_DATE,RS.CHANGED_DATE,RS.TERRORIST,RS.RATED_USR,RS.CHANGED_USR,"
                            + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED FROM ML_CUS_RISK"
                            + " where RS.TERRORIST = 'YES' and RS.BRANCH_NAME = '" + branch + "' and  RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "'";
                } else if (repType.equals("All Risk Type Report")) {
                    RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.BRANCH_NAME,RS.RISK_SCORE,RS.TERRORIST,RS.NAME,RS.NICNO,RS.BRANCH_NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.CHANGED_DATE,RS.RATED_DATE,RS.RATED_USR,RS.CHANGED_USR,"
                            + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED FROM ML_CUS_RISK RS"
                            + " where RS.BRANCH_NAME = '" + branch + "' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "'";
                }
            }
        } else if (branch.equals("All")) {
            if (repType.equals("High Risk Customers' Report")) {
                RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.RISK_SCORE,RS.NAME,RS.NICNO,RS.SUSPICIOUS,RS.BRANCH_NAME,RS.CATEGORY,RS.TERRORIST,"
                        + "RS.RATED_DATE,RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED,"
                        + "RS.CHANGED_DATE,RS.RATED_USR,RS.CHANGED_USR FROM ML_CUS_RISK RS where RS.RISK_LEVEL = 'High' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "' and INDIVIDUAL = '" + cusType + "'";
            } else if (repType.equals("Medium Risk Customers' Report")) {
                RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.SUSPICIOUS,RS.RISK_SCORE,RS.NAME,RS.BRANCH_NAME,RS.CATEGORY,RS.RATED_DATE,RS.TERRORIST,"
                        + "RS.NICNO,RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED,"
                        + "RS.CHANGED_DATE,RS.RATED_USR,RS.CHANGED_USR FROM ML_CUS_RISK RS where RS.RISK_LEVEL = 'Medium' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "' and INDIVIDUAL = '" + cusType + "'";
            } else if (repType.equals("Low Risk Customers' Report")) {
                RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.SUSPICIOUS,RS.RISK_SCORE,RS.NAME,RS.BRANCH_NAME,RS.CATEGORY,RS.RATED_DATE,RS.TERRORIST,"
                        + "RS.NICNO,RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED,"
                        + "RS.CHANGED_DATE,RS.RATED_USR,RS.CHANGED_USR FROM ML_CUS_RISK RS where RS.RISK_LEVEL = 'Low' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "' and INDIVIDUAL = '" + cusType + "'";
            } else if (repType.equals("PEP Report")) {
                RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.RISK_SCORE,RS.NAME,RS.BRANCH_NAME,RS.CATEGORY,RS.RATED_DATE,RS.TERRORIST,"
                        + "RS.NICNO,RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,"
                        + "RS.TYPE_ITEMSELECTED,RS.CHANGED_DATE,RS.CATEGORY,RS.RATED_USR,RS.CHANGED_USR FROM ML_CUS_RISK RS where RS.CATEGORY = 'PEP' and  RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "' and INDIVIDUAL = '" + cusType + "'";
            } else if (repType.equals("Suspicious Activities Report")) {
                RiskRepQuery = "select distinct RS.CUSNO,RS.PURPOSE_ITEMSELECTED,RS.RISK_LEVEL,RS.RISK_SCORE,RS.NAME,RS.BRANCH_NAME,RS.CATEGORY,RS.RATED_DATE,RS.TERRORIST,"
                        + "RS.NICNO,RS.LOC_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED,RS.CHANGED_DATE,RS.SUSPICIOUS,"
                        + "RS.RATED_USR,RS.CHANGED_USR FROM ML_CUS_RISK RS  where RS.SUSPICIOUS != '44' and  RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "' and INDIVIDUAL = '" + cusType + "'";
            } else if (repType.equals("Suspected Terrorist Report")) {
                RiskRepQuery = "select distinct RS.CUSNO,RS.PURPOSE_ITEMSELECTED,RS.RISK_LEVEL,RS.RISK_SCORE,RS.NAME,RS.BRANCH_NAME,RS.RATED_DATE,RS.TERRORIST,"
                        + "RS.CATEGORY,RS.LOC_ITEMSELECTED,RS.TYPE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.NICNO,RS.NATURE_ITEMSELECTED,RS.CHANGED_DATE,"
                        + "RS.TERRORIST,RS.RATED_USR,RS.CHANGED_USR FROM ML_CUS_RISK RS where RS.TERRORIST = 'YES' and  RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "' and INDIVIDUAL = '" + cusType + "'";
            } else if (repType.equals("All Risk Type Report")) {
                RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.BRANCH_NAME,RS.RISK_SCORE,RS.TERRORIST,RS.NAME,RS.NICNO,RS.BRANCH_NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.CHANGED_DATE,RS.RATED_DATE,RS.RATED_USR,RS.CHANGED_USR,"
                        + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED FROM ML_CUS_RISK RS"
                        + " where RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "' and INDIVIDUAL = '" + cusType + "'";
            }
        } else {
            if (repType.equals("High Risk Customers' Report")) {
                RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.BRANCH_NAME,RS.RISK_SCORE,RS.TERRORIST,RS.NAME,RS.NICNO,RS.BRANCH_NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.CHANGED_DATE,RS.RATED_DATE,RS.RATED_USR,RS.CHANGED_USR,"
                        + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED FROM ML_CUS_RISK RS"
                        + " where RS.RISK_LEVEL = 'High' and RS.BRANCH_NAME = '" + branch + "' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "' and INDIVIDUAL = '" + cusType + "'";
            } else if (repType.equals("Medium Risk Customers' Report")) {
                RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.BRANCH_NAME,RS.TERRORIST,RS.RISK_SCORE,RS.NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.NICNO,RS.RATED_DATE,RS.BRANCH_NAME,RS.CHANGED_DATE,RS.RATED_USR,RS.CHANGED_USR,"
                        + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED"
                        + " FROM ML_CUS_RISK RS where RS.RISK_LEVEL = 'Medium' and RS.BRANCH_NAME = '" + branch + "' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "' and INDIVIDUAL = '" + cusType + "'";
            } else if (repType.equals("Low Risk Customers' Report")) {
                RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.BRANCH_NAME,RS.TERRORIST,RS.RISK_SCORE,RS.NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.NICNO,RS.RATED_DATE,RS.BRANCH_NAME,RS.CHANGED_DATE,RS.RATED_USR,RS.CHANGED_USR,"
                        + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED"
                        + " FROM ML_CUS_RISK RS where RS.RISK_LEVEL = 'Low' and RS.BRANCH_NAME = '" + branch + "' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "' and INDIVIDUAL = '" + cusType + "'";
            } else if (repType.equals("PEP Report")) {
                RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.RISK_SCORE,RS.BRANCH_NAME,RS.TERRORIST,RS.NAME,RS.NICNO,RC.BRANCH_NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.RATED_DATE,RS.CHANGED_DATE,RS.CATEGORY,RS.RATED_USR,RS.CHANGED_USR,"
                        + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED FROM ML_CUS_RISK RS "
                        + "where RS.CATEGORY = 'PEP' and RS.BRANCH_NAME = '" + branch + "' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "' and INDIVIDUAL = '" + cusType + "'";
            } else if (repType.equals("Suspicious Activities Report")) {
                RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.RISK_SCORE,RS.BRANCH_NAME,RS.TERRORIST,RS.NAME,RS.NICNO,RS.BRANCH_NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.RATED_DATE,RS.CHANGED_DATE,RC.SUSPICIOUS,RS.RATED_USR,RS.CHANGED_USR,"
                        + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED FROM ML_CUS_RISK RS "
                        + " where RC.SUSPICIOUS = 'YES' and RS.BRANCH_NAME = '" + branch + "' and  RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "' and INDIVIDUAL = '" + cusType + "'";
            } else if (repType.equals("Suspected Terrorist Report")) {
                RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.RISK_SCORE,RS.BRANCH_NAME,RS.TERRORIST,RS.NAME,RS.NICNO,RC.BRANCH_NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.RATED_DATE,RS.CHANGED_DATE,RS.TERRORIST,RS.RATED_USR,RS.CHANGED_USR,"
                        + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED FROM ML_CUS_RISK"
                        + " where RS.TERRORIST = 'YES' and RS.BRANCH_NAME = '" + branch + "' and  RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "' and INDIVIDUAL = '" + cusType + "'";
            } else if (repType.equals("All Risk Type Report")) {
                RiskRepQuery = "select distinct RS.CUSNO,RS.RISK_LEVEL,RS.BRANCH_NAME,RS.RISK_SCORE,RS.TERRORIST,RS.NAME,RS.NICNO,RS.BRANCH_NAME,RS.SUSPICIOUS,RS.CATEGORY,RS.CHANGED_DATE,RS.RATED_DATE,RS.RATED_USR,RS.CHANGED_USR,"
                        + "RS.LOC_ITEMSELECTED,RS.PURPOSE_ITEMSELECTED,RS.SOURCE_ITEMSELECTED,RS.VOL_ITEMSELECTED,RS.NATURE_ITEMSELECTED,RS.TYPE_ITEMSELECTED FROM ML_CUS_RISK RS"
                        + " where RS.BRANCH_NAME = '" + branch + "' and RS.CHANGED_DATE between '" + fTime + "' and '" + tTime + "' and INDIVIDUAL = '" + cusType + "'";
            }
        }
        try {
            rs = AS400DBConnection.search(RiskRepQuery);
            if (rs != null) {
                while (rs.next()) {
                    RepdataDTO ob = new RepdataDTO();
                    ob.setCusNo(rs.getString("CUSNO"));
                    ob.setRiskLevel(rs.getString("RISK_LEVEL"));
                    ob.setName(rs.getString("NAME"));
                    ob.setBranch(rs.getString("BRANCH_NAME"));
                    ob.setOrDate(rs.getString("RATED_DATE"));
                    ob.setChangedDate(rs.getString("CHANGED_DATE"));
                    ob.setCusNic(rs.getString("NICNO"));
                    ob.setOrUsr(rs.getString("RATED_USR"));
                    ob.setChangedUsr(rs.getString("CHANGED_USR"));
                    ob.setTerrorist(rs.getString("TERRORIST"));
                    ob.setSuspicious(rs.getString("SUSPICIOUS"));
                    ob.setCusType(rs.getString("TYPE_ITEMSELECTED"));
                    ob.setNature(rs.getString("NATURE_ITEMSELECTED"));
                    ob.setSource(rs.getString("SOURCE_ITEMSELECTED"));
                    ob.setVolume(rs.getString("VOL_ITEMSELECTED"));
                    ob.setLocation(rs.getString("LOC_ITEMSELECTED"));
                    ob.setPurpose(rs.getString("PURPOSE_ITEMSELECTED"));

                    if (rs.getString("CATEGORY").startsWith("PEP")) {
                        ob.setCategory("PEP");
                    } else {
                        ob.setCategory("Non PEP");
                    }

                    if (repType.equals("PEP Report")) {
                        ob.setScore_pep(rs.getString("CATEGORY"));
                    } else if (repType.equals("Suspicious Activities Report")) {
                        if (rs.getString("SUSPICIOUS").equals("YES")) {
                            ob.setScore_pep("SUS");
                        }
                    } else if (repType.equals("Suspected Terrorist Report")) {
                        if (rs.getString("TERRORIST").equals("YES")) {
                            ob.setScore_pep("Terr/Alert");
                        }
                    } else {
                        ob.setScore_pep(rs.getString("RISK_SCORE"));
                    }

                    myList.add(ob);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(MLProcessController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return myList;
    }
}
