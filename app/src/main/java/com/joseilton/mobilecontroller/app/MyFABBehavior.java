package com.joseilton.mobilecontroller.app;

import android.content.*;
import android.support.design.widget.*;
import android.support.v4.view.*;
import android.util.*;
import android.view.*;


public class MyFABBehavior extends FloatingActionButton.Behavior {

    public MyFABBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout,
                                       final FloatingActionButton child,
                                       final View directTargetChild, final View target,
                                       final int nestedScrollAxes) {

        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed);

        child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            /**
             * Called when a FloatingActionButton has been hidden
             *
             * @param fab the FloatingActionButton that was hidden.
             */
            @Override
            public void onHidden(FloatingActionButton fab) {
                super.onShown(fab);
                fab.setVisibility(View.INVISIBLE);
            }
        });
    }
}

