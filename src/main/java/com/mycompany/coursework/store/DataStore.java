package com.mycompany.coursework.store;

import com.mycompany.coursework.model.Room;
import com.mycompany.coursework.model.Sensor;
import com.mycompany.coursework.model.Reading;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class DataStore {

    public static Map<String, Room> rooms = new HashMap<>();
    public static Map<String, Sensor> sensors = new HashMap<>();
    public static Map<String, List<Reading>> readings = new HashMap<>();

}