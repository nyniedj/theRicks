package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.models.Privilege;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.models.UserTable;
import edu.gatech.cs2340.thericks.utils.Security;

/**
 * Created by Ben Lashley on 9/19/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEntry;
    private EditText passwordEntry;
    private EditText passwordReentry;
    private RadioGroup privilege;

    private TextView invalidUsername;
    private TextView invalidPassword;
    private TextView passwordMismatch;

    private Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usernameEntry   = (EditText) findViewById(R.id.registration_create_username_entry);
        passwordEntry   = (EditText) findViewById(R.id.registration_create_password_entry);
        passwordReentry = (EditText) findViewById(R.id.registration_reenter_password_entry);

        privilege = (RadioGroup) findViewById(R.id.register_radio_group);

        createAccount = (Button)   findViewById(R.id.register_button);

        invalidUsername  = (TextView) findViewById(R.id.registration_invalid_username);
        invalidPassword  = (TextView) findViewById(R.id.registration_invalid_password);
        passwordMismatch = (TextView) findViewById(R.id.registration_password_mismatch);

        // Hide warning messages
        invalidUsername.setVisibility(View.GONE);
        invalidPassword.setVisibility(View.GONE);
        passwordMismatch.setVisibility(View.GONE);

        createAccount.setOnClickListener(v -> {
            String username = usernameEntry.getText().toString();
            String password = passwordEntry.getText().toString();
            String reenteredPassword = passwordReentry.getText().toString();
            Privilege user_privilege = null;
            boolean usernameTaken = (UserTable.getInstance().getUserByUsername(username) != null);
            boolean validEntries = true;

            if (!Security.validateUsername(username) || usernameTaken) {
                invalidUsername.setVisibility(View.VISIBLE);
                validEntries = false;
            } else {
                invalidUsername.setVisibility(View.GONE);
            }

            if (!Security.validatePassword(password)) {
                invalidPassword.setVisibility(View.VISIBLE);
                validEntries = false;
            } else {
                invalidPassword.setVisibility(View.GONE);
            }

            if (!password.equals(reenteredPassword)) {
                passwordMismatch.setVisibility(View.VISIBLE);
                validEntries = false;
            } else {
                passwordMismatch.setVisibility(View.GONE);
            }

            if (privilege.getCheckedRadioButtonId() == R.id.admin_radio) {
                user_privilege = Privilege.ADMIN;
            } else if (privilege.getCheckedRadioButtonId() == R.id.normal_radio) {
                user_privilege = Privilege.NORMAL;
            } else {
                // No privilege button selected
                validEntries = false;
            }

            if (validEntries) {
                User u = new User(username, password, user_privilege);
                UserTable.getInstance().addUser(u);

                Context context = v.getContext();
                Intent intent = new Intent(context, DashboardActivity.class);
                intent.putExtra("edu.gatech.cs2340.thericks.User", u);
                context.startActivity(intent);
                finish();
            }
        });
    }
}
