package in.imdemo.journelApp.controller;

import in.imdemo.journelApp.model.JournalEntryModel;
import in.imdemo.journelApp.model.UserEntityModel;
import in.imdemo.journelApp.service.JournalService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/__journal")
@RestController
public class JournalEntryControllerV2 {

    @Autowired
    private JournalService journalService;


    @GetMapping("{userName}")
    public ResponseEntity<List<JournalEntryModel>> getAllJournalEntriesOfUser(@PathVariable String userName){
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
    public ResponseEntity<JournalEntryModel> createEntry(@RequestBody JournalEntryModel entry){
        try{
            entry.setDate(LocalDateTime.now());
            JournalEntryModel journalEntryModel
                    = journalService.saveEntry(entry);
            if(journalEntryModel!=null){
                return new ResponseEntity<>(journalEntryModel,HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch(Exception ex){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntryModel> createJournalEntryForUser(
            @PathVariable String userName,
            @RequestBody JournalEntryModel entry){
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

    @DeleteMapping("id/{myID}")
    public ResponseEntity<Boolean> removeEntry(@PathVariable ObjectId myID)
    {
        JournalEntryModel model = null;
        try{
            model = journalService.removeEntry(myID);
            if(model!=null){
                return new ResponseEntity<>(true,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{userName}/{entryID}")
    public ResponseEntity<Boolean> removeEntryForUser(@PathVariable String userName,
                                                      @PathVariable ObjectId entryID){
        JournalEntryModel model = null;
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

    @PutMapping("id/{myID}")
    public ResponseEntity<Boolean> updateEntry(@PathVariable ObjectId myID,
                                         @RequestBody JournalEntryModel entry){
        try{
            entry = journalService.updateEntry(myID,entry);
            if(entry!=null){
                return new ResponseEntity<>(true,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{userName}/{entryID}")
    public ResponseEntity<Boolean> updateEntryForUser(@PathVariable String userName,
                                                      @PathVariable ObjectId entryID,
                                                      @RequestBody JournalEntryModel entry){
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

    @GetMapping("id/{myID}")
    public ResponseEntity<JournalEntryModel> getJournalEntryById(@PathVariable ObjectId myID){
        JournalEntryModel model = null;
        try{
            model = journalService.getEntry(myID);
            if(model!=null){
                return new ResponseEntity<>(model,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(model,HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new ResponseEntity<>(model,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
