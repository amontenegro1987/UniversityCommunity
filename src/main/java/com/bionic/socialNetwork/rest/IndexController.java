package com.bionic.socialNetwork.rest;

import com.bionic.socialNetwork.logic.LoginLogic;
import com.bionic.socialNetwork.logic.RegistrationLogic;
import com.bionic.socialNetwork.logic.SessionLogic;
import com.bionic.socialNetwork.models.User;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * @author Dmytro Troshchuk
 * @version 1.00  14.07.14.
 */
@Path("index")
public class IndexController {
    @Context
    private ServletContext context;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public InputStream getPage(@Context HttpServletRequest request) {
        Long userID = (Long) request.getAttribute("userId");

        if (userID == null) {
            return context.getResourceAsStream("/WEB-INF/pages/login.html");
        } else {
            return context.getResourceAsStream("/WEB-INF/pages/user.html");
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/x-www-form-urlencoded")
    @Path("login")
    public String login(@Context HttpServletRequest request,
                        @Context HttpServletResponse response,
                        @FormParam("login") String login,
                        @FormParam("pass") String password) {

        LoginLogic log = new LoginLogic();
        User user = log.getUser(login, password);
        Cookie cookie;
        Cookie userIdCookie;

        if (user != null) {
            SessionLogic sessionLogic = new SessionLogic();
            cookie = new Cookie("sessionId", sessionLogic.getNewSession(user));
            cookie.setPath("/");
            response.addCookie(cookie);
            userIdCookie = new Cookie("userId", String.valueOf(user.getId()));
            userIdCookie.setPath("/");
            response.addCookie(userIdCookie);

            return "{\"status\": true}";
        } else {
            return "{\"status\": false}";
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/x-www-form-urlencoded")
    @Path("registration")
    public String registration(@FormParam("name") String name,
                               @FormParam("surname") String surname,
                               @FormParam("position") String position,
                               @FormParam("email") String login,
                               @FormParam("password") String password,
                               @FormParam("invite") String invite) {

        RegistrationLogic registrationLogic = new RegistrationLogic();

        if (registrationLogic.checkInviteCode(invite)) {
            if (registrationLogic.addUser(name, surname, login, password, position)) {
                return "{\"status\": true}";
            } else {
                return "{\"status\": false}";
            }
        } else {
            return "{\"status\": noInvite}";
        }
    }


}
