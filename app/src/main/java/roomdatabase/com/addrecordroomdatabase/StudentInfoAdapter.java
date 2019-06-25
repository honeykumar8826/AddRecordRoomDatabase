package roomdatabase.com.addrecordroomdatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentInfoAdapter extends RecyclerView.Adapter<StudentInfoAdapter.ViewHolder> {
    private static final int PICK_IMG =1 ;
    private final String TAG = "abc";
    private final List<StudentInfoModal> modalList;
    private Context context;
    private Uri imageUri;

    public StudentInfoAdapter(List<StudentInfoModal> modalList) {
        this.modalList = modalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.i(TAG, "onCreateViewHolder: ");
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_user_record_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.i(TAG, "onBindViewHolder: ");
        StudentInfoModal studentInfoModalList = modalList.get(i);
        Log.i("onBindViewHolder", "onBindViewHolder: " + studentInfoModalList);
        viewHolder.tvUserName.setText(studentInfoModalList.getUserName());
        viewHolder.tvUserAge.setText(studentInfoModalList.getUserAge());
        viewHolder.tvUserGender.setText(studentInfoModalList.getUserGender());
        Glide.with(context).load(Uri.parse(studentInfoModalList.getImageUri())).into(viewHolder.userImage);
        //viewHolder.userImage.setImageURI(Uri.parse(studentInfoModalList.getImageUri()));
        final int recordId = studentInfoModalList.getDataId();
        viewHolder.deleteRecordImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setId(recordId);
                MainActivity.myAppDatabase.myDao().deleteUser(user);
                Toast.makeText(context, "Record Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.editRecordData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                final View alertLayout = inflater.inflate(R.layout.add_data_layout, null);
                final EditText etUsername = alertLayout.findViewById(R.id.et_name);
                final EditText etAge = alertLayout.findViewById(R.id.et_age);
                final RadioGroup rgGender = alertLayout.findViewById(R.id.radioGroup);
                final Button btnCancel = alertLayout.findViewById(R.id.btn_cancel);
                final Button btnOk = alertLayout.findViewById(R.id.btn_ok);
                final Button imgeUpload = alertLayout.findViewById(R.id.btn_upload_img);
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                imgeUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        ((Activity) context).startActivityForResult(intent, PICK_IMG);
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
                        user.setId(recordId);
                        user.setName(name);
                        user.setAge(age);
                        user.setGender(gender);
                        //user.setUserImage(imageUri.toString());
                        user.setUserImage("");
                        MainActivity.myAppDatabase.myDao().updateUserInfo(user);
                        Toast.makeText(context, "data Updated Successfully", Toast.LENGTH_SHORT).show();
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
                alert.show(); }
        });
    }

    @Override
    public int getItemCount() {
//        Log.i(TAG, "getItemCount: " + modalList.size());

        return modalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView tvUserName, tvUserAge,tvUserGender;
         ImageView deleteRecordImg,editRecordData;//,userImage;
         CircleImageView userImage;



        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_name_add);
            tvUserAge = itemView.findViewById(R.id.tv_age_add);
            tvUserGender = itemView.findViewById(R.id.tv_gender_add);
            deleteRecordImg = itemView.findViewById(R.id.img_close);
            userImage = itemView.findViewById(R.id.user_img);
            editRecordData = itemView.findViewById(R.id.img_edit);

        }
    }
/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                imageUri = data.getData();
            }
        }
    }*/
}