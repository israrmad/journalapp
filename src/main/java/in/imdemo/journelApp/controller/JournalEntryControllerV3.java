package in.imdemo.journelApp.controller;

import in.imdemo.journelApp.model.JournalEntryModel;
import in.imdemo.journelApp.service.JournalService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/journal")
@RestController
public class JournalEntryControllerV3 {

    @Autowired
    private JournalService journalService;


    @GetMapping
    public ResponseEntity<List<JournalEntryModel>> getAllJournalEntriesOfUser(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<JournalEntryModel> liEntries = null;
        try{
            liEntries = journalService.getAllEntriesOfUser(userName);
            if(liEntries!=null){
                return new ResponseEntity<>(liEntries, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<JournalEntryModel> createJournalEntryForUser(
            @RequestBody JournalEntryModel entry){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try{
            entry.setDate(LocalDateTime.now());
            JournalEntryModel journalEntryModel
                    = journalService.saveEntryForUser(entry,userName);
            if(journalEntryModel!=null){
                return new ResponseEntity<>(journalEntryModel,HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(Exception ex){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{entryID}")
    public ResponseEntity<Boolean> removeEntryForUser(@PathVariable ObjectId entryID){
        JournalEntryModel model = null;
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try{
            model = journalService.removeUserEntry(userName,entryID);
            if(model!=null){
                return new ResponseEntity<>(true,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{entryID}")
    public ResponseEntity<Boolean> updateEntryForUser(@PathVariable ObjectId entryID,
                                                      @RequestBody JournalEntryModel entry){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try{
            entry = journalService.updateEntryForUser(userName,entryID,entry);
            if(entry!=null){
                return new ResponseEntity<>(true,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
            }
        }catch(Exception ex){
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
