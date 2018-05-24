package com.bibangamba.ebaala.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by davy on 5/22/2018.
 */

public class RecylerClickListener implements RecyclerView.OnItemTouchListener {

    private ClickListener clickListener;
    private GestureDetector gestureDetector;

    public RecylerClickListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {

        this.clickListener = clickListener;

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

            @Override
            public void onLongPress(MotionEvent e) {


                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (view != null && clickListener != null) {
                    clickListener.onLongClick(view, recyclerView.getChildPosition(view));
                }
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View view = rv.findChildViewUnder(e.getX(), e.getY());
        if (view != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(view, rv.getChildPosition(view));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface ClickListener {

        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }
}
