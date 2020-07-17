package Gasconverter.Helpers;

import Gasconverter.MainActivity;
import Gasconverter.R;
import android.animation.*;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class AnimHelper {

    static Integer widthButton;
    static Integer llButtonHeight;
    static Integer CVSearchHeight;
    static Integer rowHeight;

    public static void AnimIncorrectParam(final View view) {
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(
                ObjectAnimator.ofArgb(view, "backgroundColor", Color.TRANSPARENT, Color.RED).setDuration(200),
                ObjectAnimator.ofArgb(view, "backgroundColor", Color.RED, Color.TRANSPARENT).setDuration(200));
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setBackgroundResource(R.drawable.line);
            }
        });
        set.start();
    }

    public static void AnimShadow (View view) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0.3f).setDuration(100));
        set.start();
    }

    public static void AnimReveal (View view) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, View.ALPHA, 0.3f, 1f).setDuration(100));
        set.start();
    }

    public static void AnimClick (View view) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0.3f).setDuration(200));
        set.play(ObjectAnimator.ofFloat(view, View.ALPHA, 0.3f, 1f).setDuration(200));
        set.start();
    }

    public static void enableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setEnabled(true);
    }

    public static void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setEnabled(false);
        editText.setEnabled(true);
    }


    public static void AnimRemoveButton(View view) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 0f).setDuration(300));
        set.play(ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 0f).setDuration(300));
        set.start();
    }

    public static void AnimAddButton(View view) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1f).setDuration(300));
        set.play(ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1f).setDuration(300));
        set.start();
    }


    public static void AnimShadowLLButtonQuick(final LinearLayout view) {
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        llButtonHeight = params.height;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 0f).setDuration(0);
        final ArrayList <Integer> value = new ArrayList<>();
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value.add(llButtonHeight - (int)(llButtonHeight * animation.getAnimatedFraction()));
                params.height = llButtonHeight - (int)(llButtonHeight * animation.getAnimatedFraction());
                view.setLayoutParams(params);
            }
        };
        objectAnimator.addUpdateListener(listener);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.d(MainActivity.LOG, "AnimShadowLLButtonQuick: llButtonHeight = " + value);
            }
        });
        objectAnimator.start();
    }

    public static void AnimShadowLLButton(final LinearLayout view) {
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        llButtonHeight = params.height;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 0f).setDuration(300);
        final ArrayList <Integer> value = new ArrayList<>();
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value.add(llButtonHeight - (int)(llButtonHeight * animation.getAnimatedFraction()));
                params.height = llButtonHeight - (int)(llButtonHeight * animation.getAnimatedFraction());
                view.setLayoutParams(params);
            }
        };
        objectAnimator.addUpdateListener(listener);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.d(MainActivity.LOG, "AnimShadowLLButton: llButtonHeight = " + value);
            }
        });
        objectAnimator.start();
    }

    public static void AnimRevealLLButton(final LinearLayout view) {
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1f).setDuration(300);
        final ArrayList <Integer> value = new ArrayList<>();
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value.add((int)(llButtonHeight * animation.getAnimatedFraction()));
                params.height = (int)(llButtonHeight * animation.getAnimatedFraction());
                view.setLayoutParams(params);
            }
        };
        objectAnimator.addUpdateListener(listener);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.d(MainActivity.LOG, "AnimRevealLLButton: llButtonHeight = " + value);
            }
        });
        objectAnimator.start();
    }


    public static void AnimRemoveButtonNewEdit(final View view, AnimatorListenerAdapter adapter) {
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        widthButton = params.width;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 0f).setDuration(300);
        final ArrayList <Integer> value = new ArrayList<>();
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value.add((widthButton - (int)(widthButton * animation.getAnimatedFraction())));
                params.width = widthButton - (int)(widthButton * animation.getAnimatedFraction());
                view.setLayoutParams(params);
            }
        };
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.d(MainActivity.LOG, "AnimRemoveButtonNewEdit: widthButton = " + value);
            }
        });
        objectAnimator.addUpdateListener(listener);
        objectAnimator.addListener(adapter);
        objectAnimator.start();
    }

    public static void AnimAddButtonNewEdit(final View view, AnimatorListenerAdapter adapter) {
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1f).setDuration(300);
        final ArrayList <Integer> value = new ArrayList<>();
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value.add((int)(widthButton * animation.getAnimatedFraction()));
                params.width = (int)(widthButton * animation.getAnimatedFraction());
                view.setLayoutParams(params);
            }
        };
        objectAnimator.addUpdateListener(listener);
        objectAnimator.addListener(adapter);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.d(MainActivity.LOG, "AnimRevealButtonNewEdit: widthButton = " + value);
            }
        });
        objectAnimator.start();
    }


    public static void AnimAddRow(final View view) {
        final ViewGroup.LayoutParams paramsChild = view.getLayoutParams();
        rowHeight = paramsChild.height;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, -1000f, 0).setDuration(300);
        final ArrayList <Integer> value = new ArrayList<>();
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value.add((int)(rowHeight * animation.getAnimatedFraction()));
                paramsChild.height = (int)(rowHeight * animation.getAnimatedFraction());
                view.setLayoutParams(paramsChild);
            }
        };
        objectAnimator.addUpdateListener(listener);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.d(MainActivity.LOG, "AnimNewRecipeAddEmptyRow: rowHeight = " + value);
            }
        });
        objectAnimator.start();
    }

    public static void AnimDeleteRow(final View view, AnimatorListenerAdapter adapter) {
        final ViewGroup.LayoutParams paramsChild = view.getLayoutParams();
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f, -1000f).setDuration(300);
        final ArrayList <Integer> value = new ArrayList<>();
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value.add(rowHeight- (int)(rowHeight * animation.getAnimatedFraction()));
                paramsChild.height = rowHeight- (int)(rowHeight * animation.getAnimatedFraction());
                view.setLayoutParams(paramsChild);
            }
        };
        objectAnimator.addUpdateListener(listener);
        set.play(objectAnimator);
        set.addListener(adapter);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.d(MainActivity.LOG, "AnimNewRecipeDeleteRow: rowHeight = " + value);
            }
        });
        set.start();
    }


    public static void AnimShadowCVSearch(final CardView view) {
        int topYcoord = view.getTop();
        int bottomYcoord = view.getBottom();
        CVSearchHeight = bottomYcoord-topYcoord;
        final LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) view.getLayoutParams();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 0f).setDuration(300);
        final ArrayList <Integer> value = new ArrayList<>();
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value.add(CVSearchHeight - (int)(CVSearchHeight * animation.getAnimatedFraction()));
                params.height = CVSearchHeight - (int)(CVSearchHeight * animation.getAnimatedFraction());
                view.setLayoutParams(params);
            }
        };
        objectAnimator.addUpdateListener(listener);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.d(MainActivity.LOG, "AnimShadowCVSearch: CVSearchHeight = " + value);
            }
        });
        objectAnimator.start();
    }

    public static void AnimRevealCVSearch(final CardView view) {
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1f).setDuration(300);
        final ArrayList <Integer> value = new ArrayList<>();
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value.add((int)(CVSearchHeight * animation.getAnimatedFraction()));
                params.height = (int)(CVSearchHeight * animation.getAnimatedFraction());
                view.setLayoutParams(params);
            }
        };
        objectAnimator.addUpdateListener(listener);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.d(MainActivity.LOG, "AnimRevealCVSearch: CVSearchHeight = " + value);
            }
        });
        objectAnimator.start();
    }
}