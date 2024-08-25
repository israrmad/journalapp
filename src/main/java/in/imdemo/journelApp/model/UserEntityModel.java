package in.imdemo.journelApp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UserEntityModel {

    private ObjectId id;
    private String userName;
    private String passWord;

    private List<JournalEntryModel> journalEntries = new ArrayList<>();
    private List<String> roles = new ArrayList<>();
}
