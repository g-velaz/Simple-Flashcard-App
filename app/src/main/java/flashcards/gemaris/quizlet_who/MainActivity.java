package flashcards.gemaris.quizlet_who;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    FlashcardDatabase flashcardDatabase; //Can be used in all methods of MainActivity
    List<Flashcard> allFlashcards; //Holds a list of Flashcards
    int currentCardDisplayedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.flashcardQuestion)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.flashcardAnswer)).setText(allFlashcards.get(0).getAnswer());
        }

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

        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentCardDisplayedIndex == -1000){
                    currentCardDisplayedIndex = -1000;
                }//once everything is deleted the button is rendered unusable
                else {
                    // advance our pointer index so we can show the next card
                    currentCardDisplayedIndex++;

                    // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                    if (currentCardDisplayedIndex > allFlashcards.size() - 1) {
                        currentCardDisplayedIndex = 0;
                    }//if last card is removed go to "next" card which is at 0
                    else if ((currentCardDisplayedIndex == 0) && (currentCardDisplayedIndex == allFlashcards.size() - 1)) {
                        currentCardDisplayedIndex = 0;
                    }//for singular card only
                    // set the question and answer TextViews with data from the database
                    ((TextView) findViewById(R.id.flashcardQuestion)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                    ((TextView) findViewById(R.id.flashcardAnswer)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());

                    findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                    findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);
                }
            }
        });

        findViewById(R.id.prevButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentCardDisplayedIndex == -1000){
                    currentCardDisplayedIndex = -1000;
                } //once everything is deleted the button is rendered unusable

                else {
                    // retract our pointer index so we can show the next card
                    currentCardDisplayedIndex--;

                    // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                    if (currentCardDisplayedIndex < 0) {
                        currentCardDisplayedIndex = (allFlashcards.size() - 1);
                    }

                    // set the question and answer TextViews with data from the database
                    ((TextView) findViewById(R.id.flashcardQuestion)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                    ((TextView) findViewById(R.id.flashcardAnswer)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());

                    findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                    findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);
                }
            }
        });
        findViewById(R.id.randomButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentCardDisplayedIndex == -1000){
                    currentCardDisplayedIndex = -1000;
                }//once everything is deleted the button is rendered unusable
                else {
                    // advance our pointer index so we can show the next card
                    currentCardDisplayedIndex = getRandomNumber(0, allFlashcards.size() - 1);

                    // set the question and answer TextViews with data from the database
                    ((TextView) findViewById(R.id.flashcardQuestion)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                    ((TextView) findViewById(R.id.flashcardAnswer)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());

                    findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                    findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);
                }
            }
        });

        findViewById(R.id.removeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((currentCardDisplayedIndex == 0)&&(currentCardDisplayedIndex == allFlashcards.size() - 1)){
                    currentCardDisplayedIndex = -1000;
                }//for last card to be deleted

                flashcardDatabase.deleteCard(((TextView) findViewById(R.id.flashcardQuestion)).getText().toString());
                allFlashcards = flashcardDatabase.getAllCards(); //ONE LESS ARRAY ELEMENT
                if (currentCardDisplayedIndex < 0) {
                    ((TextView) findViewById(R.id.flashcardQuestion)).setText("You have no more flashcards.");
                    ((TextView) findViewById(R.id.flashcardAnswer)).setText("Press the + button to begin adding flashcards.");
                }
                else {
                    if (currentCardDisplayedIndex > allFlashcards.size() - 1) {
                        currentCardDisplayedIndex = 0;
                    }
                    ((TextView) findViewById(R.id.flashcardQuestion)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                    ((TextView) findViewById(R.id.flashcardAnswer)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());

                    findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                    findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);
                }
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
                findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);

                flashcardDatabase.insertCard(new Flashcard(strQ, strAns)); //Saves the flashcard
                allFlashcards = flashcardDatabase.getAllCards(); //NEW ARRAY ELEMENT
                currentCardDisplayedIndex = (allFlashcards.size() - 1); //Since new card is added to end of array, index reflects that

            }
        }
    }
    // returns a random number between minNumber and maxNumber, inclusive.
    // for example, if i called getRandomNumber(1, 3), there's an equal chance of it returning either 1, 2, or 3.
    public int getRandomNumber(int minNumber, int maxNumber) {
        Random rand = new Random();
        return rand.nextInt((maxNumber - minNumber) + 1) + minNumber;
    }
}