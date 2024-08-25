package in.imdemo.journelApp.service;

import in.imdemo.journelApp.entity.UserEntity;
import in.imdemo.journelApp.model.UserEntityModel;
import in.imdemo.journelApp.repo.UserEntryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserService {

    @Autowired
    UserEntryRepository userRepo;

    @Autowired
    @Qualifier("modelMapper")
    ModelMapper mapper;

    private static final PasswordEncoder passWordEncoder = new
            BCryptPasswordEncoder();

    public UserEntityModel saveEntry(UserEntityModel model){
        try{
            UserEntity userEntry = mapper.map(model,UserEntity.class);
            System.out.println("Entry to be saved:"+userEntry.toString());
            if(userEntry!=null){
                userRepo.save(userEntry);
                return model;
            }else{
                System.out.println("Error in saving data into DB.");
                return null;
            }
        }catch(Exception ex){
            System.out.println("Exception in saving entry in DB"+ex.getMessage());
            return null;
        }
    }

    public UserEntityModel updateEntry(UserEntityModel model, String userName){
        try{
            Optional<UserEntity> optional =
                    userRepo.findByUserName((userName!=null && !userName.isEmpty())?
                            userName : model.getUserName());
            if(optional.isPresent()){
                UserEntity userEntry = optional.get();
                userEntry.setPassWord(model.getPassWord()!=null && !model.getPassWord().equals("")?
                        passWordEncoder.encode(model.getPassWord()) :
                        passWordEncoder.encode(userEntry.getPassWord()));
                List<String> roles = userEntry.getRoles()!=null?
                        userEntry.getRoles(): Arrays.asList(new String[0]);
                model.getRoles().forEach(x -> {
                    if(!roles.contains(x)){
                        roles.add(x);
                    }
                });
                userEntry.setRoles(roles);
                userEntry = userRepo.save(userEntry);
                return mapper.map(userEntry,UserEntityModel.class);
            }else{
                return null;
            }
        }catch(Exception ex){
            System.out.println("Exception in updateEntry from DB");
            return null;
        }
    }

    public List<UserEntityModel> getAll(){
        try{
            return userRepo.findAll().stream().
                    map(m -> mapper.map(m,UserEntityModel.class)).
                    collect(Collectors.toList());
        }catch(Exception ex){
            System.out.println("Exception in getAll from DB");
            return null;
        }
    }

    public UserEntityModel getUser(String userName){
        try{
            Optional<UserEntity> optional = userRepo.findByUserName(userName);
            if(optional.isPresent()){
                return mapper.map(optional.get(),UserEntityModel.class);
            }else{
                return null;
            }
        }catch(Exception ex){
            System.out.println("Exception in getting model from DB");
            return null;
        }
    }

    public UserEntityModel removeUser(String userName){
        try{
            Optional<UserEntity> optional = userRepo.findByUserName(userName);
            if(optional.isPresent()){
                userRepo.delete(optional.get());
                return mapper.map(optional.get(),UserEntityModel.class);
            }else{
                return null;
            }
        }catch(Exception ex){
            System.out.println("Exception in getting model from DB");
            return null;
        }
    }

}
