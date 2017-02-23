package com.wackydeveloper.designersaree.ui;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment ;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.wackydeveloper.designersaree.Helper.SlideshowDialogFragment;
import com.wackydeveloper.designersaree.ImageLargeViewActivity;
import com.wackydeveloper.designersaree.R;
import android.support.v4.app.FragmentManager;



import com.wackydeveloper.designersaree.SwipeViewActivity;
import com.wackydeveloper.designersaree.bus.RxBus;
import com.wackydeveloper.designersaree.bus.events.TopCardMovedEvent;
import com.wackydeveloper.designersaree.model.Image;
import com.wackydeveloper.designersaree.model.User;
import  com.wackydeveloper.designersaree.ui.TinderStackLayout ;
import com.wackydeveloper.designersaree.utilities.DisplayUtility;


public class TinderCardView extends FrameLayout implements View.OnTouchListener {

    // region Constants
    private static final float CARD_ROTATION_DEGREES = 40.0f;
    private static final float BADGE_ROTATION_DEGREES = 15.0f;
    private static final int DURATION = 300;
    // endregion

    // region Views
    private ImageView imageView;
    private TextView displayNameTextView;
    private TextView usernameTextView;
    private TextView likeTextView;
    private TextView nopeTextView;
    private Context  mContext ;
    // endregion

    // region Member Variables
    private float oldX;
    private float oldY;
    private float newX;
    private float newY;
    private float dX;
    private float dY;
    private float tempX ;
    private float tempY ;
    private float rightBoundary;
    private float leftBoundary;
    private int screenWidth;
    private int padding;
    // endregion

    // region Constructors
    public TinderCardView(Context context) {
        super(context);
        this.mContext = context ;
        init(context, null);
    }

