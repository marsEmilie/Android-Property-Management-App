package com.example.emilie.property_management_5;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ViewTenant extends AppCompatActivity {


    private EditText etNameSendEmail, etSubjectSendEmail, etMessageSendEmail;
    private TextView etName,etGender, etContact,etEmail,etPaymentDate;
    private Button buttonSendEmail, buttonSendEmailOnViewPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tenant);

        Intent intent = getIntent();
        etName = (TextView)findViewById(R.id.name_et_tenant);
        etGender = (TextView)findViewById(R.id.genderTenant_et);
        etContact = (TextView)findViewById(R.id.contactTenant_et);
        etEmail = (TextView)findViewById(R.id.emailTenant_et);
        etPaymentDate = (TextView)findViewById(R.id.Payment_Date_et);
        buttonSendEmailOnViewPage = findViewById(R.id.buttonSendEmailOnViewPage);

        etName.setText(intent.getStringExtra(TenantHomePage.TENANT_NAME));
        etGender.setText(intent.getStringExtra(TenantHomePage.TENANT_GENDER));
        etContact.setText(intent.getStringExtra(TenantHomePage.TENANT_CONTACT));
        etEmail.setText(intent.getStringExtra(TenantHomePage.TENANT_EMAIL));
        etPaymentDate.setText(intent.getStringExtra(TenantHomePage.TENANT_PAYMENT_DATE));

        final String name = etName.getText().toString();
        final String gender = etGender.getText().toString();
        final String payment = etPaymentDate.getText().toString();
        final String email = etEmail.getText().toString();
        buttonSendEmailOnViewPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail(name,gender,email,payment);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("VIEW TENANT");

    }

    private void sendEmail(String name, String gender, String email, String paymentDate){
        final AlertDialog.Builder sendPopUp = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View sendPopUpView = inflater.inflate(R.layout.dialog_send_email,null);
        sendPopUp.setView(sendPopUpView);

        //final AlertDialog a = sendPopUp
        sendPopUp.create().show();

        etNameSendEmail = sendPopUpView.findViewById(R.id.edit_text_to);
        etSubjectSendEmail = sendPopUpView.findViewById(R.id.edit_text_subject);
        etMessageSendEmail = sendPopUpView.findViewById(R.id.edit_text_message);
        buttonSendEmail = sendPopUpView.findViewById(R.id.button_sendEmail);
        String salutation ;

        if(gender.equals("Male")){
            salutation = "Mr.";
        }else{
            salutation = "Mrs.";
        }
        String titleConfigure = "Please kindly pay your rental =)";
        String bodyConfigure = salutation + name + ", your payment date is " + paymentDate +
                "," + "\n" + "please kindly make your payment, thank-you for cooperation. ";
        etNameSendEmail.setText(email);
        etSubjectSendEmail.setText(titleConfigure);
        etMessageSendEmail.setText(bodyConfigure);

        final String[] tenantName = email.split(",");
        final String subject = etSubjectSendEmail.getText().toString();
        final String message = etMessageSendEmail.getText().toString();

        buttonSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openEmail = new Intent(Intent.ACTION_SEND);
                openEmail.putExtra(Intent.EXTRA_EMAIL, tenantName);
                openEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
                openEmail.putExtra(Intent.EXTRA_TEXT, message);
                openEmail.setType("message/rfc822");
                startActivity(Intent.createChooser(openEmail, "Select an application"));
            }
        });
    }
}


