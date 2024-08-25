package in.imdemo.journelApp.controller;

import in.imdemo.journelApp.model.JournalEntryModel;
import in.imdemo.journelApp.model.UserEntityModel;
import in.imdemo.journelApp.service.JournalService;
import in.imdemo.journelApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    UserService userService;

    @Autowired
    private JournalService journalService;

    private static final PasswordEncoder passWordEncoder = new
            BCryptPasswordEncoder();

    @GetMapping("health-check")
    public String checkHealth(){
        return "Running";
    }

    @GetMapping("/get-all-journal")
    public ResponseEntity<List<JournalEntryModel>> getAll(){
        try{
            List<JournalEntryModel> liEntries = journalService.getAll();
            return new ResponseEntity<>(liEntries, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<Boolean> addUser(@RequestBody UserEntityModel model){
        try{
            model.setPassWord(passWordEncoder.encode(model.getPassWord()));
            model.setRoles(Arrays.asList("USER"));
            model = userService.saveEntry(model);
            if(model!=null){
                return new ResponseEntity<>(true, HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
            }
        }catch(Exception ex){
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
