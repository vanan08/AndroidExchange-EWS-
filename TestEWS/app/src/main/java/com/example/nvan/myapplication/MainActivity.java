package com.example.nvan.myapplication;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URI;
import java.util.regex.Pattern;

import microsoft.exchange.webservices.data.BasePropertySet;
import microsoft.exchange.webservices.data.CalendarFolder;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;
import microsoft.exchange.webservices.data.FindFoldersResults;
import microsoft.exchange.webservices.data.Folder;
import microsoft.exchange.webservices.data.FolderSchema;
import microsoft.exchange.webservices.data.FolderTraversal;
import microsoft.exchange.webservices.data.FolderView;
import microsoft.exchange.webservices.data.MessageBody;
import microsoft.exchange.webservices.data.PropertySet;
import microsoft.exchange.webservices.data.SearchFilter;
import microsoft.exchange.webservices.data.WebCredentials;
import microsoft.exchange.webservices.data.WellKnownFolderName;

import static microsoft.exchange.webservices.data.FolderSchema.DisplayName;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = (Button)findViewById(R.id.email_sign_in_button);
        final AutoCompleteTextView username = (AutoCompleteTextView)findViewById(R.id.fromEmail);
        final EditText password = (EditText)findViewById(R.id.password);
        final AutoCompleteTextView toEmail = (AutoCompleteTextView)findViewById(R.id.toEmail);
        final AutoCompleteTextView subject = (AutoCompleteTextView)findViewById(R.id.subject);
        final EditText body = (EditText)findViewById(R.id.body);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String _user = username.getText().toString();
                    String domain = _user.split(Pattern.quote("\\"))[0];
                    String _username = _user.split(Pattern.quote("\\"))[1];
                    Log.d("test", "Domain: "+domain);
                    Log.d("test", "_username: "+_username);

                    String _password = password.getText().toString();
                    Log.d("test", "_password: "+_password);
                    String _toEmail = toEmail.getText().toString();
                    String _subject = subject.getText().toString();
                    String _body = body.getText().toString();
                    ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2007_SP1);
                    ExchangeCredentials credentials = new WebCredentials(_username, _password, domain);
                    service.setCredentials(credentials);
                    service.setUrl(new URI("https://webmail.cmc.com.vn/EWS/Exchange.asmx"));
                    EmailMessage message = new EmailMessage(service);

                    message.setSubject(_subject);
                    message.setBody(new MessageBody(_body));
                    message.getToRecipients().add(_toEmail);


                    message.sendAndSaveCopy();
                    Toast.makeText(MainActivity.this, "Email sent", Toast.LENGTH_LONG).show();
                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("EWS", ex.getMessage(), ex);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
