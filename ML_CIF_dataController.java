package com.mbsl.velocity.risk.auth;

import com.mbsl.velocity.dal.as400.AS400DBConnection;
import com.mbsl.velocity.risk.utill.AS400Date;
import java.sql.ResultSet;
import java.sql.SQLException;
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

@Controller
public class ML_CIF_dataController {

    @RequestMapping(value = "/get_cifSalutation")
    @ResponseBody
    public List<String> getCusCategories(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400Date aS400Date = new AS400Date(libs);
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rs = null;
        try {
            String query5 = "SELECT FVDSC1 FROM frp005 where FVFLD ='CUCTIT'";
            rs = AS400DBConnection.search(query5);

            while (rs.next()) {
                myList.add(rs.getString("FVDSC1"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.getStatement().close();
            rs.close();
        }
        return myList;
    }

    @RequestMapping(value = "/get_cifCusType")
    @ResponseBody
    public List<String> get_cifCusType(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400Date aS400Date = new AS400Date(libs);
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rs = null;
        try {
            String query5 = "SELECT FVDSC1,FVVAL  FROM FRP002 where FVFLD ='CUPERS'";
            rs = AS400DBConnection.search(query5);
            String cifType = "";
            while (rs.next()) {
                String type = rs.getString("FVDSC1");
                String typeId = rs.getString("FVVAL");
                cifType = typeId + "-" + type;
                myList.add(cifType);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.getStatement().close();
            rs.close();
        }
        return myList;
    }

    @RequestMapping(value = "/get_addressType")
    @ResponseBody
    public List<String> get_addressType(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400Date aS400Date = new AS400Date(libs);
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rs = null;
        try {
            String query5 = "SELECT FVDSC1 FROM FRP002 where FVFLD ='CUPPCT'";
            rs = AS400DBConnection.search(query5);

            while (rs.next()) {
                myList.add(rs.getString("FVDSC1"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.getStatement().close();
            rs.close();
        }
        return myList;
    }

    @RequestMapping(value = "/get_DMI")
    @ResponseBody
    private List<String> get_DMI(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400Date aS400Date = new AS400Date(libs);
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rs = null;
        try {
            String query5 = "SELECT FVDSC1 FROM FRP002 where FVFLD ='CUMIDC'";
            rs = AS400DBConnection.search(query5);

            while (rs.next()) {
                myList.add(rs.getString("FVDSC1"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.getStatement().close();
            rs.close();
        }
        return myList;
    }

    @RequestMapping(value = "/get_cusClass")
    @ResponseBody
    private List<String> get_cusClass(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400Date aS400Date = new AS400Date(libs);
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rs = null;
        try {
            String query5 = "SELECT FVDSC1 FROM FRP005 where FVFLD ='CUCFLN'";
            rs = AS400DBConnection.search(query5);

            while (rs.next()) {
                myList.add(rs.getString("FVDSC1"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.getStatement().close();
            rs.close();
        }
        return myList;
    }

    @RequestMapping(value = "/get_prefCusList")
    @ResponseBody
    private List<String> get_prefCusList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400Date aS400Date = new AS400Date(libs);
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rs = null;
        try {
            String query5 = "SELECT FVDSC1 FROM FRP005 where FVFLD ='CUPREF'";
            rs = AS400DBConnection.search(query5);

            while (rs.next()) {
                myList.add(rs.getString("FVDSC1"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.getStatement().close();
            rs.close();
        }
        return myList;
    }

    @RequestMapping(value = "/get_dataSource")
    @ResponseBody
    private List<String> get_dataSource(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400Date aS400Date = new AS400Date(libs);
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rs = null;
        try {
            String query5 = "SELECT FVDSC1 FROM FRP005 where FVFLD ='CUSOOD'";
            rs = AS400DBConnection.search(query5);

            while (rs.next()) {
                myList.add(rs.getString("FVDSC1"));
            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.getStatement().close();
            rs.close();
        }
        return myList;
    }

    @RequestMapping(value = "/get_priOfficer/{officer}")
    @ResponseBody
    private int get_priOfficer(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("officer") String officer) throws Exception {

        int offResult = 0;
        ResultSet rs = null;
        try {
            String query5 = "SELECT MKOFNM FROM MCMKO01 where MKOFTY = 'FO' and MKOFCD = '" + officer + "' ";
            rs = DB.search(query5);
            if (rs.next()) {
                offResult = 1;
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.getStatement().close();
            rs.close();
        }
        return offResult;
    }

    @RequestMapping(value = "/get_mktSegment")
    @ResponseBody
    private List<String> get_mktSegment(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400Date aS400Date = new AS400Date(libs);
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rs = null;
        try {
            String query5 = "SELECT FVDSC1 FROM FRP005 where FVFLD = 'CUMARK'";
            rs = AS400DBConnection.search(query5);
            while (rs.next()) {
                myList.add(rs.getString("FVDSC1"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.getStatement().close();
            rs.close();
        }
        return myList;
    }

    @RequestMapping(value = "/get_status")
    @ResponseBody
    private List<String> get_get_status(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        List<String> libs = (List<String>) session.getAttribute("as400libs");
        AS400Date aS400Date = new AS400Date(libs);
        AS400DBConnection.setLibraries(libs);

        List<String> myList = new ArrayList<String>();
        ResultSet rs = null;
        try {
            String query5 = "SELECT FVDSC1, FVVAL FROM FRP005 where FVFLD = 'CUMRTS'";
            rs = AS400DBConnection.search(query5);
            while (rs.next()) {
                myList.add(rs.getString("FVVAL") + "-" + rs.getString("FVDSC1"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(MLDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.getStatement().close();
            rs.close();
        }
        return myList;
    }

}
