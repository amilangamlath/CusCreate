package com.mbsl.velocity.risk.auth;

import com.mbsl.velocity.as400.environment.AS400Libraries;
import com.mbsl.velocity.risk.entity.UacModelAccess;
import com.mbsl.velocity.risk.entity.UacUsers;
import com.mbsl.velocity.risk.impl.UacModelAccessDaoImpl;
import com.mbsl.velocity.risk.impl.UacUsersDaoImpl;
import io.jsonwebtoken.SignatureException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpSession;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final String jwtTokenCookieName = "JWT-TOKEN";
    private static final String signingKey = "signingKey";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String headerName = httpServletRequest.getHeader("x-requested-with");
        //System.out.println("header : " + headerName);
        String cookieData = null;
        try {
            cookieData = JwtUtil.getSubject(httpServletRequest, jwtTokenCookieName, signingKey);
        } catch (SignatureException e) {
            //System.out.println("Authentication Failed! redirecting...");
            //System.out.println(e.getMessage());
            //e.printStackTrace();

            String authService = this.getFilterConfig().getInitParameter("services.auth");
            String login_redirect_url = authService + "?redirect=" + httpServletRequest.getRequestURL().append('?').append(getQueryString(httpServletRequest));

            if (null == headerName) {
                //Synchronous request
                // System.out.println("Synchronous request");
                httpServletResponse.sendRedirect(login_redirect_url);
                return;
            } else {
                //Ajax Request
                // System.out.println("Ajax Request");
                httpServletResponse.getWriter().write("{\"error_code\":401,\"redirect_url\":\"" + login_redirect_url + "\"}");
                return;
            }
        }

        //    System.out.println("bankRec : " + cookieData);
        //System.out.println("Filter service......");
        if (cookieData != null) {

            String[] cookieArray = cookieData.split(":");

            String username = cookieArray[0].toUpperCase();
            String bankcode = cookieArray[1].toUpperCase();
            // System.out.println("username [" + username + "]; bank [" + bankcode + "];");

            if (username == null || bankcode == null) {
                String authService = this.getFilterConfig().getInitParameter("services.auth");
                String login_redirect_url = authService + "?redirect=" + httpServletRequest.getRequestURL().append('?').append(getQueryString(httpServletRequest));
                if (null == headerName) {
                    //Synchronous request
                    // System.out.println("Synchronous request");
                    httpServletResponse.sendRedirect(login_redirect_url);
                } else {
                    //Ajax Request
                    // System.out.println("Ajax Request");
                    httpServletResponse.getWriter().write("{\"error_code\":401,\"redirect_url\":\"" + login_redirect_url + "\"}");
                }
            } else {

                HttpSession session = httpServletRequest.getSession();
                // System.out.println("REQ : " + httpServletRequest.getRequestURI());
                session.setAttribute("user", username);
                session.setAttribute("bank", bankcode);
                String as400env = "MBPRODCPY";//MBPRODCPY MBTRIAL21 MBPROD
                session.setAttribute("as400env", as400env);

                if (null == session.getAttribute("as400libs")) {
                    List<String> libs = new AS400Libraries().getLibraries(as400env);
                    session.setAttribute("as400libs", libs);
                }

                if (!httpServletRequest.getRequestURI().equals(httpServletRequest.getContextPath() + "/401")) {
                    if (!httpServletRequest.getRequestURI().matches(".*(css|jpg|png|gif|js|map|eot|svg|ttf|woff|woff2)$")) {
                        //  System.out.println("URI : " + httpServletRequest.getRequestURI() + " - NOT .JS|.CSS|.MAP");
                        if (null == session.getAttribute("uac_access")) {
                            List<String> accessList = new ArrayList<>();
                            UacUsersDaoImpl uacUsersDaoImpl = new UacUsersDaoImpl();
                            uacUsersDaoImpl.setbankcode(bankcode);
                            UacUsers user = uacUsersDaoImpl.getUacUsersByDyQuery("FROM UacUsers WHERE bankCode = '" + bankcode + "' AND userName = '" + username + "'");
                            if (user != null) {

                                session.setAttribute("full_name", user.getFirstName() + " " + user.getLastName());
                                session.setAttribute("display_name", user.getDisplayName());
                                session.setAttribute("designation", user.getDesignation());
                                session.setAttribute("dept_branch", user.getBrDepDes());

                                if (user.getAccessModal() != null && !user.getAccessModal().equals("")) {
                                    String userModels = user.getAccessModal().trim();
                                    UacModelAccessDaoImpl uacModelAccessDaoImpl = new UacModelAccessDaoImpl();
                                    uacModelAccessDaoImpl.setbankcode(bankcode);

                                    String[] userModelArray = userModels.split(",");

                                    if (userModelArray.length > 0) {
                                        for (String str : userModelArray) {
                                            String queryStr = "FROM UacModelAccess WHERE appCode = 'RISK' AND modelCode = '" + str.trim() + "'";
                                            for (UacModelAccess uacModelAccess : uacModelAccessDaoImpl.getUacModelAccessByQuery(queryStr)) {

                                                accessList.add(uacModelAccess.getAccessUrl());
                                                //  System.out.println("access : " + uacModelAccess.getAccessUrl());
                                            }
                                        }
                                    }
                                }
                            }

                            session.setAttribute("uac_access", accessList);
                        }

                        List<String> uac_accessList = (List<String>) session.getAttribute("uac_access");

                        String validReq = "";

                        if (httpServletRequest.getRequestURI().equalsIgnoreCase("/RISK/")) {
                            validReq = "";
                        } else {
                            String myReq[] = httpServletRequest.getRequestURI().split("RISK/");
                            if (myReq[1].contains("/")) {
                                String myReq2[] = myReq[1].split("/");
                                validReq = myReq2[0];
                                // System.out.println("Amila Req 2 ==========" + validReq);
                            } else {
                                if (myReq[1].startsWith("home")) {
                                    validReq = "home";
                                } else {
                                    validReq = myReq[1];
                                }
                                // System.out.println("Amila Req ==========" + myReq[1]);
                            }
                        }

                        // System.out.println("Req==========" + httpServletRequest.getRequestURI());
                        if (uac_accessList.contains("/RISK/" + validReq)) {
                            //access granted
                            filterChain.doFilter(httpServletRequest, httpServletResponse);
                        } else {
                            //unauthorized
                            if (null == headerName) {
                                // System.out.println("Synchronous request");
                                httpServletResponse.sendRedirect("401");
                            } else {
                                //Ajax Request
                                // System.out.println("Ajax Request");
                                httpServletResponse.getWriter().write("{\"error_code\":403,\"redirect_url\":\"" + httpServletRequest.getRequestURL() + "\",\"message\":\"You are not authorize to access this function! (" + httpServletRequest.getRequestURL() + ")\"}");
                            }
                        }

                    } else {
                        // System.out.println("URI : " + httpServletRequest.getRequestURI() + " - :D .JS|.CSS|.MAP");
                        filterChain.doFilter(httpServletRequest, httpServletResponse);
                    }

                } else {
                    session.removeAttribute("uac_access");
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                }

            }

        } else {
            String authService = this.getFilterConfig().getInitParameter("services.auth");
            String login_redirect_url = authService + "?redirect=" + httpServletRequest.getRequestURL().append('?').append(getQueryString(httpServletRequest));
            if (null == headerName) {
                //Synchronous request
                // System.out.println("Synchronous request");
                httpServletResponse.sendRedirect(login_redirect_url);
            } else {
                //Ajax Request
                // System.out.println("Ajax Request");
                httpServletResponse.getWriter().write("{\"error_code\":401,\"redirect_url\":\"" + login_redirect_url + "\"}");
            }
        }

    }

    protected String getQueryString(HttpServletRequest request) {
        String query = "";
        // System.out.println("context path : " + request.getContextPath());
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String parameterName = (String) enumeration.nextElement();

            query += parameterName + "=";
            for (String string : request.getParameterValues(parameterName)) {
                query += string + ",";
            }
            query = query.substring(0, query.length() - 1);
        }
        // System.out.println("query : " + request.getQueryString());
        return query;
    }

}
