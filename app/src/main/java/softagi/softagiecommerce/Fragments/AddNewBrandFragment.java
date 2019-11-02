package softagi.softagiecommerce.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import softagi.softagiecommerce.LoginActivity;
import softagi.softagiecommerce.Models.BrandModel;
import softagi.softagiecommerce.R;

public class AddNewBrandFragment extends Fragment
{
    View view;
    private CircleImageView brandImg;
    private Button saveBrandBtn;
    private EditText nameField;
    private EditText categoryField;
    private Uri photoPath;
    private String name,categ;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_new_brand_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initView();
        initDialog();
        chooseImg();
        save();
    }

    private void initDialog()
    {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("please wait ...");
        progressDialog.setCancelable(false);
    }

    private void save()
    {
        saveBrandBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                name = nameField.getText().toString();
                categ = categoryField.getText().toString();

                if (name.isEmpty())
                {
                    Toast.makeText(getContext(), "enter brand name", Toast.LENGTH_SHORT).show();
                    nameField.requestFocus();
                    return;
                }

                if (categ.isEmpty())
                {
                    Toast.makeText(getContext(), "enter brand category", Toast.LENGTH_SHORT).show();
                    categoryField.requestFocus();
                    return;
                }

                if (photoPath == null)
                {
                    Toast.makeText(getContext(), "select brand image", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.show();
                upload(name,categ);
            }
        });
    }

    private void upload(final String name, final String categ)
    {
        UploadTask uploadTask;
        final StorageReference ref = storageReference.child("brands/" + photoPath.getLastPathSegment());
        uploadTask = ref.putFile(photoPath);

        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
            {
                if (!task.isSuccessful())
                {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>()
        {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                Uri downloadUri = task.getResult();
                String selectedimageurl = downloadUri.toString();
                addData(name,categ,selectedimageurl);
            }
        });
    }

    private void addData(String name, String categ, String selectedimageurl)
    {
        BrandModel brandModel = new BrandModel(name,categ,selectedimageurl);
        String key = databaseReference.child("Brands").push().getKey();
        databaseReference.child("Brands").child(key).setValue(brandModel);
        progressDialog.dismiss();
    }

    private void chooseImg()
    {
        brandImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setAspectRatio(1,1)
                        .start(Objects.requireNonNull(getContext()), AddNewBrandFragment.this);

            }
        });
    }

    private void initView()
    {
        brandImg = view.findViewById(R.id.brand_img);
        saveBrandBtn = view.findViewById(R.id.save_brand_btn);
        nameField = view.findViewById(R.id.name_field);
        categoryField = view.findViewById(R.id.category_field);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("images");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == Activity.RESULT_OK)
            {
                if (result != null)
                {
                    photoPath = result.getUri();

                    Picasso.get()
                            .load(photoPath)
                            .placeholder(R.drawable.adidas)
                            .error(R.drawable.adidas)
                            .into(brandImg);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }
}
