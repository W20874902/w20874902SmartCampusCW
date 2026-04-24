# w20874902 Smart Campus REST API

## API Design Overview

This project was developed using JAX-RS and follows RESTful architectural principles.  
The API manages smart campus resources by exposing them through URI endpoints.

The main resources implemented are:

- Rooms
- Sensors
- Sensor Readings

Each resource is represented using dedicated classes:

- RoomResource
- SensorResource
- ReadingResource
- DiscoveryResource

The API uses the following HTTP methods:

- GET
- POST
- DELETE

Path parameters are handled using:

- @PathParam

Query filtering is handled using:

- @QueryParam

JSON is used as the default communication format through:

- @Consumes(MediaType.APPLICATION_JSON)
- @Produces(MediaType.APPLICATION_JSON)

The application stores data using in-memory collections inside `DataStore.java`.

These include:

- HashMap for rooms
- HashMap for sensors
- ArrayList for readings

The API includes:

- Discovery endpoint
- Room management
- Sensor management
- Reading management
- Query filtering
- Exception handling
- Logging filters

---

# Step By Step Instructions To Build And Run

1. Download the project files

2. Extract the project folder

3. Open the project in Apache NetBeans

4. Go to the **Services** tab

5. Right click **Servers**

6. Select:

Add Server

7. Choose:

Apache Tomcat 9

8. Locate your Tomcat folder:

```text
apache-tomcat-9.0.100
```

9. Enter username and password

10. Click Finish

11. Start the Tomcat server

12. Confirm the green play icon appears

13. Right click project

14. Select:

Clean and Build

15. Right click project again

16. Select:

Run

The API base URL:

```text
http://localhost:8080/w20874902SmartCampusCW/api/v1
```

---

# Sample cURL Requests

## Create Room

```bash
curl.exe --% -X POST http://localhost:8080/w20874902SmartCampusCW/api/v1/rooms ^
-H "Content-Type: application/json" ^
-d "{\"id\":\"A101\",\"name\":\"Lecture Hall\",\"capacity\":50}"
```

---

## Get All Rooms

```bash
curl.exe --% -X GET http://localhost:8080/w20874902SmartCampusCW/api/v1/rooms
```

---

## Get Specific Room

```bash
curl.exe --% -X GET http://localhost:8080/w20874902SmartCampusCW/api/v1/rooms/A101
```

---

## Create Sensor

```bash
curl.exe --% -X POST http://localhost:8080/w20874902SmartCampusCW/api/v1/sensors ^
-H "Content-Type: application/json" ^
-d "{\"id\":\"S1\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":450,\"roomId\":\"A101\"}"
```

---

## Get All Sensors

```bash
curl.exe --% -X GET http://localhost:8080/w20874902SmartCampusCW/api/v1/sensors
```

---

## Filter Sensors

```bash
curl.exe --% -X GET "http://localhost:8080/w20874902SmartCampusCW/api/v1/sensors?type=CO2"
```

---

## Create Reading

```bash
curl.exe --% -X POST http://localhost:8080/w20874902SmartCampusCW/api/v1/sensors/S1/readings ^
-H "Content-Type: application/json" ^
-d "{\"timestamp\":\"2026-04-23T14:00:00\",\"value\":475}"
```

---

## Get Reading History

```bash
curl.exe --% -X GET http://localhost:8080/w20874902SmartCampusCW/api/v1/sensors/S1/readings
```

---

# Coursework Questions

## Q1 Project & Application Configuration

By default, JAX-RS resource classes operate on a per-request basis, which means that each time an HTTP request comes in, a new instance of the resource class is created unless I specifically mark it as a singleton with the @Singleton annotation. This setup helps avoid issues with shared state since the instance variables in resource classes aren't shared across different requests. In this project, I needed the application data to persist between requests. If I had stored rooms, sensors, and readings directly in the resource classes, I would have faced data loss. To address this, I used shared in-memory collections within the DataStore class, utilizing HashMaps and ArrayLists. This way, the data stays accessible while the application is running. In larger systems, I might also need synchronization methods to prevent race conditions when multiple requests try to change shared data at the same time.

---

## Q2 Discovery Endpoint

Hypermedia, which is also referred to as HATEOAS (Hypermedia as the Engine of Application State), enables API responses to feature links to related resources that I can allow clients to explore dynamically. This is seen as a sophisticated REST principle since clients aren't solely dependent on fixed documentation to figure out how to use the API. In this project, I implemented a discovery endpoint that offers links to key resources like rooms and sensors. This enhances flexibility because if the structure of endpoints changes in later versions, clients can still navigate the API using the links provided by my server.

---

## Q3 Returning Full Objects vs IDs

When I send back a list of rooms, there are some trade-offs to consider between just sending room IDs and sending complete room objects. Sending only IDs uses less bandwidth since the response is smaller, but clients might have to make more requests to get the full details of the rooms. On the other hand, sending full room objects makes the response larger but cuts down on the need for extra requests because clients get all the information they need right away. In this project, I decided to send full room objects because it makes things easier for users and helps to minimize unnecessary network traffic from those extra requests.

---

## Q4 DELETE Idempotency

DELETE operations are considered idempotent because sending the same DELETE request multiple times should still leave the resource in the same final state. In my project, the first DELETE request removes the room from the system successfully. If the same request is sent again, the room has already been deleted so the API may return a not found response. Even though the response may be different the second time, the final state of the system stays the same because the room remains deleted.

---

## Q5 @Consumes JSON

I used `@Consumes(MediaType.APPLICATION_JSON)` to make sure my API only accepts JSON request bodies. If a client tries to send data in another format such as `text/plain` or `application/xml`, JAX-RS will reject the request because the content type does not match what my API accepts. This usually returns a HTTP 415 Unsupported Media Type response. This helps keep requests consistent and ensures clients send data in the correct format.

---

## Q6 Query Filtering

I used query parameters for filtering because filtering does not represent a completely new resource. In my project I used `/api/v1/sensors?type=CO2` to return only sensors that match a specific type. Using something like `/api/v1/sensors/type/CO2` would make the filter seem like part of the resource path which is less suitable. Query parameters are also more flexible because I could easily add more filters later if needed.

---

## Q7 Sub Resource Locator Pattern

The sub-resource locator pattern helped me keep my API more organised by separating nested functionality into different classes. In my project `SensorResource` handles sensor related operations while `ReadingResource` handles sensor readings. If I placed all nested endpoints inside one large resource class, the code would become harder to manage as the project grows. Separating them makes the code easier to read, maintain and test.

---

## Q8 HTTP 422 Validation

HTTP 422 is more suitable than HTTP 404 when the request itself is valid but contains incorrect reference data. In my project a user could try creating a sensor using a room ID that does not exist. The API understands the request format, but it cannot complete the request because the linked room is invalid. This is why I believe HTTP 422 is more accurate than returning a generic 404 error.

---

## Q9 Security Risks

Exposing Java stack traces to users can be dangerous because attackers could learn important technical details about the application. Stack traces may reveal things such as file paths, package names, class names, framework versions and server information. Attackers could use this information to find weaknesses in the system. This is why APIs should return simple error messages instead of exposing internal errors.

---

## Q10 Logging Filters

Using JAX-RS filters for logging is useful because logging affects every request and response. If I manually added logging statements inside every resource method, it would create a lot of repeated code and make the project harder to maintain. By using request and response filters, I can handle all logging in one place which keeps the code cleaner and easier to manage.
