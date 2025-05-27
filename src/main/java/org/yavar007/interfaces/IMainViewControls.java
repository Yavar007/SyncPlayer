package org.yavar007.interfaces;


import org.yavar007.models.UsersInRoomModel;

public interface IMainViewControls {
        //void setUsername(String username);
        void setMovieLink(String movieLink);
        void setMovieTime(String movieTime);
        void addNewSpectator(UsersInRoomModel spectator);
        void showToast(String message);
}

