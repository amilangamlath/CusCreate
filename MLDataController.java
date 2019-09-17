package com.mbsl.velocity.risk.auth;

import com.mbsl.velocity.dal.as400.AS400DBConnection;
import com.mbsl.velocity.risk.ml.MLCusDTO;
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
public class MLDataController {

    @RequestMapping(value = "/getCusNat/{acc_type}")
    @ResponseBody
    public List<String> getCusCategories(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("acc_type") String acc_type) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rsCusCat = null;
        try {
            String queryCat = "SELECT description FROM ml_cus_type where risk_cat='nature/bus' and acc_type='" + acc_type + "' order by ID";

            rsCusCat = AS400DBConnection.search(queryCat);
            if (rsCusCat != null) {
                while (rsCusCat.next()) {
                    myList.add(rsCusCat.getString("description"));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rsCusCat != null) {
                    rsCusCat.getStatement().close();
                    rsCusCat.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return myList;
    }

    @RequestMapping(value = "/getCusType/{acc_type}")
    @ResponseBody
    public List<String> getCusType(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("acc_type") String acc_type) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rsCusType = null;
        try {
            String queryCusType = "SELECT description FROM ml_cus_type where risk_cat='type' and acc_type='" + acc_type + "' order by ID";

            rsCusType = AS400DBConnection.search(queryCusType);
            if (rsCusType != null) {
                while (rsCusType.next()) {
                    myList.add(rsCusType.getString("description"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rsCusType != null) {
                    rsCusType.getStatement().close();
                    rsCusType.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return myList;
    }

    @RequestMapping(value = "/getCusPurpose/{acc_type}")
    @ResponseBody
    public List<String> getCusPurpose(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("acc_type") String acc_type) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rsPurp = null;
        try {
            String queryPurp = "SELECT description FROM ml_cus_type where risk_cat='purpose' and acc_type='" + acc_type + "' order by ID";

            rsPurp = AS400DBConnection.search(queryPurp);
            if (rsPurp != null) {
                while (rsPurp.next()) {
                    myList.add(rsPurp.getString("description"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rsPurp != null) {
                    rsPurp.getStatement().close();
                    rsPurp.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return myList;
    }

    @RequestMapping(value = "/getCusSource/{acc_type}")
    @ResponseBody
    public List<String> getCusSource(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("acc_type") String acc_type) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rsSource = null;
        try {
            String querySource = "SELECT description FROM ml_cus_type where risk_cat='source' and acc_type='" + acc_type + "' order by ID";

            rsSource = AS400DBConnection.search(querySource);
            if (rsSource != null) {
                while (rsSource.next()) {
                    myList.add(rsSource.getString("description"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rsSource != null) {
                    rsSource.getStatement().close();
                    rsSource.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return myList;
    }

    @RequestMapping(value = "/getCusVol/{acc_type}")
    @ResponseBody
    public List<String> getCusVol(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("acc_type") String acc_type) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rsVol = null;
        try {
            String queryVol = "SELECT description FROM ml_cus_type where risk_cat='volume' and acc_type='" + acc_type + "' order by ID";

            rsVol = AS400DBConnection.search(queryVol);
            if (rsVol != null) {
                while (rsVol.next()) {
                    myList.add(rsVol.getString("description"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rsVol != null) {
                    rsVol.getStatement().close();
                    rsVol.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return myList;
    }

    @RequestMapping(value = "/getCusLoc/{acc_type}")
    @ResponseBody
    public List<String> getCusLoc(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("acc_type") String acc_type) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rsLoc = null;
        try {
            String queryLoc = "SELECT description FROM ml_cus_type where risk_cat='location' and acc_type='" + acc_type + "' order by ID";

            rsLoc = AS400DBConnection.search(queryLoc);
            if (rsLoc != null) {
                while (rsLoc.next()) {
                    myList.add(rsLoc.getString("description"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rsLoc != null) {
                    rsLoc.getStatement().close();
                    rsLoc.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return myList;
    }

    @RequestMapping(value = "/getCusPep/{acc_type}")
    @ResponseBody
    public List<String> getCusPep(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("acc_type") String acc_type) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rsPep = null;
        try {
            String queryPep = "SELECT description FROM ml_cus_type where risk_cat='category' and acc_type='" + acc_type + "' order by ID";

            rsPep = AS400DBConnection.search(queryPep);
            if (rsPep != null) {
                while (rsPep.next()) {
                    myList.add(rsPep.getString("description"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rsPep != null) {
                    rsPep.getStatement().close();
                    rsPep.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return myList;
    }

    @RequestMapping(value = "/getCusSus")
    @ResponseBody
    public List<String> getCusSus(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rsSus = null;
        try {
            String querySus = "SELECT description FROM ml_cus_type where risk_cat='suspicious' order by ID";

            rsSus = AS400DBConnection.search(querySus);
            if (rsSus != null) {
                while (rsSus.next()) {
                    myList.add(rsSus.getString("description"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rsSus != null) {
                    rsSus.getStatement().close();
                    rsSus.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return myList;
    }

    @RequestMapping(value = "/getCusTerr")
    @ResponseBody
    public List<String> getCusTerr(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rsTerr = null;
        try {
            String queryTerr = "SELECT description FROM ml_cus_type where risk_cat='terrorist' order by ID";

            rsTerr = AS400DBConnection.search(queryTerr);
            if (rsTerr != null) {
                while (rsTerr.next()) {
                    myList.add(rsTerr.getString("description"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rsTerr != null) {
                    rsTerr.getStatement().close();
                    rsTerr.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return myList;
    }

    @RequestMapping(value = "/getCusSearchBy")
    @ResponseBody
    public List<String> getCusSearchBy(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rs6 = null;
        try {
            String query6 = "SELECT SEARCH_BY FROM ML_SEARCH_BY";
            rs6 = AS400DBConnection.search(query6);
            if (rs6 != null) {
                while (rs6.next()) {
                    myList.add(rs6.getString("SEARCH_BY"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs6 != null) {
                    rs6.getStatement().close();
                    rs6.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return myList;
    }

    @RequestMapping(value = "/get_mlcus_det/{no}/{search_by}")
    @ResponseBody
    public MLCusDTO get_CusCIFName(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("no") String no, @PathVariable("search_by") String search_by) throws ParseException, IOException, Exception, SQLException {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400DBConnection.setLibraries(libs);

        MLCusDTO cusOB = new MLCusDTO();
        List<String> cusNO = new ArrayList<String>();

        ResultSet rsCusSearch = null;
        ResultSet rsCusSearchTap = null;

        ResultSet rsSearchFromNicCus1 = null;
        ResultSet rsSearchExiRat = null;

        ResultSet rsFromNicCus2 = null;
        ResultSet rsFromNic2 = null;

        ResultSet rsFromNic3 = null;
        ResultSet rsCusNic3 = null;
        ResultSet rsSearchFromNicCus3 = null;

        ResultSet rsFromNic4 = null;
        ResultSet rsFromNicCus4 = null;

        ResultSet rsFromNic5 = null;
        ResultSet rsFromNicCus5 = null;

        if (search_by.equalsIgnoreCase("Saving AC No")) {

            Long nicNo = Long.parseLong(no);
            String searchFromAcc = new StringBuffer().append("select cp.CUNA1,cp.CUNTID,cp.CUBRCH,cp.CUPERS,c.CUX1AC,cf.CFBRNM from ((CUP00901 c ")
                    .append(" inner join CUP00301 cp on  cp.CUNBR =c.cux1cs) inner join CFP102 cf on cf.CFBRCH = cp.CUBRCH)  where c.CUX1AP = 20 and (LOWER(cp.cuna1)")
                    .append("like LOWER('%")
                    .append(nicNo).append("%') or c.CUX1AC like('%")
                    .append(nicNo).append("%')) limit 1").toString();

            String selectSav = "select DMACCT, DMSHRT from TAP002 where DMACCT = '" + nicNo + "'";
            String searchFromNicCus = "";

            try {
                rsCusSearchTap = AS400DBConnection.search(selectSav);

                if (rsCusSearchTap.next()) {
                    String name = rsCusSearchTap.getString("DMSHRT");
                    String acc = rsCusSearchTap.getString("DMACCT");
                    cusOB.setName(name);
                    cusOB.setAccNO(acc);

                    rsCusSearch = AS400DBConnection.search(searchFromAcc);
                    if (rsCusSearch.next()) {
                        String type = rsCusSearch.getString("CUPERS");
                        String branch = rsCusSearch.getString("CFBRNM");
                        String cusNic = rsCusSearch.getString("CUNTID");
                        cusOB.setBranch(branch);
                        cusOB.setCusNic(cusNic);

                        searchFromNicCus = "select CUP00301.CUNBR from CUP00301 where CUP00301.CUNTID = '" + cusNic + "'";
                        rsSearchFromNicCus1 = AS400DBConnection.search(searchFromNicCus);

                        if ("P".equals(type)) {
                            cusOB.setIndividual("Individual");
                        } else {
                            cusOB.setIndividual("Non-Individual");
                        }
                    }
                    if (!(rsSearchFromNicCus1 == null)) {
                        while (rsSearchFromNicCus1.next()) {
                            cusNO.add(rsSearchFromNicCus1.getString("CUNBR"));

                            String searchExiRat = "select CATEGORY,SUSPICIOUS,TERRORIST,TYPE_ITEMSELECTED,NATURE_ITEMSELECTED,SOURCE_ITEMSELECTED,VOL_ITEMSELECTED,"
                                    + "LOC_ITEMSELECTED,PURPOSE_ITEMSELECTED from ML_CUS_RISK where CUSNO = '" + rsSearchFromNicCus1.getString("CUNBR") + "'";
                            rsSearchExiRat = AS400DBConnection.search(searchExiRat);
                            if (rsSearchExiRat.next()) {
                                cusOB.setCategory(rsSearchExiRat.getString("CATEGORY"));
                                cusOB.setSuspicious(rsSearchExiRat.getString("SUSPICIOUS"));
                                cusOB.setTerrorist(rsSearchExiRat.getString("TERRORIST"));
                                cusOB.setCusType(rsSearchExiRat.getString("TYPE_ITEMSELECTED"));
                                cusOB.setNature(rsSearchExiRat.getString("NATURE_ITEMSELECTED"));
                                cusOB.setSource(rsSearchExiRat.getString("SOURCE_ITEMSELECTED"));
                                cusOB.setVolume(rsSearchExiRat.getString("VOL_ITEMSELECTED"));
                                cusOB.setLocation(rsSearchExiRat.getString("LOC_ITEMSELECTED"));
                                cusOB.setPurpose(rsSearchExiRat.getString("PURPOSE_ITEMSELECTED"));
                            }
                        }
                    }
                    cusOB.setCusno(cusNO);
                }

            } catch (SQLException ex) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (rsCusSearchTap != null) {
                        rsCusSearchTap.getStatement().close();
                        rsCusSearchTap.close();
                    }
                    if (rsSearchFromNicCus1 != null) {
                        rsSearchFromNicCus1.getStatement().close();
                        rsSearchFromNicCus1.close();
                    }
                    if (rsSearchExiRat != null) {
                        rsSearchExiRat.getStatement().close();
                        rsSearchExiRat.close();
                    }
                } catch (Exception e) {
                    Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
                }

            }

        } else if (search_by.equals("NIC No")) {
            String searchFromNic = "select CUP00301.CUNBR,CUP00301.CUNTID,CUP00301.CUSEX,CUP00301.CUNA2,CUP00301.CUNA3,CUP00301.CUNA4,CUP00301.CUNA1,CUP00301.CUBRCH,CUP00301.CUNTID,CUP00301.CUPERS,CFP102.CFBRNM from CUP00301 inner join CFP102 on CFP102.CFBRCH = CUP00301.CUBRCH where CUP00301.CUNTID = '" + no + "'";
            String searchFromNicCus = "select CUP00301.CUNBR from CUP00301 where CUP00301.CUNTID = '" + no + "'";

            try {
                rsFromNic2 = AS400DBConnection.search(searchFromNic);
                rsFromNicCus2 = AS400DBConnection.search(searchFromNicCus);

                if (rsFromNic2.next()) {
                    String name = rsFromNic2.getString("CUNA1");
                    String acc = rsFromNic2.getString("CUNTID");
                    String type = rsFromNic2.getString("CUPERS");
                    // String cusNo = rsFromNic.getString("CUNBR");
                    String branch = rsFromNic2.getString("CFBRNM");
                    String cusNic = rsFromNic2.getString("CUNTID");
                    String sex = rsFromNic2.getString("CUSEX");
                    String add1 = rsFromNic2.getString("CUNA1");
                    String add2 = rsFromNic2.getString("CUNA2");
                    String add3 = rsFromNic2.getString("CUNA3");

                    cusOB.setName(name);
                    cusOB.setAccNO(acc);
                    // cusOB.setCusno(cusNo);
                    cusOB.setBranch(branch);
                    cusOB.setCusNic(cusNic);
                    cusOB.setGender(sex);
                    cusOB.setAdd1(add1);
                    cusOB.setAdd2(add2);
                    cusOB.setAdd3(add3);

                    if ("P".equals(type)) {
                        cusOB.setIndividual("Individual");
                    } else {
                        cusOB.setIndividual("Non-Individual");
                    }
                }
                if (!(rsFromNicCus2 == null)) {
                    while (rsFromNicCus2.next()) {
                        cusNO.add(rsFromNicCus2.getString("CUNBR"));

                        String searchExiRat = "select CATEGORY,SUSPICIOUS,TERRORIST,TYPE_ITEMSELECTED,NATURE_ITEMSELECTED,SOURCE_ITEMSELECTED,VOL_ITEMSELECTED,"
                                + "LOC_ITEMSELECTED,PURPOSE_ITEMSELECTED from ML_CUS_RISK where CUSNO = '" + rsFromNicCus2.getString("CUNBR") + "'";
                        rsSearchExiRat = AS400DBConnection.search(searchExiRat);
                        if (rsSearchExiRat.next()) {
                            cusOB.setCategory(rsSearchExiRat.getString("CATEGORY"));
                            cusOB.setSuspicious(rsSearchExiRat.getString("SUSPICIOUS"));
                            cusOB.setTerrorist(rsSearchExiRat.getString("TERRORIST"));
                            cusOB.setCusType(rsSearchExiRat.getString("TYPE_ITEMSELECTED"));
                            cusOB.setNature(rsSearchExiRat.getString("NATURE_ITEMSELECTED"));
                            cusOB.setSource(rsSearchExiRat.getString("SOURCE_ITEMSELECTED"));
                            cusOB.setVolume(rsSearchExiRat.getString("VOL_ITEMSELECTED"));
                            cusOB.setLocation(rsSearchExiRat.getString("LOC_ITEMSELECTED"));
                            cusOB.setPurpose(rsSearchExiRat.getString("PURPOSE_ITEMSELECTED"));
                        }
                    }
                }
                cusOB.setCusno(cusNO);
            } catch (SQLException ex) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (rsFromNic2 != null) {
                        rsFromNic2.getStatement().close();
                        rsFromNic2.close();
                    }
                    if (rsFromNicCus2 != null) {
                        rsFromNicCus2.getStatement().close();
                        rsFromNicCus2.close();
                    }
                    if (rsSearchExiRat != null) {
                        rsSearchExiRat.getStatement().close();
                        rsSearchExiRat.close();
                    }
                } catch (Exception e) {
                    Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
                }

            }
        } else if (search_by.equals("FD AC No")) {
            String searchFromNic = "select cup00901.CUX1CS,TMP003.TMPERS,TMP003.TMSHRT,TMP003.TMACCT,CFP102.CFBRNM from ((TMP003 inner join cup00901 on cup00901.CUX1AC = TMP003.TMACCT) "
                    + "inner join CFP102 on CFP102.CFBRCH = TMP003.TMBRCH) where TMP003.TMACCT = '" + no + "'";
            String cusNic = "";
            String searchFromNicCus = "";
            String cusNicQuery = "";
            try {
                rsFromNic3 = AS400DBConnection.search(searchFromNic);

                if (rsFromNic3.next()) {
                    String name = rsFromNic3.getString("TMSHRT");
                    String acc = rsFromNic3.getString("TMACCT");
                    String type = rsFromNic3.getString("TMPERS");
                    String cusNo = rsFromNic3.getString("CUX1CS");
                    String branch = rsFromNic3.getString("CFBRNM");

                    cusOB.setName(name);
                    cusOB.setAccNO(acc);
                    //cusOB.setCusno(cusNo);
                    cusOB.setBranch(branch);

                    cusNicQuery = "select CUP00301.CUNTID from CUP00301 where CUP00301.CUNBR = '" + cusNo + "'";
                    rsCusNic3 = AS400DBConnection.search(cusNicQuery);
                    if (rsCusNic3.next()) {
                        cusNic = rsCusNic3.getString("CUNTID");
                        cusOB.setCusNic(cusNic);
                    }
                    searchFromNicCus = "select CUP00301.CUNBR from CUP00301 where CUP00301.CUNTID = '" + cusNic + "'";
                    rsSearchFromNicCus3 = AS400DBConnection.search(searchFromNicCus);

                    if (!(rsSearchFromNicCus3 == null)) {
                        while (rsSearchFromNicCus3.next()) {
                            cusNO.add(rsSearchFromNicCus3.getString("CUNBR"));

                            String searchExiRat = "select CATEGORY,SUSPICIOUS,TERRORIST,TYPE_ITEMSELECTED,NATURE_ITEMSELECTED,SOURCE_ITEMSELECTED,VOL_ITEMSELECTED,"
                                    + "LOC_ITEMSELECTED,PURPOSE_ITEMSELECTED from ML_CUS_RISK where CUSNO = '" + rsSearchFromNicCus3.getString("CUNBR") + "'";
                            rsSearchExiRat = AS400DBConnection.search(searchExiRat);
                            if (rsSearchExiRat.next()) {
                                cusOB.setCategory(rsSearchExiRat.getString("CATEGORY"));
                                cusOB.setSuspicious(rsSearchExiRat.getString("SUSPICIOUS"));
                                cusOB.setTerrorist(rsSearchExiRat.getString("TERRORIST"));
                                cusOB.setCusType(rsSearchExiRat.getString("TYPE_ITEMSELECTED"));
                                cusOB.setNature(rsSearchExiRat.getString("NATURE_ITEMSELECTED"));
                                cusOB.setSource(rsSearchExiRat.getString("SOURCE_ITEMSELECTED"));
                                cusOB.setVolume(rsSearchExiRat.getString("VOL_ITEMSELECTED"));
                                cusOB.setLocation(rsSearchExiRat.getString("LOC_ITEMSELECTED"));
                                cusOB.setPurpose(rsSearchExiRat.getString("PURPOSE_ITEMSELECTED"));
                            }
                        }
                    }

                    if ("P".equals(type)) {
                        cusOB.setIndividual("Individual");
                    } else {
                        cusOB.setIndividual("Non-Individual");
                    }
                }
                cusOB.setCusno(cusNO);

            } catch (SQLException ex) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (rsFromNic3 != null) {
                        rsFromNic3.getStatement().close();
                        rsFromNic3.close();
                    }
                    if (rsCusNic3 != null) {
                        rsCusNic3.getStatement().close();
                        rsCusNic3.close();
                    }
                    if (rsSearchFromNicCus3 != null) {
                        rsSearchFromNicCus3.getStatement().close();
                        rsSearchFromNicCus3.close();
                    }
                    if (rsSearchExiRat != null) {
                        rsSearchExiRat.getStatement().close();
                        rsSearchExiRat.close();
                    }
                } catch (Exception e) {
                    Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
                }

            }
        } else if (search_by.equals("Loan-No")) {

            Long lID = Long.parseLong(no);
            String searchFromNic = "select CFP102.CFBRNM,cup00301.CUNTID,cup00901.CUX1CS,cup00301.CUPERS,LNP00301.LNNOTE,LNP00301.LNSHRT  from (((LNP00301 inner join cup00901 on cup00901.CUX1AC = LNP00301.LNNOTE)"
                    + " inner join cup00301 on cup00901.CUX1CS = cup00301.CUNBR) inner join CFP102 on CFP102.CFBRCH = LNP00301.LNBRCH) where LNP00301.LNNOTE  = '" + lID + "'";
            String searchFromNicCus = "";
            try {
                rsFromNic4 = AS400DBConnection.search(searchFromNic);
                if (rsFromNic4.next()) {
                    String name = rsFromNic4.getString("LNSHRT");
                    String acc = rsFromNic4.getString("LNNOTE");
                    String type = rsFromNic4.getString("CUPERS");
                    // String cusNo = rsFromNic.getString("CUX1CS");
                    String branch = rsFromNic4.getString("CFBRNM");
                    String nic = rsFromNic4.getString("CUNTID");

                    cusOB.setName(name);
                    cusOB.setAccNO(acc);
                    // cusOB.setCusno(cusNo);
                    cusOB.setBranch(branch);
                    cusOB.setCusNic(nic);

                    searchFromNicCus = "select CUP00301.CUNBR from CUP00301 where CUP00301.CUNTID = '" + nic + "'";
                    rsFromNicCus4 = AS400DBConnection.search(searchFromNicCus);

                    if ("P".equals(type)) {
                        cusOB.setIndividual("Individual");
                    } else {
                        cusOB.setIndividual("Non-Individual");
                    }
                }
                if (!(rsFromNicCus4 == null)) {
                    while (rsFromNicCus4.next()) {
                        cusNO.add(rsFromNicCus4.getString("CUNBR"));

                        String searchExiRat = "select CATEGORY,SUSPICIOUS,TERRORIST,TYPE_ITEMSELECTED,NATURE_ITEMSELECTED,SOURCE_ITEMSELECTED,VOL_ITEMSELECTED,"
                                + "LOC_ITEMSELECTED,PURPOSE_ITEMSELECTED from ML_CUS_RISK where CUSNO = '" + rsFromNicCus4.getString("CUNBR") + "'";
                        rsSearchExiRat = AS400DBConnection.search(searchExiRat);
                        if (rsSearchExiRat.next()) {
                            cusOB.setCategory(rsSearchExiRat.getString("CATEGORY"));
                            cusOB.setSuspicious(rsSearchExiRat.getString("SUSPICIOUS"));
                            cusOB.setTerrorist(rsSearchExiRat.getString("TERRORIST"));
                            cusOB.setCusType(rsSearchExiRat.getString("TYPE_ITEMSELECTED"));
                            cusOB.setNature(rsSearchExiRat.getString("NATURE_ITEMSELECTED"));
                            cusOB.setSource(rsSearchExiRat.getString("SOURCE_ITEMSELECTED"));
                            cusOB.setVolume(rsSearchExiRat.getString("VOL_ITEMSELECTED"));
                            cusOB.setLocation(rsSearchExiRat.getString("LOC_ITEMSELECTED"));
                            cusOB.setPurpose(rsSearchExiRat.getString("PURPOSE_ITEMSELECTED"));
                        }
                    }
                }
                cusOB.setCusno(cusNO);
            } catch (SQLException ex) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (rsFromNic4 != null) {
                        rsFromNic4.getStatement().close();
                        rsFromNic4.close();
                    }
                    if (rsFromNicCus4 != null) {
                        rsFromNicCus4.getStatement().close();
                        rsFromNicCus4.close();
                    }
                    if (rsSearchExiRat != null) {
                        rsSearchExiRat.getStatement().close();
                        rsSearchExiRat.close();
                    }
                } catch (Exception e) {
                    Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
                }

            }
        } else if (search_by.equals("CIF-No")) {
            String searchFromNic = "select CUP00301.CUNBR,CUP00301.CUNTID,CUP00301.CUNA2,CUP00301.CUNA3,CUP00301.CUNA4,CUP00301.CUNA1,CUP00301.CUBRCH,CUP00301.CUNTID,CUP00301.CUPERS,CFP102.CFBRNM "
                    + "from CUP00301 inner join CFP102 on CFP102.CFBRCH = CUP00301.CUBRCH where CUP00301.CUNBR like '%" + no + "' limit 1";
            String searchFromNicCus = "";

            try {
                rsFromNic5 = AS400DBConnection.search(searchFromNic);
                if (rsFromNic5.next()) {
                    String name = rsFromNic5.getString("CUNA1");
                    String acc = rsFromNic5.getString("CUNBR");
                    String type = rsFromNic5.getString("CUPERS");
                    // String cusNo = rsFromNic.getString("CUNBR");
                    String branch = rsFromNic5.getString("CFBRNM");
                    String cusNic = rsFromNic5.getString("CUNTID");

                    cusOB.setName(name);
                    cusOB.setAccNO(acc);
                    // cusOB.setCusno(cusNo);
                    cusOB.setBranch(branch);
                    cusOB.setCusNic(cusNic);

                    searchFromNicCus = "select CUP00301.CUNBR from CUP00301 where CUP00301.CUNTID = '" + cusNic + "'";
                    rsFromNicCus5 = AS400DBConnection.search(searchFromNicCus);
                    if ("P".equals(type)) {
                        cusOB.setIndividual("Individual");
                    } else {
                        cusOB.setIndividual("Non-Individual");
                    }
                }
                if (!(rsFromNicCus5 == null)) {
                    while (rsFromNicCus5.next()) {
                        cusNO.add(rsFromNicCus5.getString("CUNBR"));

                        String searchExiRat = "select CATEGORY,SUSPICIOUS,TERRORIST,TYPE_ITEMSELECTED,NATURE_ITEMSELECTED,SOURCE_ITEMSELECTED,VOL_ITEMSELECTED,"
                                + "LOC_ITEMSELECTED,PURPOSE_ITEMSELECTED from ML_CUS_RISK where CUSNO = '" + rsFromNicCus5.getString("CUNBR") + "'";
                        rsSearchExiRat = AS400DBConnection.search(searchExiRat);
                        if (rsSearchExiRat.next()) {
                            cusOB.setCategory(rsSearchExiRat.getString("CATEGORY"));
                            cusOB.setSuspicious(rsSearchExiRat.getString("SUSPICIOUS"));
                            cusOB.setTerrorist(rsSearchExiRat.getString("TERRORIST"));
                            cusOB.setCusType(rsSearchExiRat.getString("TYPE_ITEMSELECTED"));
                            cusOB.setNature(rsSearchExiRat.getString("NATURE_ITEMSELECTED"));
                            cusOB.setSource(rsSearchExiRat.getString("SOURCE_ITEMSELECTED"));
                            cusOB.setVolume(rsSearchExiRat.getString("VOL_ITEMSELECTED"));
                            cusOB.setLocation(rsSearchExiRat.getString("LOC_ITEMSELECTED"));
                            cusOB.setPurpose(rsSearchExiRat.getString("PURPOSE_ITEMSELECTED"));
                        }
                    }
                }
                cusOB.setCusno(cusNO);
            } catch (SQLException ex) {
                Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (rsFromNic5 != null) {
                        rsFromNic5.getStatement().close();
                        rsFromNic5.close();
                    }
                    if (rsFromNicCus5 != null) {
                        rsFromNicCus5.getStatement().close();
                        rsFromNicCus5.close();
                    }
                } catch (Exception e) {
                    Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, e);
                }

            }
        }
        return cusOB;
    }

}
