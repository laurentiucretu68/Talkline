package com.example.talkline.controller;

import com.example.talkline.entities.FriendRequest;
import com.example.talkline.entities.User;
import com.example.talkline.service.FriendRequestService;
import com.example.talkline.service.LocationService;
import com.example.talkline.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Controller
@AllArgsConstructor
public class AppController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final LocationService locationService;

    @Autowired
    private final FriendRequestService friendRequestService;

    @GetMapping("")
    public String showFirstPage(){
        return "sign-in";
    }

    @GetMapping("/notifications")
    public String showNotifications(){
        return "notifications";
    }

    @GetMapping("/messages")
    public String showMessages(){
        return "messages";
    }

    @GetMapping("/profile")
    public String showProfile(){
        return "profile";
    }

    @GetMapping("/requests")
    public String showRequests(){
        return "requests";
    }

    @GetMapping("/results")
    public String showResults(){
        return "results";
    }

    @GetMapping("/settings")
    public String showSettings(){
        return "settings";
    }

    @GetMapping("/sign-in")
    public String showSignIn(){
        return "sign-in";
    }

    @GetMapping("/sign-up")
    public String showSignUp(Model model){
        model.addAttribute("user", new User());
        return "sign-up";
    }

    @GetMapping("/list_users")
    public String viewUsersList(Model model){
        List<User> listUser = userService.getAllUsers();
        model.addAttribute("listUsers", listUser);
        return "users";
    }

    @GetMapping("/home")
    public String getUser(Principal principal, Model model){
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user_home", user);

        //Collection<User> friend_requests = userService.selectFriendRequests(principal.getName());

        Collection<User> friend_requests = userService.selectPossibleFriends(principal.getName());
        model.addAttribute("friendRequests", friend_requests);

        return "home";
    }

    @GetMapping("/login")
    public String login(){
        return "sign-in";
    }

    @RequestMapping("/process_register")
    public String processRegister(User user){
        if (!userService.findUserByEmail(user.getEmail()) && !userService.findUserByPhone(user.getPhone())){
            user.prepareInsert();
            locationService.addLocation(user.getLocation());
            userService.addUser(user);
            return "register_success";
        }
        return "redirect:/sign-up?result=duplicate";
    }

    @PostMapping("/send_friend_request")
    public String processSendFriendRequest(Principal principal, User user){
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSenderUser((User) principal);
        friendRequest.setReceiverUser(user);
        friendRequest.prepareInsert();
        friendRequestService.addRequest(friendRequest);
        return "home";
    }
}
