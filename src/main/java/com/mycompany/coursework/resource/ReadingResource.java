package com.mycompany.coursework.resource;

import com.mycompany.coursework.model.Reading;
import com.mycompany.coursework.model.Sensor;
import com.mycompany.coursework.store.DataStore;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/sensors/{id}/readings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReadingResource {

    @POST
    public Response addReading(
            @PathParam("id") String sensorId,
            Reading reading) {

        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor != null && sensor.getStatus().equalsIgnoreCase("MAINTENANCE")) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Sensor is under maintenance")
                    .build();
        }

        if (!DataStore.readings.containsKey(sensorId)) {
            DataStore.readings.put(sensorId, new ArrayList<>());
        }

        DataStore.readings.get(sensorId).add(reading);

        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }

    @GET
    public List<Reading> getReadings(
            @PathParam("id") String sensorId) {

        return DataStore.readings.getOrDefault(
                sensorId,
                new ArrayList<>());
    }
}