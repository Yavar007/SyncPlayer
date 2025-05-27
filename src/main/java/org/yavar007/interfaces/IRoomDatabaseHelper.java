package org.yavar007.interfaces;

import org.yavar007.models.UsersInRoomModel;

import java.util.List;

public interface IRoomDatabaseHelper {
    void insertData(UsersInRoomModel client);
    void updateData(UsersInRoomModel client);
    void deleteData(UsersInRoomModel client);
    UsersInRoomModel selectData(String id);
    List<UsersInRoomModel> selectAllData();
    void deleteAllData();
}
