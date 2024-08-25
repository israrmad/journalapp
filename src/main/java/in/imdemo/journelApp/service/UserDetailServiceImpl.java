package in.imdemo.journelApp.service;

import in.imdemo.journelApp.entity.UserEntity;
import in.imdemo.journelApp.repo.UserEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailServiceImpl implements UserDetailsService {


    @Autowired
    private UserEntryRepository userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByUserName(username).get();
        if(user!=null){
            return (UserDetails) User.builder().
                    username(user.getUserName()).
                    password(user.getPassWord()).
                    roles(user.getRoles().toArray(new String[0])).
                    build();
        }else{
            return null;
        }
    }
}
