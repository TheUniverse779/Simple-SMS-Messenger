package com.messenger.views.sliding.callback;


public interface DragStateListener {

    void onDragStart();

    void onDragEnd(boolean isMenuOpened);
}
