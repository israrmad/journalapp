package in.imdemo.journelApp.controller;

import in.imdemo.journelApp.model.JournalEntryModel;
import in.imdemo.journelApp.model.UserEntityModel;
import in.imdemo.journelApp.repo.UserEntryRepository;
import in.imdemo.journelApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    private static final PasswordEncoder passWordEncoder = new
            BCryptPasswordEncoder();
    @GetMapping
    public ResponseEntity<List<UserEntityModel>> getAll(){
        List<UserEntityModel> userEntityModelList = null;
        try{
            userEntityModelList = userService.getAll();
            if(userEntityModelList!=null){
                return new ResponseEntity<>(userEntityModelList,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(userEntityModelList,HttpStatus.NOT_FOUND);
            }
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<Boolean> updateUser(@RequestBody UserEntityModel entry){
        Authentication authentication
                = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try{
            entry = userService.updateEntry(entry,userName);
            if(entry!=null){
                return new ResponseEntity<>(true,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser(){
        UserEntityModel entry =null;
        Authentication authentication
                = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try{
            entry = userService.removeUser(userName);
            if(entry!=null){
                return new ResponseEntity<>(true,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
