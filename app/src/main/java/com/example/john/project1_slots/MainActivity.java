package com.example.john.project1_slots;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

/**
 * This class runs the slot machine game. Players start with $5 and each spin costs $1. If the
 * player matches 0 flowers, they lose $1, if they match 2, they win $2, and if they match all 3
 * then they win $3. Players may reset the game at any time and must reset if they lose all their
 * money.
 *
 * @author John Sweet
 * @version 02.09.2017
 */
public class MainActivity extends AppCompatActivity {

    ImageView flower1;
    ImageView flower2;
    ImageView flower3;

    ImageButton resetButton;
    ImageButton imageButton;

    TextView dollars;

    String moneyLeft;
    String finalMoneyLeft;

    int[] flowers = {R.drawable.f1, R.drawable.f2, R.drawable.f3};
    int money;
    int count;
    int random;

    /**
     * Creates first layout for when app is run.
     * @param savedInstanceState save
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        money = 5;
        count = 0;
        random = 0;
        resetButton = (ImageButton)findViewById(R.id.resetButton);
        resetButton.setVisibility(View.INVISIBLE);
        moneyLeft = getResources().getString(R.string.money_left);
    }

    /**
     * Rotates the flowers, changes them to temporary flowers while turning then changes again to
     * random flowers when the animation ends.
     * @param view view received
     */
    public void onClickGo(View view) {
        checkFunds();
        money--;
        count = 0;
        dollars = (TextView)findViewById(R.id.totalMoney);
        finalMoneyLeft = moneyLeft.format(moneyLeft, money);
        dollars.setText(finalMoneyLeft);

        imageButton = (ImageButton) findViewById(R.id.imageButton);

        //references to the flowers
        flower1 = (ImageView)findViewById(R.id.center_flower);
        flower2 = (ImageView)findViewById(R.id.left_flower);
        flower3 = (ImageView)findViewById(R.id.right_flower);

        //defines animation that will rotate the flowers
        Animation rotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);

        //changes flowers to the temporary flower image
        flower1.setImageResource(R.drawable.tmp);
        flower2.setImageResource(R.drawable.tmp);
        flower3.setImageResource(R.drawable.tmp);

        //rotates the 3 flowers
        flower1.startAnimation(rotateAnimation);
        flower2.startAnimation(rotateAnimation);
        flower3.startAnimation(rotateAnimation);

        //creates AnimationListener to determine when the rotate animation ends
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageButton.setClickable(false);
                resetButton.setClickable(false);
                resetButton.setVisibility(View.VISIBLE);
            }

            /**
             * Waits for rotate animation to end then randomizes the 3 flower colors
             * @param animation the rotate animation
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                randomize(flower1);
                int f1 = random;
                randomize(flower2);
                int f2 = random;
                randomize(flower3);
                int f3 = random;

                if(f1 == f2) {
                    count++;
                }
                if(f1 == f3) {
                    count++;
                }
                if(f2 ==  f3) {
                    count++;
                }
                checkMatches();
                checkFunds();

                TextView dollars = (TextView)findViewById(R.id.totalMoney);
                if(money >= 0) {
                    finalMoneyLeft = moneyLeft.format(moneyLeft, money);
                    dollars.setText(finalMoneyLeft);
                }
                else {
                    dollars.setText("$0");
                }
                imageButton.setClickable(true);
                resetButton.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * Determines the amount of money given or taken based on how many matches there are
     */
    public void checkMatches() {
        if(count == 0) {
            money--;
        }
        else if(count == 1) {
            money = money + 2;
        }
        else if(count == 3) {
            money = money + 3;
        }
    }

    /**
     * Checks the amount of money the player has. If they have $0 or less they have to reset the
     * game.
     */
    public void checkFunds() {
        if(money <= 0) {
            imageButton.setClickable(false);
            imageButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Makes the Go button visible and clickable again, sets the money back to $5 and displays it,
     * sets the flowers back to the purple color, and sets the rest button to invisible
     * @param view view received
     */
    public void onClickReset(View view) {
        imageButton.setClickable(true);
        imageButton.setVisibility(View.VISIBLE);
        money = 5;

        flower1.setImageResource(R.drawable.f1);
        flower2.setImageResource(R.drawable.f1);
        flower3.setImageResource(R.drawable.f1);

        dollars = (TextView)findViewById(R.id.totalMoney);
        finalMoneyLeft = moneyLeft.format(moneyLeft, money);
        dollars.setText(finalMoneyLeft);

        resetButton.setVisibility(View.INVISIBLE);
    }
    /**
     * Randomizes the ImageView to any 1 of the 3 possible flower colors.
     * @param view view received
     */
    public void randomize(ImageView view) {
        random = new Random().nextInt(3);
        view.setImageResource(flowers[random]);
    }
}