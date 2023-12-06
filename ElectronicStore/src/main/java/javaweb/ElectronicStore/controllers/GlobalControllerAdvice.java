//package javaweb.ElectronicStore.controllers;
//
//import java.security.Principal;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ModelAttribute;
//
//import javaweb.ElectronicStore.models.User;
//import javaweb.ElectronicStore.oauth2.StaticClass;
//import javaweb.ElectronicStore.repository.UserRepository;
//
//@ControllerAdvice
//public class GlobalControllerAdvice {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private StaticClass staticClass;
//
//    @ModelAttribute("user")
//    public User commonUser(Principal p) {
//        if (p != null) {
//            String email = p.getName();
//            User user = userRepository.findByEmail(email);
//            if (user == null) {
//                user = new User();
//                user.setLastName(staticClass.name);
//                user.setEmail(staticClass.email);
//            }
//            return user;
//        }
//        return null;
//    }
//}