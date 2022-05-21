package com.example.talkline.controller;

import com.example.talkline.entities.*;
import com.example.talkline.service.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.util.List;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final LocationService locationService;

    private final FriendRequestService friendRequestService;

    private final FriendsService friendsService;

    private final PostService postService;

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

    @GetMapping("/settings")
    public String showSettings(Principal principal, Model model){
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user_home", user);
        model.addAttribute("user", new User());
        return "settings";
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
        if (!userService.findUserByEmail(user.getEmail()) && !userService.findUserByPhone(user.getPhone())
            && !Objects.equals(user.getEmail(), "") && !Objects.equals(user.getPhone(), "")){
            user.prepareInsert();
            locationService.addLocation(user.getLocation());
            userService.addUser(user);
            return "register_success";
        }
        return "redirect:/sign-up?result=duplicate";
    }

    @PostMapping("/update_user_info")
    public String updateUserInfo(Principal principal,
                                 @RequestParam(value = "firstName", required = false) String firstName,
                                 @RequestParam(value = "lastName", required = false) String lastName,
                                 @RequestParam(value = "email", required = false) String email,
                                 @RequestParam(value = "phone", required = false) String phone,
                                 @RequestParam(value = "city", required = false) String city,
                                 @RequestParam(value = "district", required = false) String district,
                                 @RequestParam(value = "birthDate", required = false) String birthDate,
                                 @RequestParam(value = "password", required = false) String password,
                                 @RequestParam(value = "passwordRep", required = false) String passwordRep,
                                 @RequestParam(value = "profilePicture") MultipartFile[] multipartFile,
                                 RedirectAttributes redirectAttributes){

        if (!Objects.equals(password, passwordRep)){
            redirectAttributes.addFlashAttribute("message",
                    "Parolele nu coincid");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/settings";
        }

        User user = userService.findByEmail(principal.getName());
        String emailPrincipal = user.getEmail();

        if (!Objects.equals(firstName, "")){
            user.setFirstName(firstName);
        }
        if (!Objects.equals(lastName, "")){
            user.setLastName(lastName);
        }
        if (!Objects.equals(email, "")){
            user.setEmail(email);
        }
        if (!Objects.equals(phone, "")){
            user.setPhone(phone);
        }
        if (!Objects.equals(city, "")){
            user.getLocation().setCity(city);
        }
        if (!Objects.equals(district, "")){
            user.getLocation().setCity(district);
        }
        if (!Objects.equals(password, "")){
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(password));
        }
        if (!Objects.equals(birthDate, "")){
            user.setBirthDate(birthDate);
        }

        if (multipartFile != null){
            String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/img/";
            StringBuilder fileNames = new StringBuilder();
            for (MultipartFile file : multipartFile){
                Path fileNamePath = Paths.get(uploadDirectory, file.getOriginalFilename());
                fileNames.append(file.getOriginalFilename());
                try{
                    Files.write(fileNamePath, file.getBytes());
                }catch (IOException e){
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("message",
                            "Actualizarea a esuat");
                    redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
                }
            }

            user.setProfilePicture("/img/" + fileNames);
        }

        userService.updateUser(user, emailPrincipal);
        redirectAttributes.addFlashAttribute("message",
                "Date actualizate cu succes!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/settings";
    }

    public void sendRequestPrepare(String receiverEmail){
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User senderUser = userService.findByEmail(((UserDetails)principal).getUsername());
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setReceiverUser(userService.findByEmail(receiverEmail));
        friendRequest.setSenderUser(senderUser);
        friendRequest.prepareInsert();
        friendRequestService.addRequest(friendRequest);
    }

    @GetMapping("/send_friend_request/{email}")
    public String addFriendRequest(@PathVariable("email") String email, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message_request",
                "Cererea a esuat");
        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

        sendRequestPrepare(email);

        redirectAttributes.addFlashAttribute("message_request",
                "Cerere trimisa cu succes!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/home";
    }

    @RequestMapping("/short_send_friend_request/{email}")
    public String sendFriendReq(@PathVariable("email") String email){
        sendRequestPrepare(email);
        return "redirect:/"+ email;
    }

    @RequestMapping("/accept_friend_request/{email}")
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
        friendRequestService.deleteFriendRequestByEmails(senderUser.getEmail(), email);
        return "redirect:/requests";
    }

    @GetMapping("/decline_friend_request/{email}")
    public String declineFriendRequest(@PathVariable("email") String email) {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User senderUser = userService.findByEmail(((UserDetails)principal).getUsername());
        friendRequestService.deleteFriendRequestByEmails(email, senderUser.getEmail());
        return "redirect:/requests";
    }

    @RequestMapping("/{email}")
    public String loadProfile(@PathVariable("email") String email, Model model) {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User currentUser = userService.findByEmail(((UserDetails)principal).getUsername());
        model.addAttribute("user_home2", currentUser);

        User userProfile = userService.findByEmail(email);
        model.addAttribute("user_profile", userProfile);

        Collection<Post> profile_posts = postService.selectPostsFromProfile(email);


        int profileCurrentUser = 0;
        if (!Objects.equals(email, currentUser.getEmail())){
            Collection<User> friends = userService.getFriends(currentUser.getEmail());
            for (User u : friends){
                if (Objects.equals(u.getEmail(), email)) {
                    profileCurrentUser = 1;
                    break;
                }
            }
        } else{
            profileCurrentUser = 1;
        }

        model.addAttribute("isFriend", profileCurrentUser);
        model.addAttribute("profilePosts", profile_posts);
        return "profile";
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

    @GetMapping("/results")
    public void listResults(@RequestParam(value = "words") String words,
                            Model model, Principal principal){
        Collection<User> users = userService.searchUsers(words);
        model.addAttribute("users", users);
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user_home", user);
    }

    @GetMapping("/messages")
    public String showMessages(Model model, Principal principal){
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user_home", user);
        Collection<User> friends = userService.getFriends(principal.getName());
        model.addAttribute("friends", friends);
        return "messages";
    }

    @PostMapping("/update-likes")
    public void updateLikes(){
        System.out.println("adfsafasjgfashfgas");
    }
}
