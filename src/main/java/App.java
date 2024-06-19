import spark.Request;
import spark.Spark;
import spark.servlet.SparkApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.GenericResponseWrapper;
import dungeonmania.util.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import scintilla.Scintilla;

/**
 * A threadsafe wrapper around your DungeonManiaController.
 * It does this by storing a series of session states
 * 
 * You shouldn't need to modify this.
 * 
 * @author Braedon Wooding, Nick Patrikeos, 
 *         Noa Challis, George Litsas.
 *         Chloe Cheong, Webster Zhang, Sienna Archer
 */
public class App implements SparkApplication {
    private final class InvalidActionExceptionAPI extends RuntimeException {
        public InvalidActionExceptionAPI(String message) {
            super(message);
        }
    }
    private static volatile Map<String, DungeonManiaController> sessionStates = new HashMap<>();

    private static synchronized DungeonManiaController getDungeonManiaController(Request request) {
        String session = request.session().id();
        if (session == null) {
            System.out.println("No Session Found... using default.");
            session = "__DEFAULT_SESSION__";
        }

        if (sessionStates.containsKey(session)) {
            return sessionStates.get(session);
        } else {
            DungeonManiaController bc = new DungeonManiaController();
            sessionStates.put(session, bc);
            return bc;
        }
    }

    private static<T> GenericResponseWrapper<T> callWithWrapper(Supplier<T> runnable) {
        try {
            return GenericResponseWrapper.Ok(runnable.get());
        } catch (Exception e) {
            e.printStackTrace();
            return GenericResponseWrapper.Err(e);
        }
    }

    private static<T> GenericResponseWrapper<T> callUsingSessionAndArgument(Request request, Function<DungeonManiaController, T> runnable) {
        try {
            DungeonManiaController dmc = getDungeonManiaController(request);
            synchronized (dmc) {
                return GenericResponseWrapper.Ok(runnable.apply(dmc));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GenericResponseWrapper.Err(e);
        }
    }

    @Override
    public void init() {
        Scintilla.initialize(); 
        GsonBuilder gsonBuilder = new GsonBuilder();

        Gson gson = gsonBuilder.create();
        Object globalLock = new Object();

        Spark.after((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });

        Spark.get("/api/dungeons/", "application/json", (request, response) -> {
            // we don't *need* to globally lock this but we might as well just to keep a nice standard.
            synchronized (globalLock) {
                return callWithWrapper(() -> DungeonManiaController.dungeons());
            }
        }, gson::toJson);

        Spark.get("/api/configs/", "application/json", (request, response) -> {
            // we don't *need* to globally lock this but we might as well just to keep a nice standard.
            synchronized (globalLock) {
                return callWithWrapper(() -> DungeonManiaController.configs());
            }
        }, gson::toJson);

        Spark.post("/api/game/new/", "application/json", (request, response) -> {
            return callUsingSessionAndArgument(request, (dmc) -> dmc.newGame(request.queryParams("dungeonName"), request.queryParams("configName")));
        }, gson::toJson);
        Spark.post("/api/game/rewind/", "application/json", (request, response) -> {
            return callUsingSessionAndArgument(request, (dmc) -> {
                try {
                    return dmc.rewind(Integer.parseInt(request.queryParams("ticks")));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(e.getMessage());
                }
            });

        }, gson::toJson);
        Spark.post("/api/game/new/generate/", "application/json", (request, response) -> {
            return callUsingSessionAndArgument(request, (dmc) -> dmc.generateDungeon(Integer.parseInt(request.queryParams("xStart")), Integer.parseInt(request.queryParams("yStart")), Integer.parseInt(request.queryParams("xEnd")), Integer.parseInt(request.queryParams("yEnd")), request.queryParams("configName")));
        }, gson::toJson);
        Spark.post("/api/game/tick/item/", "application/json", (request, response) -> {
            return callUsingSessionAndArgument(request, (dmc) -> {
                try {
                    return dmc.tick(request.queryParams("itemUsed"));
                } catch (InvalidActionException e) {
                    throw new InvalidActionExceptionAPI(e.getMessage());
                }
            });
        }, gson::toJson);

        Spark.post("/api/game/tick/movement/", "application/json", (request, response) -> {
            return callUsingSessionAndArgument(request, (dmc) -> dmc.tick(Direction.valueOf(request.queryParams("movementDirection").toUpperCase())));
        }, gson::toJson);

        Spark.post("/api/game/build/", "application/json", (request, response) -> {
            return callUsingSessionAndArgument(request, (dmc) -> {
                try {
                    return dmc.build(request.queryParams("buildable"));
                } catch (InvalidActionException e) {
                    throw new InvalidActionExceptionAPI(e.getMessage());
                }
            });
        }, gson::toJson);

        Spark.get("/api/skin/current/", "application/json", (request, response) -> {
            return callUsingSessionAndArgument(request, (dmc) -> dmc.getSkin());
        }, gson::toJson);

        Spark.get("/api/localisation/current/", "application/json", (request, response) -> {
            return callUsingSessionAndArgument(request, (dmc) -> dmc.getLocalisation());
        }, gson::toJson);

        Spark.post("/api/game/interact/", "application/json", (request, response) -> {
            return callUsingSessionAndArgument(request, (dmc) -> {
                try {
                    return dmc.interact(request.queryParams("entityId"));
                } catch (InvalidActionException e) {
                    throw new InvalidActionExceptionAPI(e.getMessage());
                }
            });
        }, gson::toJson);

        Spark.post("/api/game/dungeonResponseModel/", "application/json", (request, response) -> {
            return callUsingSessionAndArgument(request, (dmc) -> dmc.getDungeonResponseModel());
        }, gson::toJson);

        Spark.post("api/game/save/", "application/json", (request, response) -> {
            return callUsingSessionAndArgument(request, (dmc) -> dmc.saveGame(request.queryParams("name")));
        }, gson::toJson);

        Spark.post("api/game/load/", "application/json", (request, response) -> {
            return callUsingSessionAndArgument(request, (dmc) -> dmc.loadGame(request.queryParams("name")));
        }, gson::toJson);

        Spark.get("api/games/all/", "application/json", (request, response) -> {
            return callUsingSessionAndArgument(request, (dmc) -> dmc.allGames());
        }, gson::toJson);

        Scintilla.start();
    }

    public static void main(String[] args) throws Exception {
        new App().init();
    }
}