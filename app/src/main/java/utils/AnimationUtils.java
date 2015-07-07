package utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by gili on 6/30/2015.
 */
public class AnimationUtils {

    private static int counter = 0;

    public static void animate(RecyclerView.ViewHolder holder){
        YoYo.with(Techniques.RubberBand)
                .duration(1000)
                .playOn(holder.itemView);
    }
    public static void animateToolbar(View containerToolbar){

        containerToolbar.setRotationX(-90);
        containerToolbar.setAlpha(0.2F);
        containerToolbar.setPivotX(0.0F);
        containerToolbar.setPivotY(0.0F);
        Animator alpha = ObjectAnimator.ofFloat(containerToolbar, "alpha", 0.2F, 0.4F, 0.6F, 0.8F, 1.0F).setDuration(4000);
        Animator rotationX = ObjectAnimator.ofFloat(containerToolbar, "rotationX", -90, 60, -45, 45, -10, 30, 0, 20, 0, 5, 0).setDuration(8000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(alpha, rotationX);
        animatorSet.start();

    }

    public static void animateScatter(RecyclerView.ViewHolder holder, boolean goesDown) {
        counter = ++counter % 4;
        int holderHeight = holder.itemView.getHeight();
        int holderWidth = holder.itemView.getWidth();
        View holderItemView = holder.itemView;
        holderItemView.setPivotY(goesDown == true ? 0 : holderHeight);
        holderItemView.setPivotX(holderWidth / 2);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holderItemView, "translationY", goesDown == true ? 300 : -300, 0);
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(holderItemView, "translationX", counter == 1 || counter == 3 ? holderWidth : -holderWidth, 0);
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(holderItemView, "scaleX", counter == 1 || counter == 2 ? 0 : 2, 1f);
        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(holderItemView, "scaleY", counter == 1 || counter == 2 ? 0 : 2, 1f);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(holderItemView, "alpha", 0f, 1f);
        animatorAlpha.setInterpolator(new AccelerateInterpolator(1.5f));
        animatorSet.playTogether(animatorAlpha, animatorScaleX, animatorScaleY, animatorTranslateX, animatorTranslateY);
        animatorSet.setDuration(2000).setInterpolator(new DecelerateInterpolator(1.1f));
        animatorSet.start();
    }

    public static void animateSunblind(RecyclerView.ViewHolder holder, boolean goesDown) {
        int holderHeight = holder.itemView.getHeight();
        holder.itemView.setPivotY(goesDown == true ? 0 : holderHeight);
        holder.itemView.setPivotX(holder.itemView.getHeight());
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", goesDown == true ? 300 : -300, 0);
        ObjectAnimator animatorRotation = ObjectAnimator.ofFloat(holder.itemView, "rotationX", goesDown == true ? -90f : 90, 0f);
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(holder.itemView, "scaleX", 0.5f, 1f);
        animatorSet.playTogether(animatorTranslateY, animatorRotation, animatorScaleX);
        animatorSet.setInterpolator(new DecelerateInterpolator(1.1f));
        animatorSet.setDuration(1000);
        animatorSet.start();
    }
}
