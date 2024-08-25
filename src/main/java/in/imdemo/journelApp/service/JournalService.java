package in.imdemo.journelApp.service;

import in.imdemo.journelApp.entity.JournalEntry;
import in.imdemo.journelApp.model.JournalEntryModel;
import in.imdemo.journelApp.model.UserEntityModel;
import in.imdemo.journelApp.repo.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JournalService {

    @Autowired
    JournalEntryRepository journalRepo;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("modelMapper")
    ModelMapper mapper;

    public JournalEntryModel saveEntry(JournalEntryModel model){
        try{
            JournalEntry journalEntry = mapper.map(model,JournalEntry.class);
            if(journalEntry!=null){
                journalRepo.save(journalEntry);
                return model;
            }else{
                System.out.println("Error in saving data into DB.");
                return null;
            }
        }catch(Exception ex){
            System.out.println("Exception in saving entry in DB");
            return null;
        }
    }

    @Transactional
    public JournalEntryModel saveEntryForUser(JournalEntryModel model, String userName){
        UserEntityModel userEntityModel = null;
        try{
            userEntityModel = userService.getUser(userName);
            if (userEntityModel!=null){
                System.out.println("User found from DB.");
                JournalEntry journalEntry = mapper.map(model,JournalEntry.class);
                if(journalEntry!=null){
                    JournalEntry saved = journalRepo.save(journalEntry);
                    model = mapper.map(saved,JournalEntryModel.class);
                    List<JournalEntryModel> journalEntryModelList
                            = userEntityModel.getJournalEntries();
                    journalEntryModelList.add(model);
                    userService.saveEntry(userEntityModel);
                    System.out.println("User entry added."+userEntityModel.toString());
                    return model;
                }else{
                    System.out.println("Error in saving data into DB.");
                    return null;
                }
            }else{
                System.out.println("Error in saving data into DB.");
                return  null;
            }
        }catch(Exception ex){
            System.out.println("Exception in saving entry in DB");
            return null;
        }
    }

    public JournalEntryModel updateEntry(ObjectId entryID, JournalEntryModel model){
        try{
            Optional<JournalEntry> optional = journalRepo.findById(entryID);
            if(optional.isPresent()){
                JournalEntry journalEntry = optional.get();
                journalEntry.setTitle(model.getTitle()!=null && !model.getTitle().equals("")? model.getTitle() : journalEntry.getTitle());
                journalEntry.setContent(model.getContent()!=null && !model.getContent().equals("")? model.getContent() : journalEntry.getContent());
                journalEntry = journalRepo.save(journalEntry);
                return mapper.map(journalEntry,JournalEntryModel.class);
            }else{
                return null;
            }
        }catch(Exception ex){
            System.out.println("Exception in updateEntry from DB");
            return null;
        }
    }

    public JournalEntryModel updateEntryForUser(String userName,
                                                ObjectId entryID,
                                                JournalEntryModel model){
        UserEntityModel userEntityModel = null;

        try{
            userEntityModel = userService.getUser(userName);
            if (userEntityModel!=null){
                System.out.println("User found from DB.");
                List<JournalEntryModel> journalEntryModelList
                        = userEntityModel.getJournalEntries();
                if(journalEntryModelList!=null &&
                    journalEntryModelList.stream().
                    anyMatch(liEntry -> liEntry.getId().equals(entryID))){

                    Optional<JournalEntry> journalEntry = journalRepo.findById(entryID);
                    if(journalEntry.isPresent()){
                        JournalEntry entry = journalEntry.get();
                        entry.setTitle(model.getTitle()!=null
                                && !model.getTitle().isEmpty() ?
                                model.getTitle():entry.getTitle());
                        entry.setContent( model.getContent()!=null
                                && !model.getContent().isEmpty() ?
                                model.getContent():entry.getContent());
                        JournalEntry saved = journalRepo.save(entry);
                        model = mapper.map(saved,JournalEntryModel.class);
                        System.out.println("User entry updated."+model.toString());
                        return model;
                    }else{
                        System.out.println("Error in saving data into DB.");
                        return null;
                    }
                }else {
                    System.out.println("Journal entry not present against user.");
                    return null;
                }
            }else{
                System.out.println("Error updating user in DB.");
                return null;
            }
        }catch(Exception e){
            System.out.println("Error updating user in DB.");
            return null;
        }
    }

    public List<JournalEntryModel> getAll(){
        try{
            return journalRepo.findAll().stream().
                    map(m -> mapper.map(m,JournalEntryModel.class)).
                    collect(Collectors.toList());
        }catch(Exception ex){
            System.out.println("Exception in getAll from DB");
            return null;
        }
    }

    public List<JournalEntryModel> getAllEntriesOfUser(String userName){
        UserEntityModel userEntityModel = null;
        List<JournalEntryModel> liEntries = null;
        try{
            System.out.println("Looking entries for user:"+userName);
            userEntityModel = userService.getUser(userName);
            if(userEntityModel!=null){
                System.out.println("User :"+userName+" Found.");
                return liEntries = userEntityModel.getJournalEntries();
            }else{
                return liEntries;
            }
        }catch(Exception ex){
            System.out.println("Exception in getAll from DB");
            return liEntries;
        }
    }

    public JournalEntryModel getEntry(ObjectId entryID){
        try{
            Optional<JournalEntry> optional = journalRepo.findById(entryID);
            if(optional.isPresent()){
                return mapper.map(optional.get(),JournalEntryModel.class);
            }else{
                return null;
            }
        }catch(Exception ex){
            System.out.println("Exception in getting model from DB");
            return null;
        }
    }

    public JournalEntryModel removeEntry(ObjectId entryID){
        try{
            Optional<JournalEntry> optional = journalRepo.findById(entryID);
            if(optional.isPresent()){
                journalRepo.delete(optional.get());
                return mapper.map(optional.get(),JournalEntryModel.class);
            }else{
                return null;
            }
        }catch(Exception ex){
            System.out.println("Exception in getting model from DB");
            return null;
        }
    }

    public JournalEntryModel removeUserEntry(String userName,ObjectId entryID){
        UserEntityModel userEntityModel = null;
        try{
            userEntityModel = userService.getUser(userName);
            if (userEntityModel!=null){
                System.out.println("User found from DB.");
                List<JournalEntryModel> journalEntryModelList
                        = userEntityModel.getJournalEntries();
                if(journalEntryModelList!=null &&
                        !journalEntryModelList.isEmpty()){
                    journalEntryModelList.removeIf(e -> e.getId().equals(entryID));
                    userService.saveEntry(userEntityModel);
                    System.out.println("User entry removed."+userEntityModel.toString());
                    Optional<JournalEntry> optional = journalRepo.findById(entryID);
                    if(optional.isPresent()){
                        journalRepo.delete(optional.get());
                        System.out.println("Entry removed.");
                        return mapper.map(optional.get(),JournalEntryModel.class);
                    }else{
                        return null;
                    }
                }else{
                    System.out.println("Journal list empty for user");
                    return null;
                }
            }else{
                System.out.println("Error in saving data into DB.");
                return  null;
            }
        }catch(Exception ex){
            System.out.println("Exception in getting model from DB");
            return null;
        }
    }

}
