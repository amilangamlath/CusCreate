package com.mbsl.velocity.risk.auth;

import com.mbsl.velocity.dal.as400.AS400DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*
author- Amila Jayarathna
 */
public class MLRiskEvaluation {

    int finalTotalRiskScore = 0;

    int totTypeRiskScore = 0;
    int totNatureRiskScore = 0;
    int totSourceRiskScore = 0;
    int totVolRiskScore = 0;
    int totLocRiskScore = 0;
    int totPurposeRiskScore = 0;

    int typeRiskScore = 0;
    int natureRiskScore = 0;
    int sourceRiskScore = 0;
    int volRiskScore = 0;
    int locRiskScore = 0;
    int purposeRiskScore = 0;

    int typeRiskWeight = 0;
    int natureRiskWeight = 0;
    int sourceRiskWeight = 0;
    int volRiskWeight = 0;
    int locRiskWeight = 0;
    int purposeRiskWeight = 0;

    public int riskEvaluation(String category, String individual, String suspicious, String terrorist, String cusType, String nature, String source, String vol, String location, String purpose, HttpServletRequest req) throws Exception, NullPointerException {

        try {
            if (individual.equals("Individual")) {
                if (category.startsWith("PEP") || !(suspicious.contains("No any suspicious")) || terrorist.equals("YES")
                        || nature.equalsIgnoreCase("Sri Lankan (Non Resident)") || nature.equalsIgnoreCase("Foreign/Dual Citizen (Non Resident)")) {
                    finalTotalRiskScore = 300;
                } else {
                    MLRiskEvaluation ob = new MLRiskEvaluation();
                    finalTotalRiskScore = ob.commonriskCal(category, individual, suspicious, terrorist, cusType, nature, source, vol, location, purpose, req);
                }
            } else {
                if (category.startsWith("PEP") || terrorist.equals("YES") || !(suspicious.contains("No any suspicious"))
                        || cusType.equalsIgnoreCase("Charity/NGO/Trusts/Clubs/Societies") || cusType.equalsIgnoreCase("Off shore/Non resident company")
                        || cusType.equalsIgnoreCase("Other Non profit Organizations")) {
                    finalTotalRiskScore = 300;
                } else {
                    // Non individual
                    MLRiskEvaluation ob2 = new MLRiskEvaluation();
                    finalTotalRiskScore = ob2.commonriskCal(category, individual, suspicious, terrorist, cusType, nature, source, vol, location, purpose, req);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(MLRiskEvaluation.class.getName()).log(Level.SEVERE, null, e);
        }
        return finalTotalRiskScore;
    }

    private int commonriskCal(String category, String individual, String suspicious, String terrorist, String cusType, String nature, String source, String vol, String location, String purpose, HttpServletRequest req) throws SQLException {

        HttpSession session = req.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        String typeQuery = "SELECT RISK_WEIGHT,RISK_SCORE FROM ML_CUS_TYPE where DESCRIPTION='" + cusType + "' and acc_type='" + individual + "'";
        String natureQuery = "SELECT RISK_WEIGHT,RISK_SCORE FROM ML_CUS_TYPE where DESCRIPTION='" + nature + "' and acc_type='" + individual + "' and RISK_CAT = 'nature/bus'";
        String sourceQuery = "SELECT RISK_WEIGHT,RISK_SCORE FROM ML_CUS_TYPE where DESCRIPTION='" + source + "' and acc_type='" + individual + "'";
        String volQuery = "SELECT RISK_WEIGHT,RISK_SCORE FROM ML_CUS_TYPE where DESCRIPTION='" + vol + "' and acc_type='" + individual + "'";
        String locationQuery = "SELECT RISK_WEIGHT,RISK_SCORE FROM ML_CUS_TYPE where DESCRIPTION='" + location + "' and acc_type='" + individual + "'";
        String purposeQuery = "SELECT RISK_WEIGHT,RISK_SCORE FROM ML_CUS_TYPE where DESCRIPTION='" + purpose + "' and acc_type='" + individual + "'";

        ResultSet rs_type = null;
        ResultSet rs_nature = null;
        ResultSet rs_source = null;
        ResultSet rs_vol = null;
        ResultSet rs_location = null;
        ResultSet rs_purpose = null;

        try {
            rs_type = AS400DBConnection.search(typeQuery);
            if (rs_type != null) {
                while (rs_type.next()) {
                    typeRiskWeight = Integer.parseInt(rs_type.getString("RISK_WEIGHT"));
                    typeRiskScore = Integer.parseInt(rs_type.getString("RISK_SCORE"));
                    // System.out.println("typeRiskWeight*********" + typeRiskWeight * typeRiskScore);
                }
            }

            rs_nature = AS400DBConnection.search(natureQuery);
            if (rs_nature != null) {
                while (rs_nature.next()) {
                    natureRiskWeight = Integer.parseInt(rs_nature.getString("RISK_WEIGHT"));
                    natureRiskScore = Integer.parseInt(rs_nature.getString("RISK_SCORE"));
                    //  System.out.println("natureRiskWeight*********" + natureRiskWeight * natureRiskScore);
                }
            }

            rs_source = AS400DBConnection.search(sourceQuery);
            if (rs_source != null) {
                while (rs_source.next()) {
                    sourceRiskWeight = Integer.parseInt(rs_source.getString("RISK_WEIGHT"));
                    sourceRiskScore = Integer.parseInt(rs_source.getString("RISK_SCORE"));
                    //  System.out.println("rs_source*********" + sourceRiskWeight * sourceRiskScore);
                }
            }

            rs_vol = AS400DBConnection.search(volQuery);
            if (rs_vol != null) {
                while (rs_vol.next()) {
                    volRiskWeight = Integer.parseInt(rs_vol.getString("RISK_WEIGHT"));
                    volRiskScore = Integer.parseInt(rs_vol.getString("RISK_SCORE"));
                    // System.out.println("volRiskWeight*********" + volRiskWeight * volRiskScore);
                }
            }

            rs_location = AS400DBConnection.search(locationQuery);
            if (rs_location != null) {
                while (rs_location.next()) {
                    locRiskWeight = Integer.parseInt(rs_location.getString("RISK_WEIGHT"));
                    locRiskScore = Integer.parseInt(rs_location.getString("RISK_SCORE"));
                    //  System.out.println("locRiskWeight*********" + locRiskWeight * locRiskScore);
                }
            }

            rs_purpose = AS400DBConnection.search(purposeQuery);
            if (rs_purpose != null) {
                while (rs_purpose.next()) {
                    purposeRiskWeight = Integer.parseInt(rs_purpose.getString("RISK_WEIGHT"));
                    purposeRiskScore = Integer.parseInt(rs_purpose.getString("RISK_SCORE"));
                    // System.out.println("purposeRiskWeight*********" + purposeRiskWeight * purposeRiskScore);
                }
            }

        } catch (Exception e) {
            Logger.getLogger(MLRiskEvaluation.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs_type != null) {
                    rs_type.getStatement().close();
                    rs_type.close();
                }
                if (rs_nature != null) {
                    rs_nature.getStatement().close();
                    rs_nature.close();
                }
                if (rs_source != null) {
                    rs_source.getStatement().close();
                    rs_source.close();
                }
                if (rs_vol != null) {
                    rs_vol.getStatement().close();
                    rs_vol.close();
                }
                if (rs_location != null) {
                    rs_location.getStatement().close();
                    rs_location.close();
                }
                if (rs_purpose != null) {
                    rs_purpose.getStatement().close();
                    rs_purpose.close();
                }

            } catch (SQLException ex) {
                Logger.getLogger(MLRiskEvaluation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        totTypeRiskScore = typeRiskScore * typeRiskWeight;
        totLocRiskScore = locRiskScore * locRiskWeight;
        totNatureRiskScore = natureRiskScore * natureRiskWeight;
        totPurposeRiskScore = purposeRiskScore * purposeRiskWeight;
        totSourceRiskScore = sourceRiskScore * sourceRiskWeight;
        totVolRiskScore = volRiskWeight * volRiskScore;

        finalTotalRiskScore = totTypeRiskScore + totLocRiskScore + totNatureRiskScore + totPurposeRiskScore
                + totSourceRiskScore + totVolRiskScore;

        //  System.out.println("MLRiskEvaluation ****" + " " + totTypeRiskScore + " " + totLocRiskScore + " " + totNatureRiskScore + " " + totPurposeRiskScore + " "
        //   + totSourceRiskScore + " " + totVolRiskScore);
        return finalTotalRiskScore;
    }
}
