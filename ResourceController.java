package com.mbsl.velocity.risk.auth;

//import com.mbsl.velocity.slips.helper.BankBranchHelper;
//import com.mbsl.velocity.slips.helper.SlipsReturnCodeHelper;
import com.mbsl.velocity.risk.model.SecuredAccessModel;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
author - Amila Jayarathna 
 */
@Controller
public class ResourceController {

    private static final String jwtTokenCookieName = "JWT-TOKEN";

    @RequestMapping("/")
    public String home() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String protectedResource(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {

        SecuredAccessModel auth = new SecuredAccessModel(); //request.getAttribute("authority");

        auth.addAuthority("1.0");
        auth.addAuthority("2");
        auth.addAuthority("3.1");

        httpServletRequest.setAttribute("secmod", auth);
        return "/WEB-INF/views/home.jsp";
    }

    @RequestMapping("/risk")
    public String risk(HttpServletRequest httpServletRequest) {
        return "/WEB-INF/views/new_cus_risk.jsp";
    }

    @RequestMapping("/reg")
    public String reg(HttpServletRequest httpServletRequest) {
        return "/WEB-INF/views/cus_reg.jsp";
    }

    @RequestMapping("/riskRep")
    public String riskReports(HttpServletRequest httpServletRequest) {
        return "/WEB-INF/views/ml_reports.jsp";
    }

    @RequestMapping("/401")
    public String unauth(HttpServletRequest httpServletRequest) {
        return "/WEB-INF/views/401.jsp";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        CookieUtil.clear(httpServletResponse, httpServletRequest, jwtTokenCookieName);
        HttpSession session = httpServletRequest.getSession();
        Enumeration<String> sessionAttr = session.getAttributeNames();

        while (sessionAttr.hasMoreElements()) {
            String attrName = sessionAttr.nextElement();
            System.out.println("Removed session attr : " + attrName);
            session.removeAttribute(attrName);
        }
        session.invalidate();
        return "redirect:/";
    }
}