    public TinderCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context ;
        init(context, attrs);
    }

    public TinderCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context ;
        init(context, attrs);
    }
    // endregion

    // region View.OnTouchListener Methods
    @Override
    public boolean onTouch(final View view, MotionEvent motionEvent) {
        TinderStackLayout tinderStackLayout = ((TinderStackLayout)view.getParent());
        TinderCardView topCard = (TinderCardView) tinderStackLayout.getChildAt(tinderStackLayout.getChildCount()-1);
        if(topCard.equals(view)){
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    oldX = motionEvent.getX();
                    oldY = motionEvent.getY();

                    // cancel any current animations
                    view.clearAnimation();
                    return true;
                case MotionEvent.ACTION_UP:
                    tempX = motionEvent.getX();
                    tempY = motionEvent.getY();
                    float distance = tempX - oldX ;
                    if(isCardBeyondLeftBoundary(view)){
                        RxBus.getInstance().send(new TopCardMovedEvent(-(screenWidth)));
                        dismissCard(view, -(screenWidth * 2));
                        SwipeViewActivity.topCardIndex++ ;
                    } else if(isCardBeyondRightBoundary(view)){
                        RxBus.getInstance().send(new TopCardMovedEvent(screenWidth));
                        dismissCard(view, (screenWidth * 2));
                        SwipeViewActivity.topCardIndex++ ;
                    } else if(distance == 0){
                        Log.v("Nikhil" ,"ImageView Touch Detected !! " ) ;
                        Bundle bundle = new Bundle();
                            Activity act = (SwipeViewActivity) mContext ;

                        bundle.putSerializable("images",((SwipeViewActivity) mContext).getImagesObject());
                        bundle.putInt("position", (SwipeViewActivity.topCardIndex));
 //STACK_SIZE  =4
//                        Image img =  ((SwipeViewActivity) mContext).getImagesObject().get(((SwipeViewActivity) mContext).getIndex()-4) ;
//                         String url = img.getLarge() ;

                        Intent intent = new Intent(act,ImageLargeViewActivity.class);
                        intent.putExtra("customBundle" , bundle ) ;
                        mContext.startActivity(intent);


//                        android.support.v4.app.FragmentTransaction ft = act.get                               //getSupportFragmentManager().beginTransaction();
//                        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
//                        newFragment.setArguments(bundle);
//                        newFragment.show(ft, "slideshow");


                    } else {
                        RxBus.getInstance().send(new TopCardMovedEvent(0));
                        resetCard(view);
                    }
                    return true;
                case MotionEvent.ACTION_MOVE:
                    newX = motionEvent.getX();
                    newY = motionEvent.getY();

                    dX = newX - oldX;
                    dY = newY - oldY;

                    float posX = view.getX() + dX;

                    RxBus.getInstance().send(new TopCardMovedEvent(posX));

                    // Set new position
                    view.setX(view.getX() + dX);
                    view.setY(view.getY() + dY);

                    setCardRotation(view, view.getX());

                    updateAlphaOfBadges(posX);
                    return true;
                default :
                    return super.onTouchEvent(motionEvent);
            }
        }
        return super.onTouchEvent(motionEvent);
    }
    // endregion

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setOnTouchListener(null);
    }

    // region Helper Methods
    private void init(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            inflate(context, R.layout.tinder_card, this);

            imageView = (ImageView) findViewById(R.id.iv);
            displayNameTextView = (TextView) findViewById(R.id.display_name_tv);
            usernameTextView = (TextView) findViewById(R.id.username_tv);
            likeTextView = (TextView) findViewById(R.id.like_tv);
            nopeTextView = (TextView) findViewById(R.id.nope_tv);

            likeTextView.setRotation(-(BADGE_ROTATION_DEGREES));
            nopeTextView.setRotation(BADGE_ROTATION_DEGREES);

            screenWidth = DisplayUtility.getScreenWidth(context);
            leftBoundary =  screenWidth * (1.0f/6.0f); // Left 1/6 of screen
            rightBoundary = screenWidth * (5.0f/6.0f); // Right 1/6 of screen
            padding = DisplayUtility.dp2px(context, 5);

            setOnTouchListener(this);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                //@Override
//                public void onClick(View v) {
//                    Log.v("Nikhl", " click on image : haha" );
//                }
//            });
        }
    }

    // Check if card's middle is beyond the left boundary
    private boolean isCardBeyondLeftBoundary(View view){
        return (view.getX() + (view.getWidth() / 2) < leftBoundary);
    }

    // Check if card's middle is beyond the right boundary
    private boolean isCardBeyondRightBoundary(View view){
        return (view.getX() + (view.getWidth() / 2) > rightBoundary);
    }

    private void dismissCard(final View view, int xPos){
        view.animate()
                .x(xPos)
                .y(0)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        ViewGroup viewGroup = (ViewGroup) view.getParent();
                        if(viewGroup != null) {
                            viewGroup.removeView(view);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
    }

    private void resetCard(View view){
        view.animate()
                .x(0)
                .y(0)
                .rotation(0)
                .setInterpolator(new OvershootInterpolator())
                .setDuration(DURATION);

        likeTextView.setAlpha(0);
        nopeTextView.setAlpha(0);
    }

    private void setCardRotation(View view, float posX){
        float rotation = (CARD_ROTATION_DEGREES * (posX)) / screenWidth;
        int halfCardHeight = (view.getHeight() / 2);
        if(oldY < halfCardHeight - (2*padding)){
            view.setRotation(rotation);
        } else {
            view.setRotation(-rotation);
        }
    }

    // set alpha of like and nope badges
    private void updateAlphaOfBadges(float posX){
        float alpha = (posX - padding) / (screenWidth * 0.50f);
        likeTextView.setAlpha(alpha);
        nopeTextView.setAlpha(-alpha);
    }

    public void bind(User user){
        if(user == null)
            return;

        setUpImage(imageView, user);
        setUpDisplayName(displayNameTextView, user);
        setUpUsername(usernameTextView, user);
    }
    public void bind(Image image){
        if(image == null)
            return ;
        setUpImage(imageView,image);
        setUpDisplayName(displayNameTextView, image);
        setUpUsername(usernameTextView, image);
    }

    private void setUpImage(ImageView iv, Image img){
        String avatarUrl = img.getMedium();
        if(!TextUtils.isEmpty(avatarUrl)){
            Picasso.with(iv.getContext())
                    .load(avatarUrl)
                    .into(iv);
        }
    }

    private void setUpImage(ImageView iv, User user){
        String avatarUrl = user.getAvatarUrl();
        if(!TextUtils.isEmpty(avatarUrl)){
            Picasso.with(iv.getContext())
                    .load(avatarUrl)
                    .into(iv);
        }
    }
    private void setUpDisplayName(TextView tv, Image img){
        String displayName = img.getName();
        if(!TextUtils.isEmpty(displayName)){
            tv.setText(displayName);
        }
    }
    private void setUpDisplayName(TextView tv, User user){
        String displayName = user.getDisplayName();
        if(!TextUtils.isEmpty(displayName)){
            tv.setText(displayName);
        }
    }

    private void setUpUsername(TextView tv, Image img){
        String username = img.getName();
        if(!TextUtils.isEmpty(username)){
            tv.setText(username);
        }
    }
    private void setUpUsername(TextView tv, User user){
        String username = user.getUsername();
        if(!TextUtils.isEmpty(username)){
            tv.setText(username);
        }
    }

    // endregion
}
