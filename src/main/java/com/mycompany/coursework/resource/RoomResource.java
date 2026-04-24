package com.mycompany.coursework.resource;

import com.mycompany.coursework.model.Room;
import com.mycompany.coursework.model.Sensor;
import com.mycompany.coursework.store.DataStore;
import java.util.Collection;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public Collection<Room> getRooms() {
        return DataStore.rooms.values();
    }

    @POST
    public Response addRoom(Room room) {

        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Room ID already exists")
                    .build();
        }

        DataStore.rooms.put(room.getId(), room);

        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }

    @GET
    @Path("/{id}")
    public Room getRoom(@PathParam("id") String id) {
        return DataStore.rooms.get(id);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") String id) {

        for (Sensor sensor : DataStore.sensors.values()) {
            if (sensor.getRoomId().equals(id)) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Cannot delete room with sensors assigned")
                        .build();
            }
        }

        DataStore.rooms.remove(id);

        return Response.ok("Room deleted").build();
    }
}