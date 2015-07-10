package utils;

import android.net.Uri;

/**
 * Created by gili on 7/9/2015.
 */
public class User {
    private String FirstName;
    private String LastName;
    private String Name;
    private Uri ProfileImage;

    private static User sUser = null;

    private User(){

    }

    public static User getUserInstance(){
        if(sUser == null){
            sUser = new User();
        }
        return sUser;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Uri getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(Uri profileImage) {
        ProfileImage = profileImage;
    }


}
