package roomdatabase.com.addrecordroomdatabase;

public class StudentInfoModal {
    private String userName;
    private String userAge;
    private String userGender;
    private int dataId;

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    private String imageUri;

    public StudentInfoModal(String userName, String userAge, String userGender, int dataId,String imageUri) {
        this.userName = userName;
        this.userAge = userAge;
        this.userGender = userGender;
        this.dataId = dataId;
        this.imageUri = imageUri;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

}
