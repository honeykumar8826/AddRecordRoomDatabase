package roomdatabase.com.addrecordroomdatabase;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMG =1 ;
    static  MyAppDatabase myAppDatabase;
    private TextView textViewUserData;
    private StudentInfoAdapter studentInfoAdapter;
    private RecyclerView recyclerView;
    private List<StudentInfoModal> listItem;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_add_data);
        textViewUserData = findViewById(R.id.tv_whole_data);
        myAppDatabase = Room.databaseBuilder(this, MyAppDatabase.class, "userdb").allowMainThreadQueries().build();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItem = new ArrayList<>();
        studentInfoAdapter = new StudentInfoAdapter(listItem);
        recyclerView.setAdapter(studentInfoAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.referesh:
                listItem.clear();
                List<User> users = myAppDatabase.myDao().getUser();
                String userInfo;
                for (User user : users) {
                    String name = user.getName();
                    String age = user.getAge();
                    String gender = user.getGender();
                    String image_uri = user.getUserImage();
                    int id = user.getId();

                    userInfo = "\n\n" +"\n" + "name:" + name + "\n" + "email:" + age;
                    StudentInfoModal studentInfoModal = new StudentInfoModal(name,age,gender,id,image_uri);
//                    textViewUserData.append(userInfo);
                    listItem.add(studentInfoModal);
                }
                studentInfoAdapter.notifyDataSetChanged();
                Toast.makeText(this, "referesh", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_data:
                LayoutInflater inflater = getLayoutInflater();
                final View alertLayout = inflater.inflate(R.layout.add_data_layout, null);
                final EditText etUsername = alertLayout.findViewById(R.id.et_name);
                final EditText etAge = alertLayout.findViewById(R.id.et_age);
                final RadioGroup rgGender = alertLayout.findViewById(R.id.radioGroup);
                final Button btnCancel = alertLayout.findViewById(R.id.btn_cancel);
                final Button btnOk = alertLayout.findViewById(R.id.btn_ok);
                final Button imgeUpload = alertLayout.findViewById(R.id.btn_upload_img);
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                imgeUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, PICK_IMG);
                    }
                });
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = etUsername.getText().toString();
                        String age = etAge.getText().toString();
                        int selectedId = rgGender.getCheckedRadioButtonId();
                        RadioButton genderradioButton = alertLayout.findViewById(selectedId);
                        String gender = genderradioButton.getText().toString();
                        User user = new User();
                        user.setName(name);
                        user.setAge(age);
                        user.setGender(gender);
                        user.setUserImage(imageUri.toString());
                        MainActivity.myAppDatabase.myDao().addUser(user);
                        Toast.makeText(MainActivity.this, "data Stored Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.setCancelable(true);
                    }
                });
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                alert.show();

                Toast.makeText(this, "add data", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Do Right Selection", Toast.LENGTH_SHORT).show();
                return true;
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                imageUri = data.getData();
            }
        }
    }
}