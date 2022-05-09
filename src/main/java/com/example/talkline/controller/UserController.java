package com.example.talkline.controller;

import com.example.talkline.entities.*;
import com.example.talkline.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Controller
@AllArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final LocationService locationService;

    @Autowired
    private final FriendRequestService friendRequestService;

    @Autowired
    private final FriendsService friendsService;

    @Autowired
    private final PostService postService;

    @Autowired
    private final CommentService commentService;

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

    @GetMapping("/requests")
    public String getRequests(Principal principal, Model model){
        Collection<User> friend_requests = userService.selectFriendRequests(principal.getName());
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user_home", user);
        model.addAttribute("friend_requests", friend_requests);
        return "requests";
    }

    @GetMapping("/home")
    public String getUser(Principal principal, Model model){
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user_home", user);
        model.addAttribute("current_post", new Post());

        Collection<User> friend_requests = userService.selectPossibleFriends(principal.getName());
        Collection<Post> home_posts = postService.selectPostsByUserEmail(user.getEmail());

        model.addAttribute("friendRequests", friend_requests);
        model.addAttribute("homePosts", home_posts);
        return "home";
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

    @GetMapping("/send_friend_request/{email}")
    public String addFriendRequest(@PathVariable("email") String email, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message_request",
                "Cererea a esuat");
        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

        Object principal = SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getPrincipal();
        User senderUser = userService.findByEmail(((UserDetails)principal).getUsername());

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setReceiverUser(userService.findByEmail(email));
        friendRequest.setSenderUser(senderUser);
        friendRequest.prepareInsert();
        friendRequestService.addRequest(friendRequest);

        redirectAttributes.addFlashAttribute("message_request",
                "Cerere trimisa cu succes!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/home";
    }

    @GetMapping("/accept_friend_request/{email}")
    public String acceptFriendRequest(@PathVariable("email") String email) {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User senderUser = userService.findByEmail(((UserDetails)principal).getUsername());

        Friends friendship = new Friends();
        friendship.setUser1(userService.findByEmail(email));
        friendship.setUser2(senderUser);
        friendsService.addFriendship(friendship);

        //friendRequestService.deleteFriendRequestByEmails(email, senderUser.getEmail());

        return "redirect:/requests";
    }

    @GetMapping("/decline_friend_request/{email}")
    public String declineFriendRequest(@PathVariable("email") String email) {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User senderUser = userService.findByEmail(((UserDetails)principal).getUsername());
        //friendRequestService.deleteFriendRequestByEmails(email, senderUser.getEmail());

        return "redirect:/requests";
    }

    @PostMapping("/post_something")
    public String postSomething(@RequestParam("pic") MultipartFile[] multipartFile,
                                Post current_post, RedirectAttributes redirectAttributes){
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User currentUser = userService.findByEmail(((UserDetails)principal).getUsername());

        current_post.setUser(currentUser);
        current_post.setLikes(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        current_post.setPostingDate(dtf.format(now));

        String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/img/posts/";
        StringBuilder fileNames = new StringBuilder();
        for (MultipartFile file : multipartFile){
            Path fileNamePath = Paths.get(uploadDirectory, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            try{
                Files.write(fileNamePath, file.getBytes());
            }catch (IOException e){
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("message",
                        "Incarcarea a esuat");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            }
        }

        current_post.setPicture("/img/posts/" + fileNames);

        postService.addPost(current_post);
        redirectAttributes.addFlashAttribute("message",
                "Postare adaugata cu succes!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/home";
    }
}
