package flashcards.gemaris.quizlet_who;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        //Create button that connects main activity with add card activity
        //This one leads back to the main from add card once cancelled
        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish(); //dismisses the current activity
            }
        });

        //Launch add card activity from main activity
        //User input is recorded in add card

        //Set OC listener for save
        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener(){
            //@Override
            public void onClick(View v) {
                //Take edit text as strings
                EditText newQ = findViewById(R.id.getQ);
                EditText newAns = findViewById(R.id.getAns);
                //Put strings in an intent
                Intent data = new Intent();
                data.putExtra("newQuestion", newQ.getText().toString()); //new question goes in intent
                data.putExtra("newAnswer", newAns.getText().toString()); //new answer goes in intent
                setResult(RESULT_OK, data);
                finish();
            }
        });


    }
}
