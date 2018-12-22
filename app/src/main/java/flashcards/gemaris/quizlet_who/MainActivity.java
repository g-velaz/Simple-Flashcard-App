package flashcards.gemaris.quizlet_who;

import android.animation.Animator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    FlashcardDatabase flashcardDatabase; //Can be used in all methods of MainActivity
    List<Flashcard> allFlashcards; //Holds a list of Flashcards
    int currentCardDisplayedIndex = -1000; //So it does not move left and right
    boolean isQCardVisible = true; //Checks for which side of the card is visible

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.flashcardQuestion)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.flashcardAnswer)).setText(allFlashcards.get(0).getAnswer());
            currentCardDisplayedIndex = 0; //If cards exist then index must be set to beginning of stack
        }

        findViewById(R.id.flashcardQuestion).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                isQCardVisible = false;

                findViewById(R.id.flashcardQuestion).animate()
                        .rotationY(90)
                        .setDuration(200)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.flashcardQuestion).setVisibility(View.INVISIBLE);
                                        findViewById(R.id.flashcardAnswer).setVisibility(View.VISIBLE);
                                        // second quarter turn SECOND PART APPEARS
                                        findViewById(R.id.flashcardAnswer).setRotationY(-90);
                                        findViewById(R.id.flashcardQuestion).setCameraDistance(250000);
                                        findViewById(R.id.flashcardAnswer).setCameraDistance(250000);
                                        findViewById(R.id.flashcardAnswer).animate()
                                                .rotationY(0)
                                                .setDuration(200)
                                                .start();
                                    }
                                }
                        ).start();
            }
        }); //Note to self: place the findViewById in onCreate, not in the MainActivity class



        findViewById(R.id.flashcardAnswer).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                isQCardVisible = true;

                findViewById(R.id.flashcardAnswer).animate()
                        .rotationY(90)
                        .setDuration(200)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);
                                        findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                                        // second quarter turn SECOND PART APPEARS
                                        findViewById(R.id.flashcardQuestion).setRotationY(-90);
                                        findViewById(R.id.flashcardAnswer).setCameraDistance(250000);
                                        findViewById(R.id.flashcardQuestion).setCameraDistance(250000);
                                        findViewById(R.id.flashcardQuestion).animate()
                                                .rotationY(0)
                                                .setDuration(200)
                                                .start();
                                    }
                                }
                        ).start();
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
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }

        });

        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);

                if(currentCardDisplayedIndex == -1000){
                    currentCardDisplayedIndex = -1000;
                }//once everything is deleted the button is rendered unusable
                else {

                    // advance our pointer index so we can show the next card
                    currentCardDisplayedIndex++;

                    leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            // this method is called when the animation first starts
                            // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                            if (currentCardDisplayedIndex > allFlashcards.size() - 1) {
                                currentCardDisplayedIndex = 0;
                            }//if last card is removed go to "next" card which is at 0
                            else if ((currentCardDisplayedIndex == 0) && (currentCardDisplayedIndex == allFlashcards.size() - 1)) {
                                currentCardDisplayedIndex = 0;
                            }//for singular card only

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // this method is called when the animation is finished playing
                            // set the question and answer TextViews with data from the database
                            ((TextView) findViewById(R.id.flashcardQuestion)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                            ((TextView) findViewById(R.id.flashcardAnswer)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
                            findViewById(R.id.flashcardQuestion).startAnimation(rightInAnim);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            // we don't need to worry about this method
                        }
                    });

                    if(isQCardVisible == true)
                        findViewById(R.id.flashcardQuestion).startAnimation(leftOutAnim);
                    if(isQCardVisible == false) {
                        findViewById(R.id.flashcardAnswer).startAnimation(leftOutAnim);
                        isQCardVisible = true;
                    }


                    rightInAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            // this method is called when the animation first starts
                            findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                            findViewById(R.id.flashcardQuestion).setRotationY(0);
                            findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // this method is called when the animation is finished playing

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            // we don't need to worry about this method
                        }
                    });


                }
            }
        });



        findViewById(R.id.prevButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final Animation leftInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_in);
                    final Animation rightOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_out);

                    if(currentCardDisplayedIndex == -1000){
                        currentCardDisplayedIndex = -1000;
                    }//once everything is deleted the button is rendered unusable
                    else {

                        // advance our pointer index so we can show the next card
                        currentCardDisplayedIndex--;

                        leftInAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                // this method is called when the animation first starts
                                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                                if (currentCardDisplayedIndex < 0) {
                                    currentCardDisplayedIndex = (allFlashcards.size() - 1);
                                }
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                // this method is called when the animation is finished playing
                                // set the question and answer TextViews with data from the database
                                ((TextView) findViewById(R.id.flashcardQuestion)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                                ((TextView) findViewById(R.id.flashcardAnswer)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
                                findViewById(R.id.flashcardQuestion).startAnimation(rightOutAnim);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                // we don't need to worry about this method
                            }
                        });

                        if(isQCardVisible == true)
                            findViewById(R.id.flashcardQuestion).startAnimation(leftInAnim);
                        if(isQCardVisible == false) {
                            findViewById(R.id.flashcardAnswer).startAnimation(leftInAnim);
                            isQCardVisible = true; //once the next card rolls in this is true
                        }


                        rightOutAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                // this method is called when the animation first starts
                                findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                                findViewById(R.id.flashcardQuestion).setRotationY(0);
                                findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                // this method is called when the animation is finished playing


                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                // we don't need to worry about this method
                            }
                        });

                    }
            }
        });
        findViewById(R.id.randomButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isQCardVisible = true;
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
                    findViewById(R.id.flashcardQuestion).setRotationY(0);
                    findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);
                }
            }
        });

        findViewById(R.id.removeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Animation UpAndOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.up_n_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);


              /*
                    findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                    findViewById(R.id.flashcardQuestion).setRotationY(0);
                    findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);*/


                    UpAndOutAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            // this method is called when the animation first starts
                            if((currentCardDisplayedIndex == 0)&&(currentCardDisplayedIndex == allFlashcards.size() - 1)){
                                currentCardDisplayedIndex = -1000;
                            }//for last card to be deleted
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // this method is called when the animation is finished playing
                            // set the question and answer TextViews with data from the database
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
                            }
                            findViewById(R.id.flashcardQuestion).startAnimation(rightInAnim);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            // we don't need to worry about this method
                        }
                    });


                    if(isQCardVisible == true)
                        findViewById(R.id.flashcardQuestion).startAnimation(UpAndOutAnim);
                    if(isQCardVisible == false) {
                        findViewById(R.id.flashcardAnswer).startAnimation(UpAndOutAnim);
                        isQCardVisible = true; //once the next card rolls in this is true
                    }


                    rightInAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            // this method is called when the animation first starts
                            findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                            findViewById(R.id.flashcardQuestion).setRotationY(0);
                            findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // this method is called when the animation is finished playing


                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            // we don't need to worry about this method
                        }
                    });


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
                }
                else {
                    ((TextView) findViewById(R.id.flashcardQuestion)).setText(strQ);

                }
                //Is the string empty for the answer?
                if (TextUtils.isEmpty(strAns)){
                    ((TextView) findViewById(R.id.flashcardAnswer)).setText("Insert an answer here");
                }
                else {
                    ((TextView) findViewById(R.id.flashcardAnswer)).setText(strAns);
                }
                findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                findViewById(R.id.flashcardQuestion).setRotationY(0);
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