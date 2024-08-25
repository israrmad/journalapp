package in.imdemo.journelApp.controller;

import in.imdemo.journelApp.model.JournalEntryModel;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@RequestMapping("/_journel/")
@RestController
public class JournelEntryController {

    private HashMap<ObjectId, JournalEntryModel> journelEntries =
            new HashMap<>();

    @GetMapping
    public List<JournalEntryModel> getAll(){
        if(!journelEntries.isEmpty()){
            return journelEntries.values().stream().collect(Collectors.toList());
        }else{
            return null;
        }
    }

    @PostMapping
    public JournalEntryModel createEntry(@RequestBody JournalEntryModel entry){
        return this.journelEntries.put(entry.getId(),entry);
    }

    @DeleteMapping("id/{myID}")
    public JournalEntryModel removeEntry(@PathVariable Integer myID){
        return this.journelEntries.remove(myID);
    }

    @PutMapping
    public JournalEntryModel updateEntry(@PathVariable ObjectId myID,
                                         @RequestBody JournalEntryModel entry){
        return this.journelEntries.put(myID,entry);
    }

    @GetMapping("id/{myID}")
    public JournalEntryModel getJournelEntryById(@PathVariable Integer myID){
        try{
            return this.journelEntries.get(myID);
        }catch(Exception e){
            System.out.println("Exception accured "+e.getMessage());
            return new JournalEntryModel();
        }
    }

}
