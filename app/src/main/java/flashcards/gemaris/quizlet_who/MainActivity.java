package flashcards.gemaris.quizlet_who;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.flashcardQuestion).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                findViewById(R.id.flashcardQuestion).setVisibility(View.INVISIBLE);
                findViewById(R.id.flashcardAnswer).setVisibility(View.VISIBLE);
            }
        }); //Note to self: place the findViewById in onCreate, not in the MainActivity class

        findViewById(R.id.flashcardAnswer).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);
            }
        });

        //Create button that connects main activity with add card activity
        //We're going from Main Activity to Add Card Activity so the button should be placed in main
        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Allows me to navigate from main to add card
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                intent.putExtra("nameQ", "question");
                intent.putExtra("nameA", "answer");
                MainActivity.this.startActivityForResult(intent, 100);
            }

        });
    }
    //onActivityResult is its own thing! That doesn't go in onCreate
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 100 && resultCode == RESULT_OK){
            if(data.hasExtra("newQuestion")) {
                String strQ = data.getExtras().getString("newQuestion");
                String strAns = data.getExtras().getString("newAnswer");
                //Is the string empty for the question?
                if (TextUtils.isEmpty(strQ)){//This is for having an empty string and clicking save
                    ((TextView) findViewById(R.id.flashcardQuestion)).setText("Insert a question here"); //If blank and saved then error
                    ((TextView) findViewById(R.id.flashcardQuestion)).setTextColor( //Changes the color to distinguish the two
                            getResources().getColor(R.color.blankResponse));
                }
                else {
                    ((TextView) findViewById(R.id.flashcardQuestion)).setText(strQ);
                    ((TextView) findViewById(R.id.flashcardQuestion)).setTextColor(
                            getResources().getColor(R.color.colorQuestion)); //Maintains the old color

                }
                //Is the string empty for the answer?
                if (TextUtils.isEmpty(strAns)){
                    ((TextView) findViewById(R.id.flashcardAnswer)).setText("Insert an answer here");
                    ((TextView) findViewById(R.id.flashcardAnswer)).setTextColor(
                            getResources().getColor(R.color.blankResponse));
                }
                else {
                    ((TextView) findViewById(R.id.flashcardAnswer)).setText(strAns);
                    ((TextView) findViewById(R.id.flashcardAnswer)).setTextColor(
                            getResources().getColor(R.color.colorAnswer));
                }

            }
        }
    }
}