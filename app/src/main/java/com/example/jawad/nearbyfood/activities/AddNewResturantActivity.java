package com.example.jawad.nearbyfood.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jawad.nearbyfood.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewResturantActivity extends AppCompatActivity {


    @BindView(R.id.recyclerView_foodImages)
    RecyclerView recyclerView_foodImages;

    @BindView(R.id.recyclerView_menuImages)
    RecyclerView recyclerView_menuImages;

    @BindView(R.id.editText_resturantName)
    EditText editText_resturantName;

    @BindView(R.id.editText_resturantAddress)
    EditText editText_resturantAddress;

    @BindView(R.id.editText_resturantLocation)
    EditText editText_resturantLocation;

    @BindView(R.id.editText_resturantPhoneNumber)
    EditText editText_resturantPhoneNumber;

    @BindView(R.id.editText_resturantCuisineCategories)
    EditText editText_resturantCuisineChoose;

    @BindView(R.id.editText_resturantQuickSearchCategories)
    EditText editText_resturantQuickSearchChoose;
    final boolean checked_state[] = {false, false, false}; //The array that holds the checked state of the checkbox items
    final CharSequence[] day_check = {"Sunday", "Monday", "Tuesday"}; //items in the alertdialog that displays checkboxes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_resturant);
        ButterKnife.bind(this);

        editText_resturantCuisineChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddNewResturantActivity.this)
                        .setTitle("Choose a Days")
                        .setMultiChoiceItems(day_check, checked_state, new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
// TODO Auto-generated method stub

//storing the checked state of the items in an array
//                                checked_state[which] = isChecked;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                                String display_checked_days = "";
                                for (int i = 0; i < 3; i++) {
                                    if (checked_state[i] == true) {
                                        display_checked_days = display_checked_days + " " + day_check[i];
                                    }
                                }
                                Toast.makeText(AddNewResturantActivity.this, "The selected day(s) is" + display_checked_days, Toast.LENGTH_LONG).show();

//clears the String used to store the displayed text
                                display_checked_days = null;

////clears the array used to store checked state
//                                for (int i = 0; i < checked_state.length; i++) {
//                                    if (checked_state[i] == true) {
//                                        checked_state[i] = false;
//                                    }
//                                }

//used to dismiss the dialog upon user selection.
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertdialog1 = builder1.create();
                alertdialog1.show();
            }
        });
    }
}
