package com.mycompany.coursework.resource;

import com.mycompany.coursework.model.Sensor;
import com.mycompany.coursework.store.DataStore;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public Collection<Sensor> getSensors(
            @QueryParam("type") String type) {

        if (type == null) {
            return DataStore.sensors.values();
        }

        List<Sensor> filteredSensors = new ArrayList<>();

        for (Sensor sensor : DataStore.sensors.values()) {
            if (sensor.getType().equalsIgnoreCase(type)) {
                filteredSensors.add(sensor);
            }
        }

        return filteredSensors;
    }

    @POST
    public Response addSensor(Sensor sensor) {

        if (DataStore.sensors.containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Sensor ID already exists")
                    .build();
        }

        if (!DataStore.rooms.containsKey(sensor.getRoomId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Room does not exist")
                    .build();
        }

        DataStore.sensors.put(sensor.getId(), sensor);

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }
}